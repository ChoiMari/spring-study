package Thread;

/*
여러개의 Thread 가 있을때 
우선순위 : default 5 (공정한 경쟁을 위해서 )
Min(1) .... Max(10)

상대적 수치값을 올리면 CPU 점유할 수 있는 확율이 높아진다

쓰레드가 5개 있는데 먼저 끝냈으면 좋겠는 스레드가 있음...
CPU점유 확률 높이고 싶다...
우선 순위가 있다. 
기본적으로는 default 5 서로 공정하게 경쟁
기본적으로 집합을 같이
숫자가 5 공평하다는 뜻

 근데 특정 스레드에게 수치를 높일 수 있다.
 무조건 이긴다기 보다는 이길확률이 높게.. 조작
 특정 스레드를 Min(1) 특정 스레드를 Max(10)으로 개발자가 조작이 가능함
 CPU 점유 확률을 줄이거나 높임
*/

class Pth extends Thread {
   @Override
   public void run() {
	   for(int i = 0 ; i < 1000 ; i++) {
		   System.out.println("--------------------------------");
	   }
   }
}

class Pth2 extends Thread {
	   @Override
	   public void run() {
		   for(int i = 0 ; i < 1000 ; i++) {
			   System.out.println("||||||||||||||||||||||||||||");
		   }
	   }
	}

class Pth3 extends Thread {
	   @Override
	   public void run() {
		   for(int i = 0 ; i < 1000 ; i++) {
			   System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		   }
	   }
	}

public class Ex06_Priority {

	public static void main(String[] args) {
		Pth pth = new Pth();
		Pth2 pth2 = new Pth2();
		Pth3 pth3 = new Pth3();
		
		// 우선순위 수치값 변경
	//default 기본 5 공평에서 개발자가 조작함, pth스레드의 점유확률을 높임
		// 우선 순위를 높였다.. 작업을 먼저 끝낼 확률을 높임
		pth.setPriority(10);
		pth2.setPriority(1);
		pth3.setPriority(1);

		// 우선순위 수치값 확인
		System.out.println(pth.getPriority());
		System.out.println(pth2.getPriority());
		System.out.println(pth3.getPriority());
		
		pth.start();
		pth2.start();
		pth3.start();
	}

}
