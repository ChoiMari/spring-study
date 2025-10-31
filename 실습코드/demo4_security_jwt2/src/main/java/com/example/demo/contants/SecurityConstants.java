package com.example.demo.contants;

//그럼 HTTP 프로토콜을 통해서
//headers : {Ahthorization: Bearer ${jwt}}  전달 ....

//1. enum 생성
//2. static 상수 생성
public final  class SecurityConstants {

	//헤더이름
	public static final String TOKEN_HEADER="Ahthorization";
	
	
	//토큰 접두사
	public static final String TOKEN_PREFIX="Bearer ";
	//Bearer 는
	//단순히 “토큰 인증 방식의 한 종류를 식별하기 위한 키워드”
	//Bearer는 영어로 “소지자(보유자)” 라는 뜻이에요.
	//즉, “이 토큰을 가진 사람(Bearer)이 곧 인증된 사용자” 라는 의미입니다.
	
	
	//토큰 타입
	public static final String TOKEN_TYPE="JWT";
}
