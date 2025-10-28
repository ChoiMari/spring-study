package com.example.demo.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//스프링 부트에서는 설정을 xml로 하지 않고
// 자바 파일에서 한다 
// @Configuration 사용해서 bean 객체를 생성해서 주입
@Configuration
@EnableWebSecurity //-> 이 클래스는 시큐리티 설정이 가능하다는 어노테이션
//(버전 : 5.x.x -> 6.x.x 변화가 있다!)
public class SecurityConfig {
//	@Bean
//	public Emp call(){
//		return new Emp();
//	}
	
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
				                            .anyRequest().authenticated()// 이걸 제외한 남은 나머지는 인증된 사용자만 허락하겠다
				                            ).formLogin(form -> form.permitAll() 
				                            		).logout(logout -> logout.permitAll());  
		
		return http.build();
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
	
	// 인증 권한 - (인 메모리)
	@Bean
	public UserDetailsService userDetailsService() { 
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
	}
	
}
