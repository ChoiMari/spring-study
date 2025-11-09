package kr.or.mari.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;
import kr.or.mari.domain.User;
import kr.or.mari.dto.ChatParticipantResponse;
import kr.or.mari.dto.ChatRoomCreateRequest;
import kr.or.mari.dto.ChatRoomResponse;
import kr.or.mari.dto.LoginResponse;
import kr.or.mari.service.ChatRoomService;
import lombok.RequiredArgsConstructor;

/**
 * 채팅방 관련 API
 * 	- 채팅방 생성
 * 	- 자신이 속한 모든 채팅방 목록 조회
 *  - 채팅방 나가기
 *  - 특정 방의 참여자 목록 조회
 *  
 *  모든 요청은 로그인 세션(HttpSession)에 저장된 사용자 정보를 바탕으로 처리
 */
@RestController @RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
public class ChatRoomController {
	private final ChatRoomService chatRoomSvc;
	
	/**
	 * 채팅방 생성 API
	 * 요청 JSON 예시
     * {
     *   "roomName": "팀 프로젝트 방", //생성할 채팅방 이름
     *   "participantIds": [2, 3, 4] // 초대할 사용자 ID
     * }
     * 
     * 방장 ID(creatorId)는 로그인된 사용자의 세션에서 자동 주입함
	 */
	@PostMapping
	public ResponseEntity<ChatRoomResponse> createRoom(
			@RequestBody ChatRoomCreateRequest dto,
			HttpSession session){
		
		// 세션에서 로그인 사용자 정보 확인
		//session.setAttribute("loginUser", loginUser); 로그인 시에 저장해둠
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
		//반환 타입은 Object
		
		// 로그인 정보가 없으면 401 Unauthorized 반환(인증이 필요하거나 인증 정보가 잘못됨)
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        //DTO에 현재 로그인 사용자의 ID 주입 (방장으로 설정)
        dto.setCreatorId(loginUser.getId());
        
        //채팅방 생성 서비스 호출
        ChatRoomResponse response = chatRoomSvc.createRoom(dto);
        
        //HTTP 201 Created + 생성된 방 정보 응답
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
	}
	
	/**
	 * 현재 로그인 사용자가 속한 모든 채팅방 목록 조회
	 * 응답 예시:
     * [
     *   {
     *     "roomId": 1,
     *     "roomName": "개발팀 회의방",
     *     "userId": 5,
     *     "participantCount": 2,
     *     "createdAt": "2025-11-08T10:30:00"
     *   },
     *   {
     *     "roomId": 2,
     *     "roomName": "프로젝트 A",
     *     "userId": 5,
     *     "participantCount": 3,
     *     "createdAt": "2025-11-08T12:00:00"
     *   }
     * ]
	 */
	@GetMapping
	public ResponseEntity<List<ChatRoomResponse>> myRooms(
			HttpSession session){
		//세션에서 로그인 사용자 정보 확인
		LoginResponse loginUser = (LoginResponse)session.getAttribute("loginUser");
		
		// 로그인 안되어 있으면 401 Unauthorized 반환
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        //Service 호출하여 로그인 사용자가 속한 채팅방 목록 조회
        List<ChatRoomResponse> rooms = chatRoomSvc.getRoomsByUser(loginUser.getId());
        
		
        return ResponseEntity.ok(rooms);
	}
	
	// 채팅방 나가기
	@PostMapping("/{roomId}/leave")
	public ResponseEntity<String> leaveRoom(
			@PathVariable(name = "roomId") Long roomId, HttpSession session){
		//로그인 여부 확인
		// 세션에 로그인 사용자 정보가 없으면 인증 실패(401)
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
        //서비스 계층 호출
        chatRoomSvc.leaveRoom(roomId, loginUser.getId());
        return ResponseEntity.ok("채팅방을 나갔습니다.");
	}
	
	// 채팅방 참여자 목록 보기
	/**
	 * 클라이언트가 특정 채팅방의 참여자 정보를 조회할 때 사용.
	 예시
	  * 응답 데이터:
		 *  - List<ChatParticipantResponse> 형태로 반환
		 *    [
		 *      {
		 *        "userId": 2,
		 *        "username": "mari",
		 *        "role": "MEMBER",
		 *        "active": "Y",
		 *      },
		 *      ...
		 *    ]
	 */
	@GetMapping("/{roomId}/participants")
	public ResponseEntity<List<ChatParticipantResponse>> getParticipants(
	        @PathVariable(name = "roomId") Long roomId,
	        HttpSession session) {
		//로그인 사용자 세션 검증
		LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
		if (loginUser == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		// 주어진 roomId에 대해 현재 참여중(isActive='Y')인 참여자 목록 조회
		List<ChatParticipantResponse> participants = chatRoomSvc.getParticipants(roomId);
		return ResponseEntity.ok(participants);
	}
	
	
	
	// 채팅방 없애기 API
	@DeleteMapping("/{roomId}")
	public ResponseEntity<Void> deleteRoom(
			@PathVariable(name = "roomId")Long roomId, HttpSession session){
		//세션에서 로그인 사용자 확인
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
		//삭제 서비스 호출 //-> JPA Entity에서 cascade설정
        chatRoomSvc.deleteRoom(roomId, loginUser.getId());
		// 삭제 성공시 204 - 요청은 성공 했지만, 서버가 응답 본문을 돌려줄 내용 없다
		// No content
        return ResponseEntity.noContent().build();
	}
	
	//모든 채팅방 목록 보기
	@GetMapping("/list/all")
	public ResponseEntity<List<ChatRoomResponse>> listAllRooms(HttpSession session) {
	    LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    List<ChatRoomResponse> rooms = chatRoomSvc.getAllRooms(loginUser.getId());
	    return ResponseEntity.ok(rooms);
	}
	
	//채팅방 참여하기
	@PostMapping("/{roomId}/join")
	public ResponseEntity<Void> joinRoom(@PathVariable(name = "roomId") Long roomId, HttpSession session) {
		//세션에서 로그인 사용자 확인
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
	    chatRoomSvc.joinRoom(roomId, loginUser.getId());
	    return ResponseEntity.ok().build();
	}

	
}
