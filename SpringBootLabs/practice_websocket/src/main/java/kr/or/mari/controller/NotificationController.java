package kr.or.mari.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.or.mari.dto.LoginResponse;
import kr.or.mari.dto.NotificationCreateRequest;
import kr.or.mari.dto.NotificationListResponse;
import kr.or.mari.dto.NotificationResponse;
import kr.or.mari.service.NotificationService;
import lombok.RequiredArgsConstructor;
/**
 * 알림 담당 REST API
 * 알림 생성(메시지 도착 / 초대 / 시스템 알림)
 * 로그인한 사용자의 알림 목록 조회
 * 특정 알림 읽음 처리
 * 전체 알림 읽음 처리
 */
@RestController @RequiredArgsConstructor @RequestMapping("/api/notify")
public class NotificationController {
	private final NotificationService notifySvc;
	
	// 알림 생성 API 
	// 채팅 메시지 도착, 초대, 시스템 이벤트 등 알림 생성
	// DB 저장 +  WebSocket을 통한 실시간 전송
	@PostMapping
	public ResponseEntity<NotificationResponse> createNotify(
			@RequestBody NotificationCreateRequest dto,
			HttpSession session){
		//로그인 여부 확인
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		// 세션의 사용자 ID를 DTO에 주입
		dto.setUserId(loginUser.getId());
		
		// 알림 생성 및 WebSocket 전송
		NotificationResponse response = notifySvc.create(dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		//201
	}
	
	//알림 목록 조회 API
	// 로그인한 사용자가 받은 알림 전체 목록 조회
	// isRead = Y/N 상태에 따라 읽음/안읽음 표시.
	@GetMapping
	public ResponseEntity<NotificationListResponse> getNotifications(HttpSession session){
		//로그인 사용자 검증
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401 인증 필요
		}
		// 알림 조회
		NotificationListResponse result = notifySvc.getUserNotifications(loginUser.getId());
		
		return ResponseEntity.ok(result);
	}
	
	//특정 알림 읽음 처리 API
	@PatchMapping("/{notifyId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable(name = "notifyId") Long notifyId, HttpSession session){
		//로그인 여부 확인
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401 인증 필요
		}
		// 특정 알림 읽음 처리
		notifySvc.markAsRead(loginUser.getId(), notifyId);
		
		return ResponseEntity.ok().build();
		//return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
	}
	
	//전체 알림 읽음 처리
	@PatchMapping("/read/all") // 리소스 일부 변경 패치
	public ResponseEntity<Void> markAllAsRead(HttpSession session){
		//로그인 여부 확인
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401 인증 필요
		}
		// 전체 알림 읽음 처리
		notifySvc.markAllAsRead(loginUser.getId());
		
		return ResponseEntity.ok().build();
		//이렇게 보내도 됨, 본문 없는 성공 응답
		//return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
	}

}
