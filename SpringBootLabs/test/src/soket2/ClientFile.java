package soket2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientFile {
	Socket socket;
	BufferedWriter bw;
	BufferedReader br; // 키보드 연결을 위해서 사용
	
	public ClientFile() {
		try {
			System.out.println("클라이언트 소켓 시작 -------------");
			socket = new Socket("localhost", 10000); //-> 해당 코드 실행 시
			// 서버 소켓의 accept() 메서드가 호출됨, -> 서버에서 소켓이 만들어진다. //-> 두 소켓 연결 시작
			// 첫번째 아규먼트 : 접속할 서버의 ip주소 
			// 두번째 아규먼트 : 서버 프로그램이 열어둔 포트 번호
			//-> 이 ip 주소의 10000번 포트에 연결해줘라고 요청
			System.out.println("서버에 연결되었습니다.");
			
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			//BufferedWriter는 성능 향상을 위해 사용
			//OutputStreamWriter를 한 번 더 감싸서 쓰기 성능 높이고, 가변 길이 문자열을 편하게 다룸
			// 성능을 향상 후 전송
			//OutputStreamWriter 문자 -> 바이트 변환기
			// 네트워크 통신(TCP/IP)는 바이트 단위로 데이터를 주고 받는다.
			// 데이터를 주고 받으려면 바이트 배열로 변환해서 전송해야함
			// 문자 'A'를 아스키 코드 65 -> 이진수로 바꿔서 -> 네트워크 전송
			// 사람이 읽을 수 있는 문자를 네트워크가 이해할 수 있는 이진 데이터로 바꾸어주는 역할을 한다.
			// 클라이언트 소켓 입장에서는 쓰기는 나가는 스트림 socket.getOutputStream()
			// 서버 입장에서는 들어오는 스크림이여서 socket.getInputStream()로 받음
			System.out.println("쓰기 버퍼 연결 완료 ----------");
			// 키보드 연결 System.in
			br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("키보드 스크림 + 읽기 버퍼 연결 완료");
			
			while(true) {
				System.out.println("키보드 메시지 입력 대기 중");
				String keyboardMsg = br.readLine(); // 한줄 씩 읽음
				
				bw.write(keyboardMsg + "\n"); // 상대방 서버 쪽으로 씀
				// 메세지를 보낼 땐, 메세지의 끝을 알려주어야 한다. \n
				bw.flush(); // 강제로 버퍼 비우기
			}

		} catch (Exception e) {
			System.out.println("클라이언트 소켓 에러 발생함 : " + e.getMessage());
		}
		
	}
	
	public static void main(String[] args) {
		new ClientFile();
	}
}
