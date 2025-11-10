package Thread;

/*
 스레드, 네트워크, 람다.., 객체지향, 상속
 동시성, 락 
 
 Thread : 프로세스에서 하나의 최소 실행 단위(흐름)
 -> method가 실행되는 공간? -> stack
 
 멀티 스레드 : stack을 여러개 생성해서 동시에 실행(가능한 상태로 만듬)
 -> 런어블 상태로 만든다(runable) CPU점유가 준비가 된 상태
 CPU를 점유할 수 있는 상태로 만든다.
 
 스레드 생성 방법
 1. 스레드를 상속하면 된다(스레드 클래스가 별도로 있음)
 	-> class Task extends Thread
 	Thread의 run 메서드를 오버라이드(run메서드는 main()메서드와 비슷)
 2. Runnable 인터페이스를 구현
 	-> class Task implements Runnable
 	이걸 구현한다고 해서 스레드는 아님.. 그냥 run이라고 하는 메서드를
 	강제로 구현하게 할 뿐..
 	스레드를 흉내..
 	스레드가 아니기 때문에 이걸 구현하면 Thread객체를 직접 만들어야한다
 	Thread thead = new Tread(new Task());
 	생성자 호출에 Runnable를 구현한 객체의 주소를 아규먼트로 넣어줌
 	
 	Runnable 인터페이스를 구현을 쓰는 이유?
 	객체 지향의 단점 : 다중 상속을 못함
 	class Task extends Car 이렇게 다른 클래스를 상속하고 있으면 못쓰니까..
 	인터페이스를 통해서 run()메서드를 강제 구현하고 그 객체의 주소를 Tread가 받음
 	
 */

class Task_1 extends Thread{
	//run()을 구현하지 않아도 에러가 안뜸..
	//run()은 추상 메서드가 아니라서 그렇다..
	// 왜 run()메서드는 추상 메서드가 아닌걸까?
	// 추상 메서드라면 new를 못하기 때문에..
	// Thread thead = new Tread(new Task()); 여기처럼 new를 못하니까..
	// 그래서 일부러 일반 함수로 만들었다.
	// 근데 무조건 오버라이드를 해야한다 extends Thread 한다면..
	@Override
	public void run() { //run() 메서드는 스레드의 main()메서드 역할
		// 새로 만든 stack에 올라가는 맨 처음 올라가는 메서드
		for(int i  = 0;i < 1000; i++) {
			System.out.println("Task_1 " + i + this.getName());
		}
		System.out.println("Task_1 run() 메서드 END");
	}
}

class Task_2 implements Runnable{
	//Runnable는 스레드가 아니다.. run()메서드를 가지고 있는 클래스..
	//여기서의 run()은 강제함(추상메서드)

	@Override
	public void run() {
		for(int i  = 0;i < 1000; i++) {
			System.out.println("Task_2 " + i);
		}
		System.out.println("Task_2 run() 메서드 END");
	}
	
}



public class Ex02_Multi_Thread {
	public static void main(String[] args) {
		//main 스레드
		Task_1 th = new Task_1();
		
		th.start(); //-> (중요)
		//start()가 실행 되면 벌어지는 일
		// 1. 메모리에 stack이 만들어진다
		// 	call stack 생성
		// 2. 스레드가 가지고 있는 run()메서드를 stack에 올려놓고
		//	수명 끝
		
		Task_2 ta = new Task_2(); // runnable구현한 객체
		Thread th2 = new Thread(ta); //-> 이 부분 익명 객체로 만들 수 있음
		th2.start();
		
		
		Thread th3 = new Thread(new Runnable() {
			// 여기서 더 발전한 것이 함수형 인터페이스 , 람다 표현식
			
			@Override
			public void run() {
				for(int i  = 0;i < 1000; i++) {
					System.out.println("th3 " + i);
				}
				System.out.println("th3 run() 메서드 END");
			}
		});
		
		for(int i  = 0;i < 1000; i++) {
			System.out.println("main " + i);
		}
		System.out.println("main() 메서드 END");
	}
}
