package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.model.NewArticleCommand;
import com.service.ArticleService;
/*
클라이언트 요청 2가지
1. 화면주세요 (글쓰기, 로그인화면): write.do
2. 처리해주세요 (글쓰기 업무 처리, 로그인 업무 처리): writeok.do

요청주소가 : write.do
요청주소가 : writeok.do
---------------------------------------
Spring 에서는 생각^^
클라이언트 요청 판단
method 단위
GET, POST (form method="post")
form태그에서 method설정

** 요청의 주소가 동일하더라도 (1개의 요청 주소로) > 화면 , 처리 판단할 수 있다
* > 전송방식(GET, POST)으로.
* 요청이 같아도 get방식이면 화면달라
* post방식이면 로직 처리구나 라고 판단
* 같은 요청 url이여도 method방식으로 판단함. 클라이언트의 요청을
http:// ...../newArticle.do
요청방식 
GET 화면 이면 view 
POST이면 서비스 처리 >> DB 연동 >> 데이터 처리 >> view
*/
@Controller //이걸 쓴다는 것은 메서드 단위의 매핑이 가능하다
@RequestMapping("/article/newArticle.do")
public class NewArticleController {
	//뉴아키클 서비스 주소값 주입
	// NewArticleController는 ArticleService를 의존합니다
	// = ArticleService의 주소가 필요합니다
	//DI에서는 setter 또는 생성자 또는 member필드로 주입받아사용
	// 지금은 setter로 주입해봄
	private ArticleService articleService;
	
	@Autowired //IOC 컨테이너 안에 ArticleService타입의 객체가 존재하면 자동 주입된다.
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	
	// 스프링 -> 동기식은 get또는 post방식뿐. 그래서 메서드 방식으로 판단함
	//2가지
	//같은 url 요청이 들어오는데 (/article/newArticle.do)
	//1번쨰는 Get방식으로 요청주소로 들어오는경우
	//post방식으로 들어오는 경우

	@GetMapping //get방식으로 넘어올때 호출되는 메서드
	public String form() { //화면 처리(화면 주세요)
		/*
		 public ModelAndView form(){
		 	ModelAndView mv = new ModelAndView();
		 	mv.setViewName = "hello.jsp";
		 } 
		 
		 스프링의 규칙
		 1. 함수의 리턴타입이 String이면 리턴값으로 앞뒤로
		 뷰리졸브에 프로퍼티스로 설정한 프리픽스, 서픽스 붙여서 뷰의 이름을 찾는다
		 */
		return "article/newArticleForm";
		// 리졸브에서 설정 /WEB-INF/views/article/newArticleForm.jsp
	}
	
	@PostMapping //post방식으로 넘어왔을때 호출되는 메서드
	public String submit(NewArticleCommand command) { //파라미터 dto
		//POST : 로직 처리해주세요(데이터 받아서 처리해주세요)
		//public String submit(HttpServletRequest request) 이건 전통적인 방식(옛날)
		
		System.out.println("POST 처리");
		System.out.println("command : " + command.toString());
		this.articleService.writeArticle(command);
		
		//데이터 담는 작업? 어디로 갔어???(생략되었음)
		//mv.addObject("greeting", getGreeting());// request객체에 저장함 request.setAttribute("greeting", getGreeting())와 같은 코드
		// 이 코드 어디로 갔느냐?
		// 어떻게 뷰에서 데이터를 받나?
		// -> 뭔가 자동으로 된다는 소리(눈에 안보이지만..)
		
		//보통 데이터를 담는 작업을 해주어야함
		// ModelAndView객체 생성해서 addObject()사용해서 데이터 저장해야되는데..
		// 파라미터에 선언한 dto가 근데 
		/*
		 이 4줄의 코드가 추상화 되어서 생략됨
		 NewArticleCommand article = new NewArticleCommand();
		article.setParentId( Integer.parseInt(request.getParameter("parentId")));
		article.setTitle(request.getParameter("title"));
		article.setContent(request.getParameter("content")); 
		 
		 그리고
		 ModelAndView mv = new ModelAndView();
		 mv.addObject("newArticleCommand", article);  //request.setAttribute("newArticleCommand", article)와 같은 코드
		 mv.setViewName("article/newArticleSubmitted");
		 */
		
		//뷰의 이름만 적으면 뷰쪽으로 데이터까지 보낸다(자동으로)
		// 스프링이 알아서 뷰에게 데이터를 보냄
		// 데이터를 뭘로 보내나?
		// 파라미터로 선언한 dto 클래스 이름으로 앞글자를 소문자로 해서 키로 만든다.
		// 값은 setter로 초기화한 dto 객체의 주소값
		// 예) newArticleCommand 이게 키가 됨. 이게 뷰에서 사용이 가능함
		// ${newArticleCommand.title} 이렇게 ${newArticleCommand.으로 사용
		//  mv.addObject("newArticleCommand", article);
		return "article/newArticleSubmitted";
	}
	
	
	/* 전통적 방법 - 아무데서나 적용된다
    호랑이 담배 피던 시절에 했던 코드 .... HttpServletRequest request >> spring 고민 고민 .... 
	@PostMapping  //5.x.x
	public ModelAndView sumbit(HttpServletRequest request) { //처리 
		System.out.println("POST 처리해주세요");
		
		NewArticleCommand article = new NewArticleCommand();
		article.setParentId( Integer.parseInt(request.getParameter("parentId")));
		article.setTitle(request.getParameter("title"));
		article.setContent(request.getParameter("content"));
		
		
		this.articleService.writeArticle(article);
		ModelAndView mv = new ModelAndView();
		mv.addObject("newArticleCommand", article);  //request.setAttribute("newArticleCommand", article)와 같은 코드
		mv.setViewName("article/newArticleSubmitted");
		
		
		return mv;
	

*/
	
	

	/*
		 2. Spring 에서 parameter DTO 객체로 받기(이게 제일 많이 사용함)
		 2.1 자동화 >> 선행조건 >> input 태그의 name값이 DTO 객체의 memberfield 명과 동일
		  
		 @PostMapping  //5.x.x
		 public ModelAndView sumbit(NewArticleCommand command) { //처리 
			System.out.println("POST 처리해주세요");
			//만약 dto객체를 통해서 데이터 자동으로 받고 싶다면?
			  input태그의 name속성값과 dto의 멤버필드명과 같으면 자동화가 된다(자동 주입)
			 
			 NewArticleCommand article = new NewArticleCommand();
			article.setParentId( Integer.parseInt(request.getParameter("parentId")));
			article.setTitle(request.getParameter("title"));
			article.setContent(request.getParameter("content"));
		이 코드 줄이 생략된것 - 이 코드를 자동화
		이 코드를 추상화 시킴(눈에 안보이게 감췄다)
		파라미터의 dto를 자동으로 new Dto() - 객체 생성
		input태그의 name과 dto의 멤버필드명이 같으면 setter로 주입시킴
		컨테이너안에 dto가 bean으로 자동으로 등록됨
			
			//1. 자동 DTO 객체 생성 : NewArticleCommand command = new NewArticleCommand();
			//2. 넘어온 parameter  setter 함수를 통해서 자동 주입  => article.setParentId 자동
			//3. NewArticleCommand command 객체 자동 IOC 컨테이너 안에 등록 : id=newArticleCommand
			//   <bean id="newArticleCommand"  class="....
			
			this.articleService.writeArticle(command);
			ModelAndView mv = new ModelAndView();
			mv.addObject("newArticleCommand", command);  //request.setAttribute("newArticleCommand", article)
			mv.setViewName("article/newArticleSubmitted");
			
			
			return mv;
	*/
	
	/*

	1. 전통적인 방법
	public ModelAndView searchExternal(HttpServletRequest request) {
	   String id= request.getParameter("id")
	} 
	 
	2. DTO 객체를 통한 전달 방법(게시판, 회원가입 데이터) ^^
	 public ModelAndView searchExternal(MemberDto member){
	 public String searchExternal(MemberDto member){
	     
	     return String view 주소 전달
	     return String view (데이터 출력) > 페이지 > forward > memberDto 객체 만들어 > 자도 forward
	     
	     /search/external.do?id=hong&name=김유신&age=100
	     2.1 DTO 있는 member field 이름이 >>
	     private String id;
	     private String name;
	      
	 }
	 
	 약속 : return String .. Model 만들면 데이터 전달
	       return String  .. 화면 UI
	 
	 
	 
	 3. 가장 만만한 방법 - dto로 한꺼번에 안받고 낱개로 
	 //파라미터와 같으면 자동으로 들어감
	  public ModelAndView searchExternal(String id, String name , int age){
	      /search/external.do?id=hong&name=김유신&age=100
	      ** 각각의 parameter 에 자동 매핑
	  }
	  
	 4. @RequestParam  annotation 사용하기
	  4.1 유효성 처리
	  4.2 기본값 처리 
	 
	  
	 5.REST 방식 (비동기 처리) method= GET , POST , PUT , DELETE
	  @PathVariable >>  /member/{memberid} >>  /member/100
	  
	  100 추출해서 parameter  사용
	*/
	
}
