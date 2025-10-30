package com.example.demo.contants;

/*
 그럼 HTTP 프로토콜을 통해서
	headers : {Ahthorization: Bearer ${jwt}}  전달 ....
	
	약속된 이름
	
	1. 열거형(Enum)
	또는
	2. static 상수 -> 지금은 이 방법 사용
 */
public final class SecurityConstants { //상속 불가(final) 클래스
	//헤더 이름
	public static final String TOKEN_HEADER = "Ahthorization";
	
	//토큰 접두사
	public static final String TOKEN_PREFIX = "Bearer ";
	//값에 공백 포함
	//Bearer(소지자, 보유자)라는 뜻. 인증된 사용자라는 키워드
	//Bearer 는
		//단순히 “토큰 인증 방식의 한 종류를 식별하기 위한 키워드”
		//Bearer는 영어로 “소지자(보유자)” 라는 뜻이에요.
		//즉, “이 토큰을 가진 사람(Bearer)이 곧 인증된 사용자” 라는 의미입니다.
	
	//토큰 타입
	public static final String TOKEN_TYPE = "JWT";
}
