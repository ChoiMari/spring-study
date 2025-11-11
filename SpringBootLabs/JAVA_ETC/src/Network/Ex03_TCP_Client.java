package Network;
//클라이언트는 서버의 ip와 포트번호가 필요
// 소캣 객체로 서버에 요청

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Ex03_TCP_Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket socket = new Socket("192.168.2.229",7777);
		System.out.println("서버와 연결 되었습니다.");
		
		//서버에서 보낸 메시지 받기
		InputStream in = socket.getInputStream(); // 읽음
		// 소캣 객체의 연결된 통로로 읽기
		
		DataInputStream dis = new DataInputStream(in);
		
		String servermsg = dis.readUTF();
		//UTF로 인코딩해서 읽음
		System.out.println("서버에서 보낸 메시지 : " + servermsg);
		dis.close();
		in.close();
		socket.close();
	}
}
