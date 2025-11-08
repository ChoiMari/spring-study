package kr.or.mari.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.or.mari.dto.ChatInviteRequest;
import kr.or.mari.dto.ChatInviteResponse;
import kr.or.mari.dto.LoginResponse;
import kr.or.mari.service.ChatInviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController @RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatInviteController {
	
	private final ChatInviteService chatInviteSvc;
	
	// 이미 존재하는 채팅방에 사용자 초대
	/* 요청 예시
	 * {
     *   "roomId": 3,
     *   "inviteeIds": [2, 5, 7]
     * }
     * 
     * 응답 예시:
     * {
     *   "roomId": 3,
     *   "invitedCount": 2,
     *   "invitedUserIds": [5,7],
     *   "message": "2명이 초대되었습니다."
     * }
	 */
	@PostMapping("/{roomId}/invite")
	public ResponseEntity<ChatInviteResponse> inviteUsers(
			@PathVariable("roomId") Long roomId,
			@RequestBody ChatInviteRequest dto,
			HttpSession session){
		
		// 세션에서 로그인 사용자 확인
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        dto.setRoomId(roomId);//   // PathVariable로 받은 roomId를 DTO에도 세팅
       
        ChatInviteResponse response = chatInviteSvc.inviteUsers(dto, session);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        //201
		
	}
	
	
}
