package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

	/*
	 1. 전통적인 방법(HttpServletRequest, HttpServletResponse)
	 2. DTO : 주로 insert, update 할 때
	 3. parameter : 쿼리스트링 url list.do?id=7888 -> search(String id)
	 	    -> select 또는 delete
	 4. @RequestParam : default값 설정 가능
	 5. REST 방식 (비동기 처리) method= GET , POST , PUT , DELETE
	  @PathVariable >>  /member/{memberid} >>  /member/100
	  
	  100 추출해서 parameter  사용
	  
	  @PathVariable은 url경로 값을 뽑아냄
	 */
	
	/*
	 요청 url
	   <h3>TEST_4 Spring parameter 다루기</h3>
	   <a href="search/external.do">external.do</a><br>
	   <a href="search/external.do?p">external.do</a><br>
	   <a href="search/external.do?query=world">external.do</a><br>
	   <a href="search/external.do?p=555">external.do</a><br>
	 
	 간단 버전 - 근데 이건 기본값 설정은 못함.
	 public ModelAndView searchExternal(String query, int p){}
	 */
	@RequestMapping("/search/external.do")
	public ModelAndView searchExternal(
		@RequestParam(value="query", 
		defaultValue = "kosa") String query,
		@RequestParam(value="p", 
		defaultValue = "10") int p) {
		//타입은 자동 변환됨. 쿼리스트링 기본값 설정 가능
		
		System.out.println("param query : " + query);
		System.out.println("param p : " + p);
		
		
		return new ModelAndView("search/external"); //생성자가 1개 들어가면 뷰의 이름이 됨 
		// /WEB-INF/views/search/external.jsp
		
	}
}
