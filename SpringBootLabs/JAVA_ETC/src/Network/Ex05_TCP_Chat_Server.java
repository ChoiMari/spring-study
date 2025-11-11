package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
Ex01 ~ Ex03 하나의 Thread가지고 작업 (순차적인 데이터 처리)

Server : read , write
Client : read , write 
read ,write 동시에 처리되지 않았다

read , write 동시에 .... thread

stack을 여러개 사용해서 ....  1:1 통신(server 와 client) >> read , write 동시에...


동시에 처리한다? -> (멀티)스레드
1개의 스레드는 읽기만
1개의 스레드는 쓰기만 담당

1개의 쓰레드로 읽기 쓰기 동시에 못함.
*/
public class Ex05_TCP_Chat_Server {

	public static void main(String[] args) {
		ServerSocket serviersocket = null;
		
		try {
			   serviersocket = new ServerSocket(9999);
			   System.out.println("접속 대기중....");
			   Socket socket =  serviersocket.accept();
			   System.out.println("Client 연결 완료 ");
			   
			    //기존코드
				//read
				//InputStream in = socket.getInputStream();
				//DataInputStream dis = new DataInputStream(in);
			   
			    new ServerSend(socket).start();
			    new ServerReceive(socket).start();
			   
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				serviersocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

//write 전용 Thread
//socket > outputStream
class ServerSend extends Thread {
	Socket socket;
	public ServerSend(Socket socket) {  //new ServerSend(socket).start();
		this.socket = socket;
	}
	
	@Override
	public  void run() { //Thread 의 main 역할
		//OutputStream out = socket.getOutputStream();
		//DataOutputStream dos = new DataOutputStream(out);
		
		BufferedReader br = null;  //서버 채팅창에 입력값 받기 
		PrintWriter pw = null;     //클라이언트에 전송 ....
		
		try {
			 
			 br = new BufferedReader(new InputStreamReader(System.in));//scanner
			 pw = new PrintWriter(socket.getOutputStream(),true);
			 
			 while(true) {
				 String data = br.readLine(); //sc.nextLine()
				 
				 if(data.equals("exit")) break;
				 pw.println(data); //접속한 클라이언트에게 메시지 보내기 
				                 //dos.writeUTF(msg);
			 }
			 System.out.println("server send 작업 종료");
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

//Read 전용 Thread
//socket : inputStream
class ServerReceive extends Thread{
	Socket socket;
	public ServerReceive(Socket socket) {
		this.socket  = socket;
	}
	
	
	
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
				System.out.println("Client 받은 메시지 [ " + data + " ] ");
			}
			System.out.println("ServerRead 종료");
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
