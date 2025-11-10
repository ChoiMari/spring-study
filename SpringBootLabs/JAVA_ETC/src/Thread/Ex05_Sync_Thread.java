package Thread;
/**
	멀티스레드 환경에서 가장 중요한 것.
	-> 공유자원
	
	락이 없다, 동기화를 보장하지 않는다.
	
	synchronized (동기화)

	Collection (Vetor , ArrayList) 비교
	
	Vetor 동기화 보장 ...> 멀디 스레드 환경 > 자원 보호 > 화장실 (lock) > 성능 저하....
	ArrayList 동기화 보장하지 않아요 > 성능 보장 > 자원 보호 보장....하지 않아요
	
	한강
	화장실(1개): 공유자원
	여러명의 사람들 (10명) : 10개의 스레드 가...... >> 동시에 화장실 접근
	
	반대 (성능)
	한강 비빕밥 축제 : 여러 사람이 동시 접근 (빨리 ....) Lock  처리 안되요
 	
 	LOCK 처리 : 객체 단위 또는 함수 단위로 걸 수 있다.
 		기능단위 묶어서 동기화 보장이 편하다.
 		
 	동기화 보장 : 공유자원을 동시에 건드려도 데이터가 꼬이지 않게, 
 					접근 순서를 안전하게 관리한다는 뜻
 				한 번에 한 명씩만 들어오게 문을 잠그는 것
 				
 	질문 : 멀티 스레드 해보셨나요?
 		멀티 스레드의 동기화를 설명해보세요
 	"하나의 공유자원에 대해서 동시에 실행하려다 보니까 충돌이 일어날 수 있어서,
	공유자원을 동시에 건드려도 데이터가 꼬이지 않도록 접근 순서를 안전하게 관리,
	synchronized 키워드를 사용해서 동기화를 보장할 수 있음."
	
	동기화를 해야되는 상황?
	동기화가 되어있는 객체를 사용해 본적 있냐? -> 백터, ArrayList
	동기화를 보장하지 않아서 좋은 이유?
	반대로 보장하지 않으면?  값의 변화를 예측할 수 없고, 등의 문제
 */

class Wroom {
    synchronized void openDoor(String name) {
    	//synchronized 락검.. 동기화 보장
		System.out.println(name + "님 화장실 입장");
		for(int i = 0 ; i < 100 ; i++) {
			System.out.println(name + " 사용 중 " + i);
			if(i == 10) {
				System.out.println(name + "님 끙 ...");
			}
			
		}
		System.out.println("휴..살았다..");
	}
}

class User extends Thread{
	Wroom wr;
	String who;
	
	User(String name, Wroom wr){
		this.who = name;
		this.wr = wr;
	}
	
	@Override
	public void run() {
		wr.openDoor(this.who);
	}
}

public class Ex05_Sync_Thread {
	
	public static void main(String[] args) {
		//한강..
		//화장실이 있음
		Wroom w = new Wroom();
		
		//사람들이 놀고 있음
		User kim = new User("김씨", w);
		User lee = new User("이씨", w);
		User park = new User("박씨", w);
		
		//급하다!
		kim.start();
		lee.start();
		park.start();
	}
}
