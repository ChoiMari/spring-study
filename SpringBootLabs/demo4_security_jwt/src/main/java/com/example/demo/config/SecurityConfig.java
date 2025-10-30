package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

//객체를 만들수 있는 자바 파일
@Configuration 
@EnableWebSecurity
public class SecurityConfig {
/*
 * 인증과 인가 
 * 1. 인메모리 - 아이디 가져오기, 아이디로 가져와서 검증, 
 * 2. JDBC
 * 3.mybatis(jpa) 사용자 정의 : 유저티테일서비스..
 * 
 */
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		//인증, 권한, 로그인 방식 정의(Spring 정의 또는 별도의 처리)
		// 시큐리티 자원 로그인으로 사용하지 않음 - 폼 로그인 X
		// 시큐리티에서 제공하는 폼 사용하지 않엤다
		//폼태그ㅔ서 id, pw 만들어서 전송 받아서 처리 이렇게 안한다고..
		// 문법은 6대 부터 람다식으로 작성하도록 권장
		//http.formLogin().disable(); // 5대에는 문제 없음(메서드 체인)
		http.formLogin((login) -> login.disable());
		//->람다 DSL 표기봅
		
		//http Spring secutity 제공 자동화 비활성화
		http.httpBasic((basic) -> basic.disable());
		
		//CSRDF -> 태그에 암호화된 문자열 -> 위변조 방지,일단, 비활성화해놓음 
		http.csrf(csrf -> csrf.disable());
		
		//JWT사용하니까 세션을 통한 정보를 관리하지 않겠다
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		 
		return http.build();
		
		
	}
}
