package Spring_DI4;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Program {

	public static void main(String[] args) {
		/*
			NewRecordView view = new NewRecordView();
			//놀다가 ....
			//나는 니가 필요해 (setter  함수를 통해서 주소를 주입)
			
			NewRecord record = new NewRecord(100, 100, 100); //Record 라는 인터페이스 구현 ...
			view.setRecord(record);//Record 구현하는 객체 (다형성)
			view.print();
		*/
		    //의존객체를 주입 받는다 >> Spring 이작업을 자동화 (DI)
		
		/*
		  스프링은 자신만의 메모리 공간을 가져요 ....
		 1. SpringFrameWork 컨테이너를 제공 (메모리 생성 : 컨테이너 (IOC 컨테이너 : 제어의 역전)) 
		    > 지금까지는 내가 필요한 클래스 만들고 .. 사용은 개발자가 편의에 따라서 new Emp() , new Emp(),
		    
		    ApplicationContext context = new ClassPathXmlApplicationContext("DIConfig.xml");
		    ApplicationContext 메모리 공간을 만드는데  공간을 만들고 DIConfig.xml 컴파일 하고 read 그리고 설정
		 
		 2. 컨테이너 만들고 그 메모리에 필요한 [객체를 생성]하고 조립(주입:DI) .... ... 나중에 자동 소멸
		    >>  컨테이너 생성되고 DIConfig.xml read .... 
		    >>  DIConfig.xml 객체 생성과 조립 xml 코드가 있어요 ....
		    >>  생성된 객체를 bean 이라 합니다 
		  
		  
		  
		 */
		
		ApplicationContext context = new ClassPathXmlApplicationContext("DIConfig.xml");
		//Spring 필용한 메모리 공간을 생성하고 메모리에 객체를 생성하고 주입하는 설명파일(DIConfig.xml)제공 
		//컨테이너 자동 생성되고 DIConfig.xml read 객체 생성하고 주입하는 코드 실행
		//컨테이너 안에 객체가 있어요 > 가지고 와서 사용 > 반납 
		
		RecordView view =  (RecordView)context.getBean("view");
		//컨테이너 안에서 필용한 객체가 있다면 가지고 오면 되요 (getBean 함수를 통해서)
		view.input();
		view.print();
		
	}

}
