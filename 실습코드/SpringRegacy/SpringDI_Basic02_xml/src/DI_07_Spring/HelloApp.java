package DI_07_Spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloApp {

	public static void main(String[] args) {
	
		/*
		ProtocolHandler hander = new ProtocolHandler();
		//JAVA API 제공하는 Collection 사용
		List<MyFilter> list = new ArrayList<>();
		//EncFilter encFilter  = new EncFilter(); 했다면 ...
		//<bean id="encFilter"    class="DI_07_Spring.EncFilter"></bean>
		
		list.add(new EncFilter());
		list.add(new ZipFilter());
		list.add(new HeaderFilter());
		
		hander.setFilters(list); //주입 대상 JAVA API 제공하는 클래스
		System.out.println(hander.filter_Length());
		*/
		
		ApplicationContext context = 
				new  GenericXmlApplicationContext("classpath:DI_07_Spring/DI_07.xml");
		
		 ProtocolHandler protocolHandler = context.getBean("protocolHandler",ProtocolHandler.class);
		 System.out.println(protocolHandler.filter_Length());
		
	}

}
