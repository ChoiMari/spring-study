package DI_01;

public class HelloApp {

	public static void main(String[] args) {
		MessageBean messageBean = new MessageBean();
		messageBean.sayHello("hong");

	}

}
/*
요구사항
1. 한글버전 (hong) > 안녕 hong
2. 영문버전 (hong) > hello hong

MessageBean_kr
MessageBean_en

interface  기반 (다형성) 

*/