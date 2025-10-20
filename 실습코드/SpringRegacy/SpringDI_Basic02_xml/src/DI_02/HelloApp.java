package DI_02;

public class HelloApp {

	public static void main(String[] args) {
		
		/*
		MessageBean_en en = new MessageBean_en();
		en.sayHello("hong");
		
		MessageBean_kr kr = new MessageBean_kr();
		kr.sayHello("hong");
		*/
		
		//인터페이스 (다형성) 부모타입 > 느슨한 구조 
		MessageBean messageBean = new MessageBean_kr();
		messageBean.sayHello("hong");
		
		//스프링 원하는 설계 방식 (인터페이스를 통한 다형성 : 느슨한 구조)
	}

}
