package com.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.model.OrderCommand;
import com.model.OrderItem;

/*
 요청 주소는 같은데, 1개의 요청 주소로 2가지 업무를 처리
 판단 기준 : 
 	get방식 -> 화면 주세요
 	post방식 -> 로직을 처리해 주세요
 */
@Controller
@RequestMapping("/order/order.do") //매핑url
public class OrderController {
	@GetMapping //화면 보여주는 메서드
	public String form() {
		return "order/OrderForm";
	}
	
	@PostMapping
	public String submit(OrderCommand orderCommand) {
		//OrderCommand orderCommand
		// 내부적으로 어떤 코드가 발생 할까??
		/*

		 생략된 코드
		 OrderCommand클래스 안에
		 private List<OrderItem> orderItme;
		 이게 멤버필드로 있음
		 
		 실질적으로는, OrderCommand가 동작하려면
		 setter로 주입되는 코드가 완성이 되어야 한다.
		 
		 1. OrderCommand의 타입의 객체가 생성됨
		 OrderCommand orderCommand = new OrderCommand();
		 
		 2. 자동 매핑 - 클라이언트가 넘긴 데이터
		 	주입되는 코드 setter를 통해 orderItme이 주입되어야함(자동으로)
		 	member필드가 자동 매핑되어야함
		 	private List<OrderItem> orderItme; 으로 선언한
		 	멤버 필드가 자동 주입되어야 함
		 	
		3. List<OrderItem> itemList = new ArrayList(); 이게 자동으로 만들어짐
		 -> orderItem[0].itemid -> 1
		 orderItem[0].number -> 10
		 orderItem[0].remark -> 파손주의
		 
		 이게 1개의 객체
		 itemList.add(new OrderItem(1,10,"파손주의)));
		 이게 3번 실행됨 - 자동으로(인덱스 0~2)
		 
		 OrderCommand.setOrderItme(itemList)를 통해 최종적으로 완료됨
		 스프링이 자동으로 만들어줌
		 setter로 주입되는 코드가 단건단건 넘어옴
		 
		 뷰로 전달까지 자동으로 해준다.(forward)
		 
		 public String submit(OrderCommand orderCommand)
		 
		 여기 dto 클래스의 이름을 소문자로 바꿔서 키로 잡음
		 orderCommand -> 키
		 값은 dto 클래스를 setter로 자동 초기화 시킨 객체의 주소
		 
		 ModelAndView mv = new ModelAndView();
		 mv.addObject("orderCommand", 주소);와 같음
		 
		 orderCommand는 List<OrderItem> itemList를 가지고 있음
		 그걸로 화면에 출력함
		 
		 근데 필요한 bean객체는 만들어야함 -> 디스패처 서블릿이 사용하는 컨테이너에..
		 자동 bean으로 생성되는거 제외하고..
		 
		 */
		
		
		return "order/OrderCommitted"; // 뷰의 주소
	}

}
