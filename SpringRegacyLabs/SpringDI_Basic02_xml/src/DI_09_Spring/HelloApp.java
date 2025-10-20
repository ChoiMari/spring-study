package DI_09_Spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloApp {

	public static void main(String[] args) {
	
		/*
		ProtocolHandlerFactory factory = new ProtocolHandlerFactory();
		
		Map<String, ProtocolHandler> handler = new HashMap<String, ProtocolHandler>();
		handler.put("rss", new RssHandler());
		handler.put("rest", new RestHandler());
				
		factory.setHandlers(handler); //Map 객체의 주소 주입
		이 코드를 스프링으로 가면 DI_09.xml과 같음
		스프링은 개발자가 new하지 않음
		스프링 레거시는 xml파일에 bean으로 등록함
		*/
		
		
		ApplicationContext context = 
				new  GenericXmlApplicationContext("classpath:DI_09_Spring/DI_09.xml");
		
		 
		
	}

}
