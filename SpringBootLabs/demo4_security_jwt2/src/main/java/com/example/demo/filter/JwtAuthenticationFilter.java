package com.example.demo.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.contants.SecurityConstants;
import com.example.demo.prop.JwtProps;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component @RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final UserDetailsService userDetailsService;
	
	private final JwtProps jwtProps; // 시크릿 키 가져오기
	
	private static final List<String> EXCLUDE_URLS = Arrays.asList("/login", "/register");
	/*
    
	    ******************************************************************
	    *JwtAuthenticationFilter Token  발급된 사용자를 사이트 접속 시 .... 
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
	
	private boolean shouldExclude(String requestURI) {
		return EXCLUDE_URLS.stream().anyMatch(requestURI::startsWith);//메서드 참조
		//풀어쓰면 requestURI.startsWith(item) 있으면 true, 없으면 false
		//reauestURI = "/loginForm";
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//리액트
		//axios.Get("localhost:8090/admin")
		//Header : {Authorization : Bearer jwt토큰..}
		
		String anthorizationHeader = request.getHeader("Authorization");
		log.info("anthorizationHeader : {}" , anthorizationHeader);
		
		String requestURI = request.getRequestURI();
		if(shouldExclude(requestURI)) {
			filterChain.doFilter(request, response);
			return;
			// /login, /register 등의 경로면 필터 메서드 종료
		}
		
		//검증
		try {
			//시그니처(서명)
			String secretkey = jwtProps.getSecretKey(); // 서버가 가지고 있는 비밀키
			byte[] signingkey = jwtProps.getSecretKey().getBytes();//실제 토큰에 들어가는 값 byte[]
			String jwt = anthorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
			
			//토큰 해석
			Jws<Claims> parsedToken = Jwts.parser()
					.verifyWith(Keys.hmacShaKeyFor(signingkey))
					.build()
					.parseSignedClaims(jwt);
			
			log.info("doFilterInternal : ***********");
			
			String username = parsedToken.getPayload().get("uid").toString();
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			//사용자 정보 가져오기
			
			//인증 정보 만듬 -> 시큐리티 컨텍스트에 저장
			UsernamePasswordAuthenticationToken autentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(autentication);
		} catch (Exception e) {
			log.error("JWT 검증 실패 : {}", e.getMessage());
		}
		
		filterChain.doFilter(request, response);
		//다음 필터로 전달
	}

}
