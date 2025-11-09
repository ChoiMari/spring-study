package kr.or.mari.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.or.mari.dto.ChatReadUpdateRequest;
import kr.or.mari.dto.ChatReadUpdateResponse;
import kr.or.mari.dto.LoginResponse;
import kr.or.mari.service.ChatReadService;
import lombok.RequiredArgsConstructor;
// 읽음 처리 API
// 사용자가 채팅방의 읽음 상태를 서버에 전달할 때 사용함
@RestController @RequiredArgsConstructor @RequestMapping("/api/chat/read")
public class ChatReadController {
	private final ChatReadService chatReadSvc;
	private final SimpMessagingTemplate messagingTemplate;
	
	// 마지막으로 읽은 메시지 ID 갱신 요청 처리
	//  PATCH는 자원의 “부분 업데이트”에 사용되는 HTTP 메서드.
	@PatchMapping
	public ResponseEntity<ChatReadUpdateResponse> updateLastRead(@RequestBody ChatReadUpdateRequest dto,
			HttpSession session){
		
		//로그인 되어있는 사용자인지 확인
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401 인증 필요
		}
		//요청 DTO에 로그인 사용자 ID 주입
		dto.setUserId(loginUser.getId());
		
		// 읽음 상태 업데이트 실행(DB 반영)
		ChatReadUpdateResponse res = chatReadSvc.updateLastRead(dto);
		
		//실시간 알림 전송 (같은 방 구독자에게 "읽음 이벤트" 브로드캐스트)
        messagingTemplate.convertAndSend(
            "/topic/chat/" + dto.getRoomId() + "/read",
            res
        );
		
		// 성공적으로 갱신된 읽음 정보 반환
		return ResponseEntity.ok(res);
	}
	
	
}
