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

import lombok.RequiredArgsConstructor;

//객체를 만들수 있는 자바 파일
@Configuration 
@EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig {
/*
 * 인증과 인가 
 * 1. 인메모리 - 아이디 가져오기, 아이디로 가져와서 검증, 
 * 2. JDBC
 * 3.mybatis(jpa) 사용자 정의 : 유저티테일서비스..
 * 
 */
	// CORS 설정 주입
	private final CorsConfigurationSource corsConfigurationSource;

    //JWT 필터 주입
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		//인증, 권한, 로그인 방식 정의(Spring 정의 또는 별도의 처리)
		// 시큐리티 자원 로그인으로 사용하지 않음 - 폼 로그인 X
		// 시큐리티에서 제공하는 폼 사용하지 않엤다
		//폼태그ㅔ서 id, pw 만들어서 전송 받아서 처리 이렇게 안한다고..
		// 문법은 6대 부터 람다식으로 작성하도록 권장
		//http.formLogin().disable(); // 5대에는 문제 없음(메서드 체인)
		/*
		http.formLogin((login) -> login.disable());
		//->람다 DSL 표기봅
		
		//http Spring secutity 제공 자동화 비활성화
		http.httpBasic((basic) -> basic.disable());
		
		//CSRDF -> 태그에 암호화된 문자열 -> 위변조 방지,일단, 비활성화해놓음 
		http.csrf(csrf -> csrf.disable());
		
		//JWT사용하니까 세션을 통한 정보를 관리하지 않겠다
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		*/
		/* jwt와 시큐리티와 연동
		  hasRole("USER") → "USER 권한이 있는 사용자만 허용"
          hasRole("ADMIN") → "ADMIN 권한이 있는 사용자만 허용"

        
		  
		  ROLE_ADMIN 사용자는 /user/**에 접근 금지
		  
		  .authorizeHttpRequests(auth -> auth
        .requestMatchers("/user/**").access("hasRole('USER') and !hasRole('ADMIN')")
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().permitAll()
         )

		 */
		
		/* 권한 검사 필터 필요함
			이 설정은 **"JWT 인증 필터를 UsernamePasswordAuthenticationFilter보다 앞서 실행하겠다"**는 의미입니다.
          이때 jwtAuthenticationFilter()는 보통 HTTP 요청마다 실행되는 필터로 동작합니다.
		
		
		    UsernamePasswordAuthenticationFilter  ->	로그인(POST /loginPro 등) 시에만 실행되는 필터
		    JwtAuthenticationFilter -> 모든 요청마다 실행되어, JWT가 있는지 확인하고 인증 처리
		    
		     [클라이언트 요청]
            → FilterChain 시작
             → JwtAuthenticationFilter  (회원가입 되어 있고 ... 로그인 요청 헤더의 토큰 .... JWT 가지고 와요)
               → 요청 헤더에서 Authorization 확인
                 → Bearer {token}
                   → 토큰 유효성 검사
                     → 인증 정보 생성 후 SecurityContextHolder 저장
                       → UsernamePasswordAuthenticationFilter (필요 시 로그인 처리)
                         → DispatcherServlet → Controller 실행
		
		*/
		
		http.cors(cors -> cors.configurationSource(corsConfigurationSource))
		.csrf(csrf -> csrf.disable())
		.formLogin((login) -> login.disable())
		.httpBasic((basic) -> basic.disable())
		.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/register").permitAll()
				.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated())
				//.anyRequest().permitAll())
		//.addFilterBefore(null, null);
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
		
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 알고리즘 사용
        return new BCryptPasswordEncoder();
    }
    
  //mybatis 연동 loadUserByUsername 구현
//  	@Bean
//  	public UserDetailsService userDetailsService() {
//  		return new CustomerUserDetailsService();
//  	}
    
//  	//filter 사용한 인증 
//  	@Bean
//  	public JwtAuthenticationFilter jwtAuthenticationFilter() {
//  		return new JwtAuthenticationFilter();
//  	}
    
}
