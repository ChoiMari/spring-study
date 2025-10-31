package com.example.demo.filter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.demo.contants.SecurityConstants;
import com.example.demo.prop.JwtProps;
import lombok.extern.slf4j.Slf4j;
	
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	/*
	    
	    ******************************************************************
	    *JwtAuthenticationFilter Token  발급된 사용자를 사이트 접속시 .... 
	    *비밀번호 확인 필요없어요 ... Token 자체 시그니쳐 확인 되면 통과 
	    *인증되어서 토큰을 가지고 있고 ... 내 사이트 특정 주소를 요청을 보내면 ... 검증 
	    ******************************************************************
	    
	    
		1. 이 필터는 OncePerRequestFilter를 상속하므로 HTTP 요청마다 한 번만 실행.
	    2. Authorization: Bearer <JWT> 형식의 헤더에서 JWT를 추출하고 검증.
	    3. 유효한 JWT가 있는 경우 → 사용자 정보 조회 + 인증 처리
	    4. 예외 URL (/login , /register) 필터 적용 제외
	    
	    
	    처리)
	    1. UserDetailsService 서비스 사용
	*/
	
	@Autowired
	private UserDetailsService userDetailsService; //CustomerUserDetailsService 객체 주입
	
	
	@Autowired
	private JwtProps jwtProps; //시그니쳐 (비밀키) 검증
	
	private static final List<String> EXCLUDE_URLS = Arrays.asList("/login" , "/register");
	
	
	//EXCLUDE_URLS 검증하는 함수
	private boolean shouldExclude(String requestURI) {
		return EXCLUDE_URLS.stream().anyMatch(requestURI::startsWith); //메서드 참조
	}
	
	//requestURI.startWith(item) >>  true , false
	//requestURI = "/loginForm"
	//"/loginForm".startWith("/login") -> true
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//Axios.Get("localhost:8090/admin")
		//Header : {Ahthorization : Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9........}
		
		//가장 앞단에 .... 근본적인 ..HttpServletRequest request, HttpServletResponse response
		
		String authorizationHeader = request.getHeader("Authorization");
		System.out.println("authorizationHeader ..." + authorizationHeader);
		//Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9........
		
		String requestURI = request.getRequestURI();
		if(shouldExclude(requestURI)) {
			filterChain.doFilter(request, response);
			return;
			// /login , /register 등의 경로는 필터를 적용하지 않고 통과 
		}
		
		try {
				//1. 서명 (시그니쳐)
				String secretkey = jwtProps.getSecretkey();
				//서명이 사용될 때는 (토큰 사용시 byte[] 변환)
				byte[] signingkey =  jwtProps.getSecretkey().getBytes(); //실제 토큰에 들어가는 값
				
				
				String jwt  =authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX,"");
				//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9........  
				
				
				//토큰 해석
				Jws<Claims> parsedToken = Jwts.parser()
						                  .verifyWith(Keys.hmacShaKeyFor(signingkey))
						                  .build()
						                  .parseSignedClaims(jwt);
				
				log.info("doFilterInternal :  + ***************************");
				log.info("parsedToken : " + parsedToken);
				
				String username = parsedToken.getPayload().get("uid").toString();
				log.info("username : " + username);
				
				//실제 사용자인지 검증 
				//in-memory
				//JDBC
				//사용자 정의 (mybatis) 선택 ....
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				//사용자 정보 가지고 오기
				
				//인증정보 만들어서
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				
				//정보 담아서 사용
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
		} catch (Exception e) {
			log.error("JWT 검증 실패 " + e.getMessage());
		}
		
		filterChain.doFilter(request, response); //다음 필터로 전달 
	}


}
