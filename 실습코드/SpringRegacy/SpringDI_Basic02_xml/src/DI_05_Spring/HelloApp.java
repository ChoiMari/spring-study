package DI_05_Spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloApp {

	public static void main(String[] args) {
		/*
		MyBean myBean = new MyBean();
		MyBean myBean2 = new MyBean("hong");
		MyBean myBean3 = new MyBean();
		
		System.out.println(myBean + " , " + myBean2 + " , " + myBean3);
		//DI_05_Spring.MyBean@73a28541 ,
		//DI_05_Spring.MyBean@6f75e721 , 
		//DI_05_Spring.MyBean@69222c14
		
		Singleton singleton = Singleton.getInstance();
		Singleton singleton2 = Singleton.getInstance();
		Singleton singleton3 = Singleton.getInstance();
		
		System.out.println(singleton + " , " + singleton2 + " , " + singleton3);
		//DI_05_Spring.Singleton@782830e , 
		//DI_05_Spring.Singleton@782830e , 
		//DI_05_Spring.Singleton@782830e
		*/		
		ApplicationContext context = 
				new  GenericXmlApplicationContext("classpath:DI_05_Spring/DI_05.xml");
		MyBean m = context.getBean("myBean",MyBean.class);
		MyBean m2 = context.getBean("myBean",MyBean.class);
		MyBean m3 = context.getBean("myBean",MyBean.class);
		MyBean m4 = context.getBean("myBean2",MyBean.class);
		System.out.println(m);
		System.out.println(m2);
		System.out.println(m3);
		System.out.println(m4);
		/*
		 
		DI_05_Spring.MyBean@117e949d
		DI_05_Spring.MyBean@117e949d
		DI_05_Spring.MyBean@117e949d 
		*/
		
		Singleton single = context.getBean("single",Singleton.class);
		Singleton single2 = context.getBean("single",Singleton.class);
		System.out.println(single + " , " + single2);
		//DI_05_Spring.Singleton@d21a74c , DI_05_Spring.Singleton@d21a74c
		
		/*
		 getBean()
		 1. 컨테이너 안에 있는 객체를 리턴( new 아니예요)
		 2. return type Object (타입에 맞는 casting)
		 3. * 스프링 컨테이너 안에 있는 객체들은 default singleton
		 4. * 예외적으로 getBean() .. new 라는 용도로 사요가능(거의 사용하지 않는다) 
		  
		 
		 */
	}

}
