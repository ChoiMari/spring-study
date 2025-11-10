package Thread;

import javax.swing.JOptionPane;

/**
 * 게임을 만들고 싶음.. 문제는 문제대로 출력이 되고.. 시간은 시간대로 흐르길 원함..
 * 
 * 2개의 작업이 동시에 실행되길 원함..(경합..)
 * 
 * 싱글(단일)스레드로는 불가능..
 * 
 * -> 멀티 스레드 필요(여러개의 stack 메모리를 확보해야 함)
 */

class WordTime extends Thread {
	@Override
	public void run() {
		for (int i = 10; i > 0; i--) {
			if (Ex04_Multi_Word_Game.inputCheck) {
				return; // run 함수 종료, thread종료
			}
			try {
				System.out.println("남은 시간 : " + i);
				Thread.sleep(1000);// 1초, 남은 시간 출력하고 휴게실 가서 쉬어라..
				// 그리고 다시 점유함..(다시 runnable)
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

}

class WordInputThread extends Thread {
	@Override
	public void run() {
		String dan = "2";
		String inputData = JOptionPane.showInputDialog(dan + "단을 입력하세요");
		if (inputData != null && !inputData.equals("")) {
			Ex04_Multi_Word_Game.inputCheck = true;
		}
		System.out.println("입력 값 : " + inputData);
	}
}

public class Ex04_Multi_Word_Game {

	static boolean inputCheck = false;

	public static void main(String[] args) {
		WordTime timer = new WordTime();
		timer.start(); // stack만들고 run 함수 올리기
		WordInputThread wordInputThread = new WordInputThread();
		wordInputThread.start();
		
		//상태
		// 각 위성과 지구와의 거리
		// 목성 거리 + 토성 거리 + 금성 거리
		// -> 총 거리합(3개의 스레드 종료 후 그 다음에 종료)
		
		//main 메서드가 word 스레드, time 스레드가 종료 후 
		// 종료 시키고 싶다.. -> join() 사용
		try {
			//timer.stop() 개발자가 예전엔 이거 사용해서 강제로 스레드를 종료시켰으나..
			// 이제 이건 사라지는 메서드...
			// 개발자가 컨트롤하는건 위험해서 사라짐..
			timer.join(); // join() : main스레드야. 넌 내가 끝날 때까지 기다려(강제)
			wordInputThread.join(); // join() : main스레드야. 넌 내가 끝날 때까지 기다려(강제)
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("Main END");
	}
}
