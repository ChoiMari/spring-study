package DI_09_Spring;

import java.util.Map;

public class ProtocolHandlerFactory {
	public ProtocolHandlerFactory() {
		System.out.println("ProtocolHandlerFactory 기본 생성자 호출");
	}
	
	//POINT
	private Map<String, ProtocolHandler> handlers;  //JAVA API 제공하는 객체 

	public void setHandlers(Map<String, ProtocolHandler> handlers) {
		this.handlers = handlers;
		System.out.println("setHandlers 주입 성공" + this.handlers);
	}
	
	
}
