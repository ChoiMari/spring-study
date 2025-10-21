package com.model;

import lombok.Data;

//DB 상품 테이블
// 주문이 걸리면 주문 상세가 만들어지는 경우
@Data 
public class OrderItem {
	 
	private int itemid;
	private int number;
	private String remark;
	
}
