package Thread;

import javax.swing.JOptionPane;

/**
 멀티 스레드가 필요
  - 동시에 실행 되어야하는 경우
 단어 맞추기 게임
 구구단 게임
 
 문제가 나오고 답을 맞춰야함
 시간이 주어지고, 일정 시간이 지나면 게임이 종료되길 원함
 
 */
public class Ex03_Single_Word_Game {
	public static void main(String[] args) {
		String inputData = JOptionPane.showInputDialog("값을 입력하세요");
		System.out.println("입력값 : " + inputData);
		
		timer(); //-> 입력값이 끝나야지만 시간이 흐름..
		// 원하는 작업이 아니다..
		// 싱글스레드로는 구현 불가
	}
	//타이머 만듬
	static void timer() {
		for(int i = 10;i > 0;i--) {
			try {
				System.out.println("남은 시간 : " + i);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
