package Event;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//자바의 태생.. CS프로그램(메모장, 그림판, 이클립스 같은 툴)

//awt 기술 : OS가 가지고 있는 자원
//swing : 순수한 자바로 컴포넌트 기반으로 만듬
class MyFrame extends Frame{
	public MyFrame(String title) {
		super(title);
	}
}

//이벤트 클래스
class BtnClickHandler implements ActionListener{
	//클릭 이벤트가 발생 되었을 때, 실행되는 함수 구현

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("나를 클릭해라..");
		
	}
	
}

public class Ex14_awt_Frame {
	
	public static void main(String[] args) {
		MyFrame my = new MyFrame("login");
		my.setSize(350,300);
		my.setVisible(true);
		my.setLayout(new FlowLayout()); //띄워지는 창에 대한 모눈종이
		
		Button btn1 = new Button("one button");
		Button btn2 = new Button("one button");
		Button btn3 = new Button("one button");
		
		//일반적인 방법
		BtnClickHandler handler = new BtnClickHandler();
		btn1.addActionListener(handler);
		//버튼에 이벤트 붙임
		
		//이벤트 발생 소스, 행위, 감지기 
		
		//익명객체
		btn2.addActionListener(new ActionListener() {
			//원칙 : class BtnClickHandler implements ActionListener
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("인터페이스 익명 객체 구현");	
			}
		});
		
		my.add(btn1);
		my.add(btn2);
		my.add(btn3);
		
		
	}
}
