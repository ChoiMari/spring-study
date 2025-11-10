package Event;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Btn_handler implements ActionListener{  
	//1. 인터페이스 구현한 객체 사용

	//button 클릭하면 ....
	//id 입력값 , pwd 입력값을 가지고 와서 유효성 검증
	
	private TextField txtid;
	private TextField txtpwd;
	
	public Btn_handler(TextField txtid , TextField txtpwd) {
		this.txtid = txtid;  //주소값 
		this.txtpwd = txtpwd;
	}
	
	//Button 클릭 되면  실행 함수 .....actionPerformed
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getSource());
		if(txtid.getText().equals("hong")) {
			System.out.println("방가 : " + "/ " + txtpwd.getText());
		}else {
			System.out.println("배고픈 당신은 누구 ?");
		}
		
	}
	
}


class LoginForm extends Frame{
	Label lbl_id;
	Label lbl_pwd;
	TextField txt_id;
	TextField txt_pwd;
	Button btn_ok;
	
	public LoginForm(String title) {
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
		btn_ok.addActionListener(new Btn_handler(txt_id,txt_pwd));
		
	}
}

public class Ex15_Button_Event {

	public static void main(String[] args) {
		LoginForm login = new LoginForm("로그인");

	}

}
