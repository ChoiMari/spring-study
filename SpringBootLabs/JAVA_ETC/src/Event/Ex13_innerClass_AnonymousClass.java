package Event;
/*
익명 클래스 
클래스를 정의하지 않고 객체를 만드는 방법 > 1회용 클래스 > 재사용 불가능한 클래스 

이벤트 처리 , 스레드 객체 (runnable) , 람다식 , 스트림 사용

*/
/*
inner class  (ex) awt , swing , 안드로이드 앱
클래스안에 클래스가 있다  
>> 코드를 간소화 할 수 있다 

클래스 안의 클래스가 들어갈 수 있다
바깥 -> outer클래스
안쪽 -> inner클래스

장점 : inner클래스는 outer클래스 자원에 접근이 가능하다.
*/

class OuterClass {
	public int pdata=10;
	private int data=30;
	
	//inner class (자원에 대한 접근을 편하게 )
	//outerclass 의 member field 선언되는 곳에 만들면 되요
	class InnerClass {
		 void msg() {
			 System.out.println("outer class data : " + data);
			 System.out.println("outer class pdata : " + pdata);
		 }
	}
}
///////////////////////////////////////////////////////////////////
abstract class Person{ // 강제적 구현
	abstract void eat();
} //추상클래스는 미완성 클래스여서 객체를 못만듬

class Man extends Person{

	@Override
	void eat() {
		System.out.println("Person 의 eat함수 재정의");
	}
	
}

interface Eatable{
	void eat();
}

//class Eat implements Eatable{
//}

//T.method(new Eat())
class Test{
	void method(Eatable e) { //parameter Eatable 구현한 객체의 주소 .....
		e.eat();
	}
}





public class Ex13_innerClass_AnonymousClass {

	public static void main(String[] args) {
		OuterClass outobj = new OuterClass();
		System.out.println("public field : " + outobj.pdata);
		//public field : 10

		OuterClass.InnerClass innerobj = outobj.new InnerClass();
		
		innerobj.msg(); //outer 클래스에 대한 자원 접근 용이
		
		////////////////////////////////////////////////
		Man m = new Man();
		m.eat();
		Person p = m;
		p.eat();
		
		Person p2 = new Man();
		p2.eat();
		/////////////////////////////////////////////////
		
		
		// abstract class Person 강제 구현 .....
		// 추상 클래스는 객체 생성 불가능 (미완성)
		// 추상클래스를 상속하는 클래스 만들고 사용 .....
		// 생각) 한번만 사용 , 재사용하지 않을 건데 ..... 
		// ex)  class Man extends Person ....
		
		// Person 상속하는 클래스 없이 (이름 없이) 1회성으로 사용가능하게 하고 싶다
		// 익명클래스 만들자
		
		//추상 클래스, 객체 생성하면서 바로 구현
		Person person = new Person() {  //class Man extends  Man이라는 이름이 없다
			@Override
			void eat() {
				System.out.println("익명 객체 타입으로 구현");
				
			}
		};
		person.eat();
	
		
		
		//////////////////////////////////////////////////////////
		
		//TODAY POINT
		Test t = new Test();		
		
		t.method(new Eatable() { // 바로 구현해서 넣어서 호출 -> 익명클래스
			
			@Override
			public void eat() {
				System.out.println("일회성 자원으로 인터페이스를  직접 구현");
				
			}
		}); //Eatable 인터페이스를 구현하고 있는 객체의 주소가 ... 
		    // class ManEat implements Eatable {}
		
	}

}

//추상 클래스와 인터페이스는 class 이름없이 익명 클래스로 바로 구현이 가능하다.
//-> 일회성, 재사용성은 없음