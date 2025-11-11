package Network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 TCP 서버
 클라이언트는 접속을 위해 서버의 IP와 포트번호가 필요하다.
 서버 IP : 192.168.2.229
 포트번호 : 7777(쓰지 않는 번호 임의로 정함) 
 */
public class Ex03_TCP_Server {
	public static void main(String[] args) throws IOException {
		//ServerSocket 클라이언트의 접속을 기다림(리스너의 역할)
		ServerSocket serverSocket = new ServerSocket(7777);
		System.out.println("클라이언트의 요청 기다림...");
		
		Socket socket = serverSocket.accept();
		//클라이언트의 요청이 올 때까지 기다림
		System.out.println("연결 완료");
		
		//서버의 소캣객체와 클라이언트의 소캣 객체가 연결된 상태
		// socket 객체는 입출력 객체를 가지고 있다(input, output stream 내장)
		OutputStream out = socket.getOutputStream();
		// 소캣 객체로 부터 나갈 수 있는 주소값을 얻어냄.
		//read와 write하려면 연결을 2개를 해야함
		// 읽는 stream, 쓰는 stream
		// 양쪽으로 읽고 쓰려면 싱글 스레드로 불가능..
		// 멀티 스레드 써야함
		
		DataOutputStream dos = new DataOutputStream(out);
		//8가지 타입으로 읽고 쓸 수 있음
		// 보조 스트림
		
		dos.writeUTF("문자데이터 Byte 통신입니다.");//UTF로 인코딩 시켜서 쓰겠다라는 뜻
		System.out.println("서버 종료");
		
		//네트워크 자원은
		//가비지 컬렉터가 수집하지 못하는 자원이라서
		// 명시적으로 개발자가 닫아주어야한다
		dos.close();
		out.close();
		
		socket.close();
		serverSocket.close();
		
	}
}
