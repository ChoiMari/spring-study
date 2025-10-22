package com.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CookieController {
	/*
	 <h3>TEST_5 Spring Cookie 사용하기</h3>
     <a href="cookie/make.do">make.do 쿠키 생성하기</a><br>
     <a href="cookie/view.do">view.do 쿠키 보기</a><br>
	 */
	@RequestMapping("/cookie/make.do")
	public String make(HttpServletRequest request, 
			HttpServletResponse response) {
		response.addCookie(new Cookie("SpringAuth", "1004"));
		//쿠키의 key("SptingAuth"), value설정("1004")
		// 클라이언트 브라우저에 write
		return "cookie/CookieMake"; //view 페이지 보여주기
	}
	
	//클라이언트 쿠키 가져오기 -> 전통적인 방법
	//public String view(HttpServletRequest request) {
	//	
	//}
	
	//스프링이 만듬 - 어노테이션 - 리퀘스트파람처럼 설계함
	@RequestMapping("/cookie/view.do")
	public String view(@CookieValue(value = "SpringAuth" , 
			defaultValue = "1007") String auth) {
		System.out.println("클라이언트 브라우저에서 read한 쿠키값 : " + auth);
		//클라이언트 브라우저에서 read한 쿠키값 : 1004
		return "cookie/CookieView";
	}
}
