package kr.or.mari.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.or.mari.dto.ChatMessageRequest;
import kr.or.mari.dto.ChatMessageResponse;
import kr.or.mari.dto.LoginResponse;
import kr.or.mari.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
/**
 * 웹소켓 기반 실시간 메시지 송신 처리
 * REST API 기반 과거 메시지 조회 기능 제공
 * 
 *  WebSocket : 클라이언트가 /app/chat.send 로 보낸 메시지 처리
 *  REST      : 특정 채팅방의 메시지 목록(최근 30개) 조회
 */
@RestController @RequiredArgsConstructor 
@RequestMapping("/api/chat/messages")
public class ChatMessageController {
	private final ChatMessageService chatMsgSvc;
	//private final SimpMessagingTemplate messagingTemplate;// WebSocket 참여자에게 실시간 전송 도구
	// 웹소캣에서 메시지를 실시간으로 특정 대상에게 보내는 스프링 내장도구
	// 백엔드에서 클라이언트로 푸시 메시지를 보낼 떄 사용하는 핵심 클래스
	// SimpMessagingTemplate 서버에서 웹소켓 구독자에게 실시간 메시지를 전달해주는 스프링 내장 메신저
	// 스프링 웹소켓 또는 STOMP 기반의 실시간 채팅, 알림 시스템에서 서버에서 클라이언트로 데이터를 push할 때 사용함
	//A가 메시지를 보냄
	// 서버가 DB에 저장함
	// 서버가 SimpMessagingTemplate를 이용해서 이 방에 참여 중인 사람들한테
	// 메시지를 push 이때, 쓰는게 convertAndSend() 메서드
	
	
	//[웹소켓 메시지 송신 처리]
	//@MessageMapping은 STOMP에서 쓰는 메시지 라우팅
	// STOMP 메시지의 목적지를 매핑함
	// 클라이언트가 STOMP 프로토콜로 "/app/chat.send" 경로로 메시지를 전송하면 이 메서드가 호출됨.
	/**
	 * 처리 흐름
	 * 1. 클라이언트가 /app/chat.send 로 메시지 보냄 (STOMP publish)
	 * 2. @MessageMapping("/chat.send") 이 해당 메시지를 수신함
	 * 3.  ChatMessageService.saveMessage(dto)를 호출하여 DB에 저장
	 * 4. SimpMessagingTemplate.convertAndSend()로 같은 방을 구독 중인 모든 사용자에게 브로드캐스트
	 
	 * [예시]
     * 클라이언트 → 서버:
     * {
     *   "roomId": 1,
     *   "senderId": 3,
     *   "content": "안녕하세요!"
     * }
     *
     * 서버 → 클라이언트 (브로드캐스트):
     * destination: /topic/chat/1
     * payload: ChatMessageResponse(JSON)
     * 
     * 브로드캐스트 : 서버가 새 메시지를 모든 구독자들에게 뿌림
	 * @param dto
	 */
	@MessageMapping("/chat.send")
	public void sendMessage(ChatMessageRequest dto) {
		//채팅 메시지 DB에 저장함
		//ChatMessageResponse saved = chatMsgSvc.saveMessage(dto);
        chatMsgSvc.saveMessage(dto);
        // 컨트롤러는 메시지를 받기만 하고,
        // 실제 DB 저장 및 브로드캐스트, 알림 푸시는 Service에서 처리
		
		// 구독 경로
		//topic/chat/ 같은 경로는 스프링 STOMP(웹소켓 메시지 시스템의 약속된 관습)
		/*
		 * 왜 이렇게 해야하나?"
		 * STOMP 프로토콜 구조와 스프링의 메시지 브로커 설계 규칙 때문
		 * STOMP 프로토콜은 HTTP가 아니라 Pub/Sub 발생과 구독 모델이다.
		 * HTTP는 요청과 응답이지만,
		 * STOMP는 발행자와 중개자 구독자가 있음
		 * 발행자 : 서버 또는 클라이언트가 메시지를 보냄(메시지를 보내는 사용자)
		 * 중개자 : 메시지를 받아서 구독 중인 모든 사람에게 전파함(스프링 내부 메시지 브로커, (SimpleBroker) / RabbitMQ / Kafka 등)
		 * 구독자 : 특정 주제를 구독해서 실시간으로 받음(같은 방에 접속 중인 사용자들)
		 * 
		  <Spring이 STOMP 프로토콜에 따라 정한 관습이자 약속>
		  /topic/** 브로드캐스트용
		  /queue/** 1:1 전송용 
		  
		  예시) /topic/chat/1은 “채팅방 1번을 구독 중인 모든 사용자에게 보내라”는 뜻
		 */
		// 방 참가자들에게 실시간으로 전송함(브로드캐스트)
		//messagingTemplate.convertAndSend("/topic/chat/" + dto.getRoomId(), saved);
		//topic/chat/{roomId}를 구독 중인 모든 클라이언트에게
		//saved 메시지를 실시간으로 전송하라
		//-> 서비스 계층 로직으로 변경함
		
	}
	
	
	//특정 채팅방의 최근 메시지 조회
	// 클라이언트가 특정 방의 메시지 목록을 요청할 때 사용
	// 로그인 세션에서 사용자 인증 확인 후, ChatMessageService를 통해 최근 메시지 최대 30개 반환
	@GetMapping("/{roomId}")
	public ResponseEntity<List<ChatMessageResponse>> getMessages(
			@PathVariable(name = "roomId") Long roomId,
			HttpSession session){
		
		//세션에서 로그인 사용자 확인함
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			//401 : 인증이 필요하다
		}
		
		//해당 방의 최근 메시지 30개를 조회함
		List<ChatMessageResponse> messages = chatMsgSvc.getRecentMessages(roomId);
		
		//JSON 응답 반환
		return ResponseEntity.ok(messages);
		//.ok()는 내부적으로 빌더 + build()까지 끝낸 단축메서드라서 필요없다.
	}
	
}
