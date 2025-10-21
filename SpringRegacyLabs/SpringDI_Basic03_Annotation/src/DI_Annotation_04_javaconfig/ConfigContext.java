package DI_Annotation_04_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//@Configuration은 객체를 만드는 설정 파일 이라고 컴파일러에게 알려줌
@Configuration  //DI.xml 같은 역할 (빈객체 생성과 조립)  순수한 자바 파일로
public class ConfigContext {
	
	//xml <bean id="user" class="....User">
	//자바코드에서는 함수를 통해서 객체를 리턴
	
	//@Configuration과 @Bean어노테이션은 단짝
	@Bean    //컨테이너안에 생성 ....
	public User user() {
		return new User(); // 메서드에서 User객체를 리턴함
	} //xml의 파일의 bean태그 id속성과 같음

	//xml <bean id="user2" class="....User2">
	@Bean    //컨테이너안에 생성 ....
	public User2 user2() {
		return new User2();
	}
}
