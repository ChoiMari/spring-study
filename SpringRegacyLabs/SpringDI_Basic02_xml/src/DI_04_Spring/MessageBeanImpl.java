package DI_04_Spring;

public class MessageBeanImpl implements MessageBean{

	private String name;
	private String greeting;
	
	//생성자 함수
	public MessageBeanImpl(String name) {
		this.name = name;
	}
	
	//setter 함수
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
	
	
	@Override
	public void sayHello() {
		System.out.println(this.name + " / " + this.greeting);
		
	}

}
