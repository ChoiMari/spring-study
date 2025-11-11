package Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;



/*
서버 1개
여러명의 Client 가 하나의 서버 접속 : 채팅방 1개 가정 
KEY POINT : HashMap > key : 사용자ID , Value : socket 객체 주소

여러개의  채팅방 : 어떤 collection 

ArrayList<HashMap> ... 
[0] 방번호  > HashMap

HashMap<k,List>  > key : 방번호 , ArrayList 주소 (socket 배열)
*/
public class Ex06_TCP_Chat_Server {
	
	ServerSocket serversocket = null;
	Socket socket = null;
	
	//POINT MAP
	HashMap<String, DataOutputStream> ClientMap;
	//Map의 value는?
	//-> DataOutputStream Socket 가지고 있는 Stream 의 주소값
	
	//HashMap<String, DataOutputStream>
	// key는 사용자ID, 값은 소캣 주소
	//kglim , Socket
	//hong  , Socket
	
	
	public Ex06_TCP_Chat_Server() {
		ClientMap = new HashMap<>(); //각각의 socket 가지는 출력객체의 주소 관리
	}
	
	//1. 서버 초기화 하는 작업
	public void init() {
		try {
			   serversocket = new ServerSocket(9999); //포트번호 9999로 열음
			   System.out.println("[ 서버시작 .. 요청 대기 .....]");
			   while(true) {
				   socket = serversocket.accept(); // 클라이언트가 접속 할 때까지 기다림
				   System.out.println(" [ " + socket.getInetAddress() + " / "
						  		      +socket.getPort() + " ] ");
				   //socket.getInetAddress() : 연결된 클라이언트의 ip를 얻어냄
				   
				   //코드 작업
				   //클라이언트 접속시 마다
				   //Thread client = new   socket 생성자로
				   //Thread start() 
				   
				   Thread client = new MultiServerRev(socket);
				   client.start();
				   
			   }
			
		} catch (Exception e) {
			  System.out.println("Init " + e.getMessage());
		}
	}
	
	
	//2. 접속된 모든 클라이언트 (socket) 메시지를 전달하고 싶어요
	//Map(key , value)
	//key >> 사용자ID(고유값) ex) kglim , hong , kim
	//ClientMap<kglim, 각각의 socket 객체로부터 얻어낸 DataOutPutStream 주소값>
	//ClientMap<hong,  각각의 socket 객체로부터 얻어낸 DataOutPutStream 주소값>
	public void sendAllMsg(String msg) {
		Iterator<String> ClientKeySet = ClientMap.keySet().iterator();
		while(ClientKeySet.hasNext()) {//모든 접속 클라이언트에게 메시지 브로드캐스트 전송
			try {
				// 현재 사용자 ID로부터 DataOutputStream 객체 꺼내기
				  DataOutputStream clientout = ClientMap.get(ClientKeySet.next());
				  //각각의 Client 에게 메시지 전달
				  //ClientMap.get key 가 가지고 있는 value 얻기 (각각의 socket 얻은  DataOutputStream 주소값)
				  clientout.writeUTF(msg);// writeUTF() : UTF-8 인코딩된 문자열로 전송 
			} catch (Exception e) {
				System.out.println("send Exception : " + e.getMessage());
			}
		}
	}
	
	//3. 접속된 모든 유저 리스트 목록 관리하기
	public String showUserList(String name) {
		StringBuilder output = new StringBuilder("<<접속자 목록 >>\n\r"); //개행처리 , enter 처리
		
		Iterator<String> users = ClientMap.keySet().iterator();
		
		while(users.hasNext()) {
			try {
				   String key = users.next(); //접속한 ID >> kim , hong
				   if(key.equals(name)) { //목록을 요청한 사용자 라면
					   key+="(*)";
				   }
				   output.append(key + "\n\r");
				   
			} catch (Exception e) {
				System.out.println("showUserList method : " + e.getMessage());
			}
		}
		
		return output.toString();
	}
	
	//4. 옵션 (귓속말 ) 특정 id 에게만
	public void sendToMsg(String fromname, String toname , String tomsg) {
		try {
			  ClientMap.get(toname).writeUTF("귓속말 from (" + fromname + ")=>" + tomsg);
			  //ClientMap.get(toname) >> socket 이 가지는 DataOutPutStream 주소값
			  ClientMap.get(fromname).writeUTF("귓속말 to (" + toname + ")=>" + tomsg);
		} catch (Exception e) {
			System.out.println("sendToMsg 예외 : " + e.getMessage());
		}
	}
	
	//Thread  사용 (보내기 , 받기)
	//inner class 형태로
	
	class MultiServerRev extends Thread {
		Socket socket = null;
		
		DataInputStream in;
		DataOutputStream out;
		
		public MultiServerRev(Socket socket) {
			this.socket = socket;
			try {
				   in = new DataInputStream(this.socket.getInputStream());
				   out = new DataOutputStream(this.socket.getOutputStream());
				   
			} catch (Exception e) {
				System.out.println("MultiServerRev  예외 : " + e.getMessage());
			}
			
		}
		
		@Override
		public void run() {
			//read , write   (readUTF() , writeUTF())
			String name=""; //클라이언트 이름 (id)  저장
			
			 try {
				    //연결된 socket 을 통해서 Client 메시지 전달
				    out.writeUTF("이름을 입력하세요");
				    name = in.readUTF();
				    out.writeUTF("현재 채팅방에 입장하셨습니다"); //현재 1개의 채팅방 입니다 ^^
				    
				    //채팅방 10명 user 접속
				    //서버에 접속된 모든 사용자(socket) 입력된 내용 전달
				    sendAllMsg(name + "님 입장^^"); //10개의  socket에 전달
				    
				    //KEY POINT 
				    //현재 접속한 socket 도 관리 (Map)
				    ClientMap.put(name, out); // Map("김씨", socket 가는 출력객체 주소)
				    System.out.println("서버 모니터링 : 현재 접속자 수는 : "+ ClientMap.size() + "명");
				  //추가기능 (대화)
				    while(in != null) {
				    	String msg = in.readUTF();
				    	
				    	//채팅방에 참여하고 있는 모든 접속자에게 전달
				    	if(msg.startsWith("/")) {
				    		if(msg.trim().equals("/접속자")) {
				    			//접속한 사용자 목록
				    			out.writeUTF(showUserList(name)); 
				    		}else if(msg.startsWith("/귓속말")) {
				    			String[] msgarr = msg.split(" ",3);
				    			//  /귓속말  홍길동  방가방가
				    			if(msgarr == null || msgarr.length < 3) {
				    				//나머지 옵션이 정상 코드가 아니다
				    				out.writeUTF("HELP:사용법\n\r /궛속말 [상대방ID] [메시지]");
				    			}else {
				    				String toname = msgarr[1];
				    				String tomsg  = msgarr[2];
				    				if(ClientMap.containsKey(toname)) {
				    					//map 안에 id 존재하면
				    					//메시지 보내기 (특정 상대방)
				    					sendToMsg(name,toname,tomsg);
				    				}else {
				    					out.writeUTF("입력한 사용자가 없습니다 ...");
				    				}
				    			}
				    		} else {
				    			out.writeUTF("잘못된 명령어 입니다");
				    		}
				    	}else {
				    		//전체 사용자에게 write
				    		sendAllMsg(" [ " +  name + " ] " + msg);
				    		
				    	}
				    }//while end
				    
			} catch (Exception e) {
				System.out.println("Thread run 예외 :" + e.getMessage());
			}finally {
				//Client (종료) 퇴장
				ClientMap.remove(name); //클라이언트 퇴장 시 map에서 제거함
				sendAllMsg(name + "님 퇴장하였습니다");
				 System.out.println(" Finally 서버 모니터링 : 현재 접속자 수는 : "+ ClientMap.size() + "명");
			}
		}
	}
	
	
	public static void main(String[] args) {
		Ex06_TCP_Chat_Server server = new Ex06_TCP_Chat_Server();
		server.init();

	}

}			    