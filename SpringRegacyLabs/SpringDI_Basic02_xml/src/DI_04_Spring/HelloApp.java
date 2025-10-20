package DI_04_Spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloApp {

	public static void main(String[] args) {
		
		//java 코드 필요하면 객체 생성 마음대로 ... 
		
		//MessageBeanImpl messageBeanImpl = new MessageBeanImpl("hong"); //생성자 값 주입(String)
		//messageBeanImpl.setGreeting("Hello"); //setter 함수를 통해서 값 주입(String)  > DI(의존성 주입)
		//messageBeanImpl.sayHello();
		
		
				
		ApplicationContext context = 
				new  GenericXmlApplicationContext("classpath:DI_04_Spring/DI_04.xml");
		MessageBean messageBean = context.getBean("m1",MessageBean.class);
		messageBean.sayHello();
	}

}
