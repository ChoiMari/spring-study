@FunctionalInterface
interface Myfun{
	void method(int x); // 구현을 람다식으로..
}

@FunctionalInterface
interface Myfun2{
	int method(int x, int y); 
}

public class Ex04_Lamda {
	Myfun my = new Myfun() {
		
		@Override
		public void method(int x) {
			System.out.println("param x : " + x);
			
		}
	};
	
	Myfun my2 = (x) -> System.out.println("param x : " + x);
	
	Myfun2 myfun3 = (x, y) -> x + y;
	
	
}
