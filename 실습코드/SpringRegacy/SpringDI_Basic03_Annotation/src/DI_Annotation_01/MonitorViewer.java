package DI_Annotation_01;

import org.springframework.beans.factory.annotation.Autowired;

public class MonitorViewer {

	//MonitorViewer 는 recorder 에 의존합니다
	//MonitorViewer recorder객체의 주소가 필요 합니다
	
	private Recorder recorder;

	/*
	 @Autowired 있으면  (By Type)
	 컨테이너안에서 찿아요  Recorder  타입을 가지고 있는 객체를 찿아요
	 있다면 .... 그 객체의 주소를 setRecorder(Recorder recorder)  주입
	 
	 <property name="recorder">
		  			<ref  bean="recorder" />
	 </property> 
	 
	  @Autowired(required = true) >> default >> 무조건 >> injection
	  @Autowired(required = false)>> 있으면 주입 없으면 그냥 ... 삽입하지 않아요  
	 */
	
	@Autowired
	public void setRecorder(Recorder recorder) {
		this.recorder = recorder;
	}
	
	public Recorder getRecorder() {
		return recorder;
	}

	
	
	
}
