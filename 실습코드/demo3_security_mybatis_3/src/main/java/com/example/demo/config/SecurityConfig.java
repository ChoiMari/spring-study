package com.example.demo.config;


import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo.security.CustomerAccessDeniedHandler;
import com.example.demo.security.CustomerDetailService;
import com.example.demo.security.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

//Spring boot 에서 설정 (xml) 하지 않고 java 파일 (@Configuration 사용해서 빈객체 생성 주입)

@Configuration
@EnableWebSecurity //이 클래스 스프링 시큐리티 설정 가능  (버전 : 5.x.x > 6.x.x 변화가) 
@RequiredArgsConstructor  //롬복
public class SecurityConfig {

	private final DataSource dataSource;
	//@RequiredArgsConstructor  lombok
	//implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	
	//사용자 정의 방식으로 (mybatis 사용을 위해서 )
	@Autowired
	private CustomerDetailService customerDetailService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
				
		http.authorizeHttpRequests(auth -> auth
				                               .requestMatchers("/admin/**").hasRole("ADMIN")
				                               .requestMatchers("/user/**").hasAnyRole("USER","ADMIN") //ROLE_USER , ROLE_ADMIN
				                               .requestMatchers("/css/**" , "/js/**" , "/images/**").permitAll()
				           		               .anyRequest().permitAll())
											   .logout(withDefaults()); //spring 정의한 기본 로그아웃
				           		               //.formLogin(withDefaults()); //로그인 방식 기본값 유지
											   	
				                
		http.logout(logout -> logout
				                    .logoutUrl("/logout")     //로그아웃 요청을 받을 URL
				                    .logoutSuccessUrl("/")    //로그아웃 성공 후 이동할 URL
				                    .deleteCookies("JSESSIONID")  //쿠키 삭제
				                    .invalidateHttpSession(true)); //세션 객체 삭제
		
		http.formLogin(form -> form
				               .loginPage("/login")                //커스텀 로그인 페이지 요청 경로
				               .loginProcessingUrl("/loginPro")    //커스텀 로그인 처리 경로
				               //.defaultSuccessUrl("/")             //로그인 성공시 이동 경로 
				               .usernameParameter("id")            
				               .passwordParameter("pw")
				               .successHandler(authenticationSuccessHandler())                //로그인 성공 ... 축하 메일 ...
				               .permitAll());  //모든 사용자에게 로그인 페이지 접근 허용
		
		http.userDetailsService(customerDetailService); //사용자 정의 인증방식 (mybatis) 사용하는
		http.exceptionHandling(exceptions -> exceptions.accessDeniedHandler(accessDeniedHandler()));
		//http.csrf(csrf -> csrf.disable());  //비활성 사용 .....
		return http.build();

	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomerAccessDeniedHandler();
	}
    
	//로그인 성공시 필요한 부분 처리 
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler(){
			return new LoginSuccessHandler();
		}
	//authenticationManager 구현하기
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration ) throws Exception
	{
			return authenticationConfiguration.getAuthenticationManager();		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
