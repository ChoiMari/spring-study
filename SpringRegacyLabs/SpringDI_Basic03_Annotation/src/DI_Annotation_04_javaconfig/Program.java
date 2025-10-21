package DI_Annotation_04_javaconfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Program {

	public static void main(String[] args) {
		/*
		 xml 설정 파일인 경우에는 이렇게 사용했음
		  ApplicationContext context = 
				new GenericXmlApplicationContext("classpath:DI_Annotation_02/DI_02.xml"); 
		  
		 */

		//java 파일이 설정 파일인 경우에는 이렇게 사용함
		//ConfigContext.class 객체로 만들어서 조립하겠다?
		ApplicationContext context = new AnnotationConfigApplicationContext(ConfigContext.class);
		//컨테이너 만들고 ... ConfigContext.class 설정을 read 해서 객체 생성 조립
		// main메서드 안에서 ... 컨테이너..
		User user = context.getBean("user",User.class);
		user.userMethod();
		
		User2 user2 = context.getBean("user2",User2.class);
		user2.userMethod2();
	}

}
