package com.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;

/*
 주문서 클래스
 하나의 주문은 여러개의 상품(OrderItem)을 가질 수 있다
 
 board 테이블
 reply 댓글 테이블
 
 하나의 게시글은 여러개의 댓글을 가질 수 있다.
 
 클래스로 표현하면
 Board클래스에서 
 private List<Reply> replyList;
 
하나의 은행은 여러개의 계좌를 가질 수 있다
 */

@Data
public class OrderCommand { //주문(주문 1건은 여러개의 주문 상세(상품)를 가질 수 있다)
	private List<OrderItem> orderItem;

	public List<OrderItem> getOrderItem() {
		return orderItem;
	}


	public void setOrderItem(List<OrderItem> orderItme) {
		this.orderItem = orderItme;
	}
	
	/*
	주문이 발생함 - 2건의 주문이 들어옴
	1. 10개 파손주의
	2. 2개 리모컨 별도 주문
	
	OrderCommand command = new OrderCommand();
	List<OrderItem> itemList = new ArrayList();
	itemList.add(new OrderCommand(1,10,"파손주의")); //1건 넣음
	itemList.add(new OrderCommand(2,3,"리모컨 별도 주문"));
	command.setOrderItme(itemList);
	
	case
	하나의 주문은 여러 개의 상품을 갖는다
	은행은 여러 개의 계좌를 갖는다
	게시글 1개에 여러 개의 댓글을 갖는다
	 */
}
