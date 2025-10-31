package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.demo.filter.JwtAuthenticationFilter;
import com.example.demo.service.CustomerUserDetailService;

@Configuration   //객체 생성 ... 주입 자바파일 @Bean
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private CorsConfigurationSource corsConfigurationSource;
	/*
	 인증과인가(권한)
	 1. in memory
	 2. jdbc (2가지 쿼리 : id,pwd,enabled  :  auth ) 
	 3. mybatis(jpa) : 사용자 정의 UserDetailService ...  
	*/
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http
		.cors(cors -> cors.configurationSource(corsConfigurationSource))
		.csrf((csrf) -> csrf.disable())
		.formLogin((login) -> login.disable())
		.httpBasic((basic) -> basic.disable())
		.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth -> auth
				                          .requestMatchers("/user/**").hasRole("USER")
				                          .requestMatchers("/admin/**").hasRole("ADMIN")
				                          .anyRequest().permitAll())
		.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		/*
		
		hasRole("USER") → "USER 권한이 있는 사용자만 허용"
        hasRole("ADMIN") → "ADMIN 권한이 있는 사용자만 허용"

 	    ROLE_ADMIN 사용자는 /user/**에 접근 금지
		  
		.authorizeHttpRequests(auth -> auth
        .requestMatchers("/user/**").access("hasRole('USER') and !hasRole('ADMIN')")
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().permitAll()
         )

		 */
		
		/*
		이 설정은 **"JWT 인증 필터를 UsernamePasswordAuthenticationFilter보다 앞서 실행하겠다"**는 의미입니다.
        이때 jwtAuthenticationFilter()는 보통 HTTP 요청마다 실행되는 필터로 동작합니다.
		
		UsernamePasswordAuthenticationFilter  >>	로그인(POST /loginPro 등) 시에만 실행되는 필터
		              JwtAuthenticationFilter >> 모든 요청마다 실행되어, JWT가 있는지 확인하고 인증 처리
		    
		     [클라이언트 요청]
            → FilterChain 시작
             → JwtAuthenticationFilter  (회원가입 되어 있고 ... 로그인(header token) .... JWT 가지고 와요)
               → 요청 헤더에서 Authorization 확인
                 → Bearer {token}
                   → 토큰 유효성 검사
                     → 인증 정보 생성 후 SecurityContextHolder 저장
                       → UsernamePasswordAuthenticationFilter (필요 시 로그인 처리)
                         → DispatcherServlet → Controller 실행
		
		*/
		
		
		return http.build();
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
		
		//mybatis 연동 loadUserByUsername 구현
		@Bean
		public UserDetailsService userDetailsService() {
			return new CustomerUserDetailService();
		}
		//filter 사용한 인증 
		@Bean
		public JwtAuthenticationFilter jwtAuthenticationFilter() {
			return new  JwtAuthenticationFilter();
		}
	
}
