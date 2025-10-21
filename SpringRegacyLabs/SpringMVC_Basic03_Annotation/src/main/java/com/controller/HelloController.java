package com.controller;

import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
// 기존에는 implements Controller 했음
	// 단점 : 이렇게 하면 컨트롤러 1개당 1개의 요청만 처리 가능
// 근데 게시판 하나만 만들어도 요청이 여러개..
	// 1요청마다 1컨트롤러를 만드는건 무식..
	// 클래스 단위 매핑이 아니라 메서드 단위로 매핑
	// 1개의 컨트롤러 클래스가 여러개의 요청을 처리
	// @Controller 어노테이션을 붙이면 그 안에서
	// 메서드 단위의 매핑을 할 수 있게 해주겠다
	
	//기본에는 디스패처name-sevelt.xml파일에서
	// bean태그로 객체 생성하고 컨트롤러 매핑
	// 이건 불편
	
	//@Controller를 붙인 클래스는 여러개의 요청 주소를
	// 메서드로 매핑이 가능하다
	
	// 생성자
	public HelloController() {
		System.out.println("HelloController 생성됨");
	}
	
	//요청
	// <a href="hello.do">hello.do 요청하기</a>에 매핑
	@RequestMapping("/hello.do") //매핑 url
	// 기본엔 bean태그 id
	public ModelAndView hello() {
		System.out.println("hello.do 요청에 대해서 method call 호출");
		ModelAndView mv = new ModelAndView();
		mv.addObject("greeting", getGreeting());// request객체에 저장함
		mv.setViewName("Hello");// 뷰이름 설정 
		// 뷰리졸브에 프리픽스, 서픽스 설정 되어있어야함
		//spring-servlet.xml 파일에서 작성(디스패처 컨테이너 설정파일)
		
		
		return mv;
	}
	
	private String getGreeting() { //내부에서 사용하는 메서드
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		String data = "";
		if(hour >= 6 && hour <= 10) {
			data = "학습시간";
		}else if(hour >= 11 && hour <= 13){
			data = "배고픈 시간";
		}else if(hour >=  14 && hour <= 18) {
			data = "졸리운 시간 ~_~";
		}else {
			data = "집에 갑니다.";
		}
		
		return data;
		
	}

/*
 흐름 서버시작 -> web.xml을 읽음
 컨테이너 생성
 디스패처서블릿 컨테이너 설정파일 안의 bean태그에 작성된 객체 생성됨
 @Controller붙였다고 해서 bean이 만들어지지 않아서
 bean으로 등록해야함( 컨테이너 설정파일 spring-servlet.xml 안에)

 * */
}
