package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//InnerClass (Outerclass자원 사용)
//InnerClass >> ClientSend
//InnerClass >> ClientReceive


public class Ex05_TCP_Chat_Client {
	Socket socket;
	public Ex05_TCP_Chat_Client() {
		try {
			socket = new Socket("192.168.2.57",9999);
			System.out.println("Chatt 서버와 연결 되었습니다");
			
			//ClientSend Thread start
			//ClientReceive Thread start
			
			new ClientSend().start();
			new ClientReceive().start();
			
			
		} catch (IOException e) {
			 e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Ex05_TCP_Chat_Client client = new Ex05_TCP_Chat_Client();

	}
	
	//Inner class
	class ClientSend extends Thread {
		@Override
		public void run() {
			BufferedReader br = null;  //서버 채팅창에 입력값 받기 
			PrintWriter pw = null;     //클라이언트에 전송 ....
			
			try {
				 
				 br = new BufferedReader(new InputStreamReader(System.in));//scanner
				 pw = new PrintWriter(socket.getOutputStream(),true); //auto flush
				 
				 while(true) {
					 String data = br.readLine(); //sc.nextLine()
					 if(data.equals("exit")) break;
					 pw.println(data); //접속한 서버에게 메시지 보내기 
					                 //dos.writeUTF(msg);
				 }
				 System.out.println("client send 작업 종료");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				
			}finally {
				try {
					pw.close();
					br.close();
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
			}	
		}
	}
	
	//Inner class - outer클래스의 자원을 사용할 수 있음
	class ClientReceive extends Thread {
		@Override
		public void run() {
			BufferedReader br = null;
			try {
				
				//InputStream in = socket.getInputStream();
				//DataInputStream dis = new DataInputStream(in);
				//String msg = dis.readUTF()
				
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String data = null;
				while((data = br.readLine()) != null) {
					System.out.println("Server 받은 메시지 [ " + data + " ] ");
				}
				System.out.println("Client Read 종료");
			} catch (Exception e) {
				  System.out.println(e.getMessage());
			} finally {
				try {
					br.close();
				} catch (IOException e) {
						e.printStackTrace();
				}
			}
		}
	}
}
