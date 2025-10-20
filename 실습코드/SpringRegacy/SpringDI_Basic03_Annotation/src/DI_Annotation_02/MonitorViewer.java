package DI_Annotation_02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MonitorViewer {
	private Recorder recorder;
	
	public Recorder getRecorder() {
		return this.recorder;
	}
	
	/*
	setter 를 통해서 Recorder타입의 객체 주소 주입
	<property name="recorder">
	  				<ref  bean="recorder" />
	</property>
	
	컨테이너 안에 Recorder 타입의 객체가 존재하면 자동 주입 발생 ... (By type)
	*/
	//@Autowired(required = true) default >> 무조건 injection 하겠다
	//@Autowired(required = false) >> 컨테이너 안에 원하는 타입 객체가 없으면 안 하면 되지
	@Autowired
	@Qualifier("recoder_1") /* <qualifier value="recoder_1"></qualifier>  */
	public void setRecorder(Recorder recorder) {
		this.recorder = recorder;
		System.out.println("setRecorder : " + this.recorder);
	}
	
	//일반 함수
	@Autowired
	@Qualifier("recoder_2")
	public void gMethod(Recorder rec) {
		System.out.println("Gmethod(rec) : " + rec);
	}
	
}
