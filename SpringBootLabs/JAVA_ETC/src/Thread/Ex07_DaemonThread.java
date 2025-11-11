package Thread;

//자동으로 저장하겠냐? 묻는것도 하나의 쓰레드
// 주 스레드의 의존해서 같이 끝나는 (자동 저장기능같은)걸 보조 스레드라고 함
class AutoSaveThread extends Thread {
	
	public void save() {
		System.out.println("작업 내용을 자동 저장 ....");
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				  Thread.sleep(2000); //자다가 
			} catch (Exception e) {
				break;
			}
			
			save(); //깨면 저장함
		}
		
	}
}
// main(주 스레드)가 끝나면 죽어버림.. 그래서 보조 스레드라고 그런다

public class Ex07_DaemonThread {

	public static void main(String[] args) {
		//main이 주 스레드, autosavethread가 보조스레드ㄴ
		AutoSaveThread autosavethread = new AutoSaveThread();
		autosavethread.setDaemon(true);  //autosavethread 데몬 ....
		autosavethread.start();
		//main thread  종료 .... 데몬도 종료
		
		try {
			  Thread.sleep(5000); //main 쓰레드 재움, autosavethread스레드는 돌고 있음
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("메인 스레드 종료");
	}

}
