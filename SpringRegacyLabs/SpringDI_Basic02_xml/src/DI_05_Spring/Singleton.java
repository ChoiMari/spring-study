package DI_05_Spring;

public class Singleton {

	private Singleton() {}
	
	private static Singleton instance = new Singleton();
	
	public static Singleton getInstance() {
		return instance; //같은 객체 사용 보장 ...
	}
}
