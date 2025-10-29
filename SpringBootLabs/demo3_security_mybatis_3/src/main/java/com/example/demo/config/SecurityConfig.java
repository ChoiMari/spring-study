package com.example.demo.config;



import javax.sql.DataSource;
import static org.springframework.security.config.Customizer.withDefaults;

import org.apache.ibatis.javassist.bytecode.ExceptionsAttribute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo.security.CustomerAccessDeniedHandler;
import com.example.demo.security.CustomerDetailService;
import com.example.demo.security.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

//스프링 부트에서는 설정을 xml로 하지 않고
// 자바 파일에서 한다 
// @Configuration 사용해서 bean 객체를 생성해서 주입
@Configuration
@EnableWebSecurity //-> 이 클래스는 시큐리티 설정이 가능하다는 어노테이션
//(버전 : 5.x.x -> 6.x.x 변화가 있다!)
@RequiredArgsConstructor // 롬복
public class SecurityConfig {
	//시큐리티 컨피그는 데이터 소스에 의존합니다(데이터 소스 주소가 필요하다)
	private final DataSource dataSource; // 자동 주입됨(@RequiredArgsConstructor)
	// implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	
	private final CustomerDetailService customerDetailService;
	
	// 의존성 추가
//	@Bean
//	public Emp call(){
//		return new Emp();
//	}
	
	//사용자 정의 방식으로 
	// 원래 유저디테일스는 자동 만들어주는건데 커스텀으로 만들어 버림(implements)
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		//인증과 권한을 한 곳에 모아서 처리하기를 원함
		// 자원(특정 페이지 또는 폴더, 특정 자원)에 대해서
		// 사용자별 권한 접근 제한을 하겠다
		//url 경로에 /admin -> ROLE_USER 안되고 ROLE_ADMIN 권한만 가능하다
		// 이런걸 하겠다!
		
		/* 스프링 프레임워크(레거시에서 사용한 방식)
		 가로 챌것이다. 엑세스가 가능한지...!!!  url을 인터셉터
		 spring regacy 사용한 security 3.x.x 대 문법
		   <security:intercept-url pattern="/customer/noticeDetail.do" access="hasRole('ROLE_USER')" />
          <security:intercept-url pattern="/customer/noticeReg.do" access="hasRole('ROLE_ADMIN')" />

        spring boot   security 버전 5.x.x 대 문법
		   http.authorizeRequests()
				.antMatchers("/admin","/admin/**").hasRole("ADMIN") //접속한 사용자가 ADMIN이면 통과
				.antMatchers("/user","/user/**").hasAnyRole("USER","ADMIN") //접속한 사용자가 USER, ADMIN이면 통과
				.antMatchers("/css/**" , "/js/**" , "/imges/**").permitAll() //permitAll() : 모든 사용자 허용
				.antMatchers("/**").permitAll() // 나머지도 허용
				.anyRequest().authenticated();
		
		
		 spring boot   security 버전 6.x.x 대도 또 바뀌었다!!
			람다 표현식 사용
			
			보통 스프링 부트로 프로젝트 하게 된다면?
			spring framework는 6.x.x
			스프링 부트는 3.x.x
			시큐리티는 6.x.x
			(자바 버전도 바꾸어야 한다고 함)
		 */
		
		http.authorizeHttpRequests(auth -> auth   //ROLE_는 생략이 가능함. ADMIN권한만 허용
				                            .requestMatchers("/admin/**").hasRole("ADMIN")
				                            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // hasAnyRole 또는
				                            .requestMatchers("/css/**", "/js/**", "/image/**").permitAll() // /경로가 static
				                            .requestMatchers("/", "/**").permitAll() // 위의 경로 부터 잡아서..남은 나머지는.. 이라고
				                            .anyRequest().authenticated())// 이걸 제외한 남은 나머지는 인증된 사용자만 허락하겠다
				                            .logout(withDefaults()); //스프링이 정의한 기본 로그아웃을 사용하겠다
											// .formLogin(withDefaults()); //로그인 방식 기본값 유지
				//import static org.springframework.security.config.Customizer.withDefaults;
		
		http.logout(logout -> logout.logoutUrl("/logout")// 로그아웃 요청 받을 url
				.logoutSuccessUrl("/")//로그아웃 후 이동할 url
				.deleteCookies("JSESSIONID") // 세션 id 삭제(쿠키 삭제)
				.invalidateHttpSession(true)); // 세션의 여러가지 정보도 삭제(세션 객체 삭제) 
		
		http.formLogin(form -> form.loginPage("/login") // 커스텀 로그인 페이지 요청 경로
				.loginProcessingUrl("/loginPro") // 커스텀 로그인 처리 경로
				//.defaultSuccessUrl("/") // 로그인 성공 후 이동할 url 설정
				.usernameParameter("id") //로그인 뷰의 태그 name속성 파라미터 바꾸기
				.passwordParameter("pw") //로그인 뷰의 태그 name속성 파라미터 바꾸기
				.successHandler(authenticationSuccessHandler()) // 로그인에 성공하면 메일을 보내겠다.
				.permitAll()); // 모든 사용자에게 로그인 페이지 접근 허용
		
		http.userDetailsService(customerDetailService); // 사용자 정의 인증 방식으로
		// 마이바티스를 사용하는 걸로 바꿈
		
		http.exceptionHandling(exceptions -> exceptions.accessDeniedHandler(accessDeniedHandler()));
		//http.csrf(csrf -> csrf.disable()); 처음 테스트할 땐, 비 활성화 해놓고 사용해라.
		return http.build();
		
		/*
		 http
   		.formLogin(form -> form
       .loginPage("/login")              // 내가 만든 로그인 페이지 URL
       .loginProcessingUrl("/loginProc") // 로그인 요청을 처리할 URL (Controller 필요 없음)
       .usernameParameter("userId")      // 로그인 폼에서 name="userId" 인 입력 필드 사용
       .passwordParameter("userPw")      // name="userPw"
       .defaultSuccessUrl("/home", true) // 로그인 성공 시 이동
       .failureUrl("/login?error=true")  // 실패 시 이동
       .permitAll()
       loginProcessingUrl()은 Spring Security가 직접 처리하기 때문에
       Controller에서 /loginProc 매핑을 만들 필요가 없습니다.
        );
	  
		 */
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomerAccessDeniedHandler();
	}
	
	/*
		예전에 쓰던 코드.. 인메모리 방식으로 사용자, 암호, 권한 설정해서 Test할 수 있다
		이게 끝나면 DB작업 하면 된다고..
		<security:user-service>
			<security:user name="hong"  password="1004" authorities="ROLE_USER"/>
			<security:user name="admin" password="1004" authorities="ROLE_USER,ROLE_ADMIN"/>
		</security:user-service>
*/
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 알고리즘 사용
        return new BCryptPasswordEncoder();
    }
	
	
    /*
     마이바티스 사용으로 필요없어져서 주석처리함
	@Bean
	public UserDetailsService userDetailsService() {  */
		/*
		 * // 인증 권한 - (인 메모리)
		// (PasswordEncoder passwordEncoder) 이건 빈을 일단 등록은 시켜놓아야 파라미터로 주입됨, 빈 등록없이 그냥 파라미터 선언만 한다고 자동 주입되는게 아니다..
		// 사용자 정보(원칙은 DB에서 가지고 와야하지만, 인메모리로 테스트가 가능하다)
		
		// 사용자 정보 유저디테일 타입 객체로 만듬(인터페이스)
		UserDetails user = User.builder()
				.username("user")
				.password(passwordEncoder().encode("1004"))
				.roles("USER")
	            .build();
		
		UserDetails admin = User.builder()
				.username("admin")
				.password(passwordEncoder().encode("1007"))
				.roles("USER", "ADMIN")
	            .build();
		
		
		return new InMemoryUserDetailsManager(user, admin);
		//in memory 방식 사용자 생성
		 */
		
		//DB 붙임 - JDBC
		//JDBC 연결
		  /*
	    JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
	    	
        // 사용자 정보 쿼리
        String sql1 = "SELECT user_id AS username, user_pw AS password, enabled FROM user WHERE user_id = ?";
        // 권한 정보 쿼리
        String sql2 = "SELECT user_id AS username, auth FROM user_auth WHERE user_id = ?";

        userDetailsManager.setUsersByUsernameQuery(sql1);
        userDetailsManager.setAuthoritiesByUsernameQuery(sql2);

        return userDetailsManager;
		
	}
	
*/
	// authenticationManager 구현하기
	//로그인 성공시 필요한 부분 처리 
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler(){
			return new LoginSuccessHandler();
		}
    
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
