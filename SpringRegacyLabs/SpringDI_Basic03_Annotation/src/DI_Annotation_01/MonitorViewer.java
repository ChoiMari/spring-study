package DI_Annotation_01;

import org.springframework.beans.factory.annotation.Autowired;

public class MonitorViewer {

	//MonitorViewer 는 recorder 에 의존합니다
	//MonitorViewer recorder객체의 주소가 필요 합니다
	//생성자나 setter로 주입받음
	/*
	 * <bean id="recorder" class="DI_Annotation_01.Recorder"></bean> <bean
	 * id="monitorViewer" class="DI_Annotation_01.MonitorViewer"> <property
	 * name="recorder"> <ref bean="recorder" /> </property> </bean>
	 근데 이런건 어노테이션으로 대체 가능 @Autowired
	 @Autowired가 붙어 있으면 메모리에 올리다가
	 주입되는 객체의 타입을 찾는다. 어디서? 스프링 컨테이너 안에서
	 
	 */
	
	private Recorder recorder;

	/*
	 @Autowired 있으면  (By Type)
	 컨테이너안에서 찿아요  Recorder  타입을 가지고 있는 객체를 찿아요
	 있다면 .... 그 객체의 주소를 setRecorder(Recorder recorder)  주입
	 
	 <property name="recorder">
		  			<ref  bean="recorder" />
	 </property> 
	 
	  @Autowired(required = true) >> default >> 무조건 >> injection
	  @Autowired(required = false)>> 있으면 주입 없으면 그냥 ... 삽입하지 않아요(이런 코드는 안쓴다) 
	 */
	
	//@Autowired 주입하려는 객체를 스프링컨테이너 안에서 찾아서 자동으로 넣어달라는 뜻
	// 그래서 스프링 컨테이너 안에 빈 등록해놓아야함
	// <bean id="recorder"  class="DI_Annotation_01.Recorder"></bean>
	// 필요한 객체가 있다면 xml파일에서 property태그 안쓰고 @Autowired로 주입받음
	// 만약 찾다가 스프링 컨테이너에서 못찾으면 예외 터진다고 함.
	// 레코드 타입이 여러개이면 뭘 찾을까? 이것도 예외가 터진다
	// 이건 타입 기반으로 찾는거라서... 그래서 태그안에 우선순위 지정하는거나
	// id로 찾게끔 설정함
	@Autowired
	public void setRecorder(Recorder recorder) {
		this.recorder = recorder;
	}
	
	public Recorder getRecorder() {
		return recorder;
	}

	
	
	
}
