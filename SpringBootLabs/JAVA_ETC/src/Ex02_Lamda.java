//람다(Lambda Expression)
/*
  람다 표현식
  람다식은 함수를 하나의 식으로 표현한 것
  
  자바에서 람다식 사용하기 위해서는 함수형 인터페이스를 사용해야 한다.
  
  자바에서는 함수 하나만 단독 사용 불가.
  자바에서는 클래스 안에서 함수를 정의해야 함(그게 메서드)
  비효율적..
  -> 그래서 자바에서 함수형 프로그래밍을 만듬
  
  근데 자바가 객체지향 언어라서..
  인터페이스를 사용함
  함수형 인터페이스
  인터페이스가 **단 1개의 추상메서드만** 가지게 한다.
  
  interface MyFunc{
  	int max(int a, int b);
  }
  
  익명 클래스
  익명 객체 선언을 통해서,
  MyFunc f = new MyFuc(){ 
  	int max(int a, int b){ return a > b ? a : b;}
  }
  
  int value = f.max(3.5);
  
  원칙은,
  
  class Func implements MyFunc{ public int max(){...} }
  
  람다식의 장점
  	코드 간결
  	병렬 프로그래밍이 가능하다
  	
  단점
  람다를 사용하면, 익명 함수는 재사용이 불가
  디버깅이 어렵다
  재귀 구현이 어렵다
  
  <정리>
  자바에서 자바스크립트처럼 함수를 사용하기 위한 방법
  함수적 인터페이스를 사용한다
  @FunctionalInterface -> 이거 붙으면 검사함
  인터페이스 안에서 1개의 추상메서드만 선언 가능
  
  @FunctionalInterface //-> 이게 붙으면 검사
 */

import java.util.List;
import java.util.ArrayList;

interface MyLamdaFunction{
	int max(int a, int b);
}

public class Ex02_Lamda {
	public static void main(String[] args) {
		System.out.println(new MyLamdaFunction() {
			
			// 인터페이스를 객체 생성과 동시에 구현해버림
			@Override
			public int max(int a, int b) {
				return a > b ? a: b;
			}
			
		
		}.max(3,5));
		// 인터페이스주소.으로 접근해서 인터페이스 안의 함수 호출
		// 그럼 오버라이드 한 게 호출
		
		
		// 람다식으로 더 간단하게 표현 가능
		MyLamdaFunction lamdaFuc = (int a, int b) -> a > b ? a : b;
		// 익명함수를 변수에 담음
		System.out.println(lamdaFuc.max(3, 5));
		
		List<String> list = new ArrayList<>();
		list.add("java");
		list.add("c");
		list.add("react");
		
		for(String str : list) {
			System.out.println(str);
		}
		
		System.out.println();
		
		list.stream().forEach((str) -> 
			System.out.println(str)
		);
		
		list.stream().forEach(System.out::println);		
		//메서드 참조(::) 이미 있는 메서드를 간단히 전달 할 때 사용함
	}
}
