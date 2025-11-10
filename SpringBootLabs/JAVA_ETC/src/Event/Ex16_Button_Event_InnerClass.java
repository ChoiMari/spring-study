package Event;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class LoginForm2 extends Frame{
	Label lbl_id;
	Label lbl_pwd;
	TextField txt_id;
	TextField txt_pwd;
	Button btn_ok;
	
	public LoginForm2(String title) {
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
		class Btn_handler implements ActionListener{  

			//button 클릭하면 ....
			//id 입력값 , pwd 입력값을 가지고 와서 유효성 검증
			
			//inner 클래스의 장점 : outer  class 자원 사용이 용이하다 (코드의 간소화)
			
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
		
		
		btn_ok.addActionListener(new Btn_handler());
		
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
	}
}

public class Ex16_Button_Event_InnerClass {

	public static void main(String[] args) {
		LoginForm2 login = new LoginForm2("inner class");

	}

}
