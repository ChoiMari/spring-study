package soket2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFile {
	// 소캣을 왜 2개를 했냐?
	ServerSocket serverSocket; // 클라이언트의 연결만 받는 서버 소켓
	// 서버 소캣 : 연결 요청 수락용
	
	Socket socket; // 실제 통신을 하는 소켓, 실제 데이터 주고 받는 통로
	// 실제 데이터 송수신용
	
	BufferedReader br;
	
	public ServerFile() {
		System.out.println("1. 서버소캣 시작 -------------");
		try {
			// 1. 서버 소켓 생성(포트 번호 필수), 생성자의 아규먼트로 포트번호를 넣어준다
			serverSocket = new ServerSocket(10000); // 통신 포트번호 - 10000번으로 설정
			// 포트번호 10000번 문앞에서 기다리고 있다..
			/*
		     public ServerSocket(int port) throws IOException {
		        this(port, 50, null);
		    } 
		 */
			
				System.out.println("2. 서버소켓 생성 완료----------");
				System.out.println("클라이언트 연결 대기 중...");
				
				//클라이언트의 연결 요청을 기다림 (blocking)
				socket = serverSocket.accept(); // 클라이언트 접속 대기중..
				//accept()는 언제 발동 하느냐? 
				// 클라이언트가 연결을 하면 실행된다
				// 클라이언트의 연결 요청을 기다리다가, 연결이 들어오면 수락하는 코드
				// accept()는 대기하다가 클라이언트가 문을 두드리면 열어주는 메서드
				//Blocking 메서드라서, 누군가 접속하기 전까지 이 줄에서 프로그램이 멈춰있음
				System.out.println("3. 클라이언트 연결 완료");
				
				// 버퍼를 단다 - 데이터 수신용 스트림 연결
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//소켓의 인풋스트림과 버퍼를 연결함 - input 외부로 부터 들어오는 버퍼를 담
				System.out.println("읽기 버퍼 연결 완료");
				while (true) {
					// 클라이언트로부터 데이터 수신
					String msg = br.readLine(); // 클라이언트가 보낸 데이터를 한 줄씩 읽는 코드
					System.out.println("4. 클라이언트로 부터 받은 메시지 : " + msg);
				}

		} catch (Exception e) {
			System.out.println("서버 소켓 에러 발생함 : " + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new ServerFile();

	}

}
