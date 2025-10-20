package DI_03_Spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloApp {

	public static void main(String[] args) {
		
		/*
		MessageBean_en en = new MessageBean_en();
		en.sayHello("hong");
		
		MessageBean_kr kr = new MessageBean_kr();
		kr.sayHello("hong");
		*/
		
		//인터페이스 (다형성) 부모타입 > 느슨한 구조 
		
		//MessageBean messageBean = new MessageBean_kr();
		//messageBean.sayHello("hong");
		
		//스프링 원하는 설계 방식 (인터페이스를 통한 다형성 : 느슨한 구조)
		
		//1. Spring 컨테이너 만들기
		//2. 컨테이너 안에 생성할 객체정보를 가지고 있는 xml 만들기
		//3. 필용한 객체 얻어내기
		
		ApplicationContext context = 
				new  GenericXmlApplicationContext("classpath:DI_03_Spring/DI_03.xml");
		
		//interface 사용(다형성) .... 좋은 코드
		MessageBean messageBean = context.getBean("messageBean",MessageBean.class);
		messageBean.sayHello("hong");
	}

}
