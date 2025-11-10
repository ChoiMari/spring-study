package Event;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class LoginForm3 extends Frame{
	Label lbl_id;
	Label lbl_pwd;
	TextField txt_id;
	TextField txt_pwd;
	Button btn_ok;
	
	public LoginForm3(String title) {
		super(title);
		lbl_id = new Label("ID:",Label.RIGHT);
		lbl_pwd = new Label("pwd:",Label.RIGHT);
		
		txt_id = new TextField(10);
		txt_pwd = new TextField(10);
		txt_pwd.setEchoChar('#');
		btn_ok = new Button("login");
		
		this.setLayout(new FlowLayout()); //순서 (add())
		this.setSize(500, 100);
		this.setVisible(true);
		
		this.add(lbl_id);
		this.add(txt_id);
		
		this.add(lbl_pwd);
		this.add(txt_pwd);
		
		this.add(btn_ok);
		
		//inner class 활용하기 
		/*
		class Btn_handler implements ActionListener{  

			//button 클릭하면 ....
			//id 입력값 , pwd 입력값을 가지고 와서 유효성 검증
			
			//장점 : outer  class 자원 사용이 용이하다 (코드의 간소화)
			
			//Button 클릭 되면  실행 함수 .....actionPerformed
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = txt_id.getText().trim();  //TextField txt_id; 접근
				String pwd = txt_pwd.getText();
				
				System.out.println(e.getSource());
				if(id.equals("hong")) {
					System.out.println("방가 : " + "/ " + pwd);
				}else {
					System.out.println("배고픈 당신은 누구 ?");
				}
				
			}
			
		}
		*/
		
		//최종 ..... //익명 타입 (추상클래스와 인터페이스를 직접 구현할 수 있다 - 익명클래스로, 일회성)
		btn_ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = txt_id.getText().trim();  //TextField txt_id; 접근
				String pwd = txt_pwd.getText();
				
				System.out.println(e.getSource());
				if(id.equals("hong")) {
					System.out.println("방가 : " + "/ " + pwd);
				}else {
					System.out.println("배고픈 당신은 누구 ?");
				}
				
			}
		});
		
		this.addWindowListener(new WindowAdapter() {  //WindowAdapter 일반클래스
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().setVisible(false);
				e.getWindow().dispose(); //메모리 해제 ...
				
			}	
		});
		
		
		/*
			  
		단점)
		1회성
		만약 인터페이스 추상함수를 10개 가지고 있으면 다 구현  { 생성 } >> 비생산 적일 수 있다
		  
		  
		  
		this.addWindowListener(new WindowListener() { //WindowListener 인터페이스
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().setVisible(false);
				e.getWindow().dispose(); //메모리 해제 ...
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		*/
	}
}

public class Ex17_Button_Event_Final {

	public static void main(String[] args) {
		LoginForm3 login = new LoginForm3("로그인");

	}

}
