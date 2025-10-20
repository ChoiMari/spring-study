package DI_Annotation_04_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  //DI.xml 같은 역할 (빈객체 생성과 조립)  순수한 자바 파일로
public class ConfigContext {
	
	//xml <bean id="user" class="....User">
	//자바코드에서는 함수를 통해서 객체를 리턴
	
	@Bean    //컨테이너안에 생성 ....
	public User user() {
		return new User();
	}

	//xml <bean id="user2" class="....User2">
	@Bean    //컨테이너안에 생성 ....
	public User2 user2() {
		return new User2();
	}
}
