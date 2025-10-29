package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dto.CustomerUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 로그인이 성공하면, 필요한 업무
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("로그인 인증 성공");

		// User user = (User)authentication.getPrincipal();
		CustomerUser user = (CustomerUser) authentication.getPrincipal();

		log.info("아이디 :" + user.getUsername());
		log.info("패스워드 :" + user.getPassword());
		log.info("권한 :" + user.getAuthorities());

		// 로그인 성공 후 직접 이동 경로 지정한 경우
		// formLogin().defaultSuccessUrl(...) 설정은 무시됩니다
		// 로그인 성공 시 동작은 전적으로 LoginSuccessHandler에서 제어합니다
		// .defaultSuccessUrl("/") // 로그인 성공 시 이동할 경로는 의미가 없습니다

		response.sendRedirect("/admin"); // 여러분 원하시는 데로 ...

		/*
		 * LoginSuccessHandler 언제 쓰나요? 
		 * 사용자 로그인 시 로그 기록 저장 
		 * 사용자 권한에 따라 리다이렉트 경로 분기 
		 * 최초 로그인/이후 로그인 구분 
		 * 사용자별 맞춤 리다이렉션
		 *  if (hasRole(authentication, "ROLE_ADMIN")) {
		 * response.sendRedirect("/admin"); } else {
		 * response.sendRedirect("/user/home"); }
		 * 
		 * 
		 */

	}

}
