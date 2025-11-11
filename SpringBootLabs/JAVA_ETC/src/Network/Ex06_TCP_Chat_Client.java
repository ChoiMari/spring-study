package Network;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Ex06_TCP_Chat_Client extends JFrame implements ActionListener , Runnable {

	JTextArea ta; //출력창 (다중라인)
	JTextField txtinput; //채팅 내용 입력창
	DataInputStream in;
	DataOutputStream out;
	
	public Ex06_TCP_Chat_Client() {
		//초기화
		//UI 구성
		this.setTitle("Multi 채팅");
		ta = new JTextArea(); //출력창
		txtinput = new JTextField(); //입력창
		JScrollPane sp = new JScrollPane(ta, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				                             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		sp.setAutoscrolls(true);
		ta.setBackground(Color.yellow);
		ta.setLineWrap(true); //텍스트 자동 줄바꿈
		ta.setEditable(false); //편집 안되요 (출력....)
		
		txtinput.setText("이름 입력");
		txtinput.requestFocus();
		txtinput.selectAll();
		
		this.add(sp,"Center");
		this.add(txtinput , "South");
		this.setSize(400, 500);
		this.setVisible(true);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		txtinput.addActionListener(this);
		
		//소켓 생성하고 설정
		try {
			  Socket socket = new Socket("192.168.2.18",9999);
			  in = new DataInputStream(socket.getInputStream());
			  out = new DataOutputStream(socket.getOutputStream());
			  
			  //서버와 연결
			  ta.append("서버에 접속 되었습니다 \n\r");
			  
			  //Thread 구동
			  Thread client = new Thread(this);
			  client.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
				
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
	  //Textfield 입력하고 enter 처리되면
	  //actionPerformed 가 자동 호출  txtinput.addActionListener(this);
	 if(e.getSource().equals(txtinput)) {
		 String msg = txtinput.getText();
		 try {
			   out.writeUTF(msg); //서버로 출력
			   txtinput.setText("");
		} catch (Exception e2) {
			System.out.println(e2.getMessage());
		}
	 }	
		
	}
	
	
	@Override
	public void run() {
		try {
			  String msg = in.readUTF();
			  ta.append(msg + "\n\r");
			  while(in != null) {
				  msg = in.readUTF();
				  ta.append(msg + "\n\r");
			  }
		} catch (Exception e) {
			System.out.println("접속중 서버와 연결 종료");
			return;
		}
		
	}


	
	public static void main(String[] args) {
		Ex06_TCP_Chat_Client client = new Ex06_TCP_Chat_Client();

	}

	

}