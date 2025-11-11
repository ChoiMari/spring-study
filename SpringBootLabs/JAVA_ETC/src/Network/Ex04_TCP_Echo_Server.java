package Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Ex04_TCP_Echo_Server {

	public static void main(String[] args) throws IOException {
		
		ServerSocket serversocket = new ServerSocket(9999);
		System.out.println("접속 대기중....");
		Socket socket  =   serversocket.accept(); //클라이언트 요청이 오면 (접속)
		System.out.println("연결 완료"); // socket 과 socket 연결
		
		/*
		 Client 서버로 write 데이터를 서버가 받아서 그대로 다시 Client 전송
		 server : read , write
		 client : write , read
		 
		socket 으로 부터 : input, output  stream 을 얻어야 하는 구나
		 */
		
		//read
		InputStream in = socket.getInputStream();
		DataInputStream dis = new DataInputStream(in);
		
		//write
		OutputStream out = socket.getOutputStream();
		DataOutputStream dos = new DataOutputStream(out);
		
		while(true) {
			String clientmsg = dis.readUTF();
			System.out.println("Client msg : " + clientmsg);
			
			if(clientmsg.equals("exit")) break;
			
			//메아리 기능
			dos.writeUTF(clientmsg);
			dos.flush(); //close() 해도 되는데 ... 바로바로 내용을 전달
		}
		System.out.println("클라이언트 종료 요청(exit) 서버 종료");
		
		dis.close();
		dos.close();
		in.close();
		out.close();
		socket.close();
		serversocket.close();
	}

}
