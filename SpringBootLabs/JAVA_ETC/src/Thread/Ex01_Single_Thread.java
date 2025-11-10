package Thread;
/*
 프로그램이 있고 그걸 실행함
 현재 실행중인 프로그램 -> 프로세스
 최소 하나의 Thread를 가지고 있음, 최소 1개의 stack 메모리를 갖는다
 JVM이 OS한테 빌려서 목적에 맞게 설정, stack 메모리를 만들고
 main()메서드가 최초로 올라가서 실행
 
  지금까지는 싱글 프로세스 + 싱글 스레드. 
  main()메서드 실행, 하나의 stack
  
   멀티스레드 -> 메모리에 stack 여러개
   	최소 2개 이상을 만드는 행위
   	
 */
public class Ex01_Single_Thread {
	public static void main(String[] args) {
		//thread를 만들지 않아서 순차적으로 실행됨
		System.out.println("나는 main 일꾼");
		worker();
		worker2();
		//stack메모리에 main()올라감
		// System.out.println("나는 main 일꾼");찍고 main()으로 돌아옴
		// worker();찍고 내려옴
		// worker2(); 
	}
	static void worker() {
		System.out.println("난 1번 일꾼");
	}
	static void worker2() {
		System.out.println("난 2번 일꾼");
	}
}
