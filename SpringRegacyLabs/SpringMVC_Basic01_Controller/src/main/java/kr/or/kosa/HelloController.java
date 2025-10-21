package kr.or.kosa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;



//Model2 기반의 MVC 패턴


public class HelloController implements Controller { //spring web 전용 자바파일 : Controller 구현

	//handleRequest가 doGET , doPOST > doProcess와 같음
	//doGET , doPOST > doProcess
	//요청이 들어어면 자동으로 실행되는 함수 
	//한글 > 데이터 받기 > 요청 판단 > 요구사항 (DB) > 저장 > 뷰지정 > forward
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("HelloController 요청 실행 : handleRequest 자동 실행");
		
		//요구사항 처리
		//DB연결 조회 처리  > 담기 > 뷰지정 ....
		ModelAndView mav = new ModelAndView(); //데이터 담고 뷰지정 처리 
		mav.addObject("name", "kosauser"); //request.setAttribute()
		mav.setViewName("Hello"); //뷰 지정  ///WEB-INF/views/Hello.jsp
		
		//internalResourceViewResolver
		//  /WEB-INF/views/ + Hello  + .jsp
		//  /WEB-INF/views/Hello.jsp

		return mav; //forward(request,response)
	}

}
