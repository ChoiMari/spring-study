package kr.or.mari.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import kr.or.mari.domain.ChatParticipant;
import kr.or.mari.domain.ChatRole;
import kr.or.mari.domain.ChatRoom;
import kr.or.mari.domain.User;
import kr.or.mari.dto.ChatParticipantResponse;
import kr.or.mari.dto.ChatRoomCreateRequest;
import kr.or.mari.dto.ChatRoomResponse;
import kr.or.mari.repository.ChatParticipantRepository;
import kr.or.mari.repository.ChatRoomRepository;
import kr.or.mari.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * 채팅방 생성 및 조회 비즈니스 로직을 담당
 * public 메서드 전부 트랜잭션 처리
	- 방 생성
	- 내 방 목록
	- 방 나가기
	- 참여자 목록
 * 
 * createdAt은 DB DEFAULT(SYSDATE)로 설정되어 있으므로
 * 코드에서 set하지 않음 → JPA가 INSERT & update 시 컬럼을 제외함
 */
@Service @RequiredArgsConstructor
@Transactional //-> 클래스 위에 붙이면 이 클래스에 선언된 모든 public 메서드는 트랜잭션 처리한다는 뜻
//private메서드 또는 내부에서 자기자신(this) 호출 시 트랜잭션 적용 되지 않음//->Spring AOP 프록시 기반이여서
public class ChatRoomService {
	
	private final ChatRoomRepository chatRoomRepo;              
    private final ChatParticipantRepository participantRepo;  
    private final UserRepository userRepo;        
    private final EntityManager em;//DB insert 후 실제값(refresh)용

    //채팅방 생성
    /**
     * ChatRoom 엔티티 생성 및 저장
     * 생성자(방장) role를 OWNER로 CHAT_PARTICIPANT(참가자 테이블)에 등록
     * 초대 대상 사용자들을 MEMBER로 참가자 테이블에 추가
     * ChatRoomResponse으로 반환
     */
    public ChatRoomResponse createRoom(ChatRoomCreateRequest dto) {
    	// 채팅방 생성
    	ChatRoom room = ChatRoom.builder()
    	.roomName(dto.getRoomName())
    	.build();
    	
    	chatRoomRepo.save(room); //DB 저장
    	chatRoomRepo.flush(); // 즉시 DB insert 수행
    	//chatRoomRepo.saveAndFlush(room); // save + flush 동시 수행
    	//flush()는 트랜잭션을 유지한 채, 단지 영속성 컨텍스트의 변경 내용을 DB에 “즉시 반영”하는 동작
    	//commit;은 안하기 때문에 트랜잭션을 유지할 수 있음
    	em.refresh(room); //DB에 실제 insert된 row 값으로 엔티티 갱신
    	// createdAt = SYSDATE 반영됨
    	
    	//방장 정보 조회(없는 경우, 예외)
    	 User creator = userRepo.findById(dto.getCreatorId())
                 .orElseThrow(() -> new EntityNotFoundException("생성자 정보를 찾을 수 없습니다."));
    	 
    	//방장 참가자 등록(권한 OWNER)
    	 ChatParticipant owner = ChatParticipant.builder()
                 .room(room)
                 .user(creator)
                 .isActive("Y")
                 .role(ChatRole.OWNER)
                 .build();
         participantRepo.save(owner);
         
         //초대된 사용자들을 등록(MEMBER)
         Set<ChatParticipant> invited = new HashSet<>();
         // 순서 중요하지 않고, 초대 사용자 중복되면 안되므로, Set사용
         
         if(dto.getParticipantIds() != null && !dto.getParticipantIds().isEmpty()) {
        	 for(Long userId : dto.getParticipantIds()) {
        		 User invitedUser = userRepo.findById(userId)
        		 .orElseThrow(() -> new EntityNotFoundException("초대 대상 사용자를 찾을 수 없습니다."));
            	 
        		 ChatParticipant member = ChatParticipant.builder()
                         .room(room)
                         .user(invitedUser)
                         .isActive("Y") // 현재 방 참여 상태
                         .role(ChatRole.MEMBER) // 권한
                         .build();
        		 
        		 invited.add(member); // 중복이면 자동 무시됨
        	 }
        	 
        	 participantRepo.saveAll(invited); //엔티티 여러 개를 한꺼번에 저장
        	 //JPA가 최적화해서 한 트랜잭션 안에서 묶어서 처리함
         }
         
    	 
         return ChatRoomResponse.builder()
                 .roomId(room.getId())
                 .roomName(room.getRoomName())
                 .ownerId(creator.getId())
                 .participantCount(1 + invited.size())
                 .createdAt(room.getCreatedAt()) // DB가 SYSDATE 채워준 값이 조회 시점에 반영됨
                 .build();
    }
    
    //내가 속한 모든 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getRoomsByUser(Long userId) {
        return chatRoomRepo.findRoomsByUserId(userId);
    }
    
    // 방 나가기
    // 방장이 나가면 권한을 다른 참여자에게 위임함(자동)
    // 남은 참여자가 없으면 채팅방 자체를 제거함
    // 일반 참여자가 나갈 경우 (isActive='N')만 변경
    public void leaveRoom(Long roomId, Long userId) {
    	//userId는 로그인 세션에서 파싱함
    	 ChatParticipant participant = participantRepo.findByRoomIdAndUserId(roomId, userId)
                 .orElseThrow(() -> new EntityNotFoundException("참여자 정보를 찾을 수 없습니다."));
    	 
    	 // 방장인지 확인함
    	 if (participant.getRole() == ChatRole.OWNER) {
    	        // 방장일 경우  // 방의 남은 활성 참여자 조회 (본인 제외)
    	        List<ChatParticipant> others = participantRepo.findByRoomIdAndIsActive(roomId, "Y")
    	                .stream()
    	                .filter(p -> !p.getUser().getId().equals(userId))
    	                .toList();

    	        if (others.isEmpty()) {
    	            // 남은 사람이 없으면 방 삭제
    	            chatRoomRepo.deleteById(roomId);
    	            return;
    	        } else {
    	            // 남은 사람 중 첫 번째 사람에게 방장 권한 위임
    	            ChatParticipant newOwner = others.get(0);
    	            newOwner.changeRole(ChatRole.OWNER);
    	        }
			}

			// participant.setIsActive("N"); setter메서드는 사용하지 않는게 권장사항이라서 만들지 않음
			participant.deactivate(); // 도메인 메서드로 대체함
			// @Transactional 이 걸려 있으므로 DB UPDATE는 JPA의 더티 체킹(Dirty Checking, 변경 감지)에 의해 자동
			// 수행됨
			// @Transactional 덕분에 별도 save() 호출 불필요
			//// 메서드 종료 시점에 flush → SQL 자동 실행
    	
    }
    
    // 참여자 목록 조회
    @Transactional(readOnly = true) //readOnly=true (쓰기 불가, 성능 향상)
    public List<ChatParticipantResponse> getParticipants(Long roomId) {
    	//특정 채팅방에 현재 활성 상태(Y)인 사용자 목록을 조회
        List<ChatParticipant> list = participantRepo.findByRoomIdAndIsActive(roomId, "Y");
        
        //Lazy Loading으로 User 객체를 가져와 DTO로 변환
        return list.stream()
                .map((ChatParticipant p) -> ChatParticipantResponse.builder()
                        .userId(p.getUser().getId())          // 참가자 PK
                        .username(p.getUser().getUsername())  // 참가자 로그인 아이디
                        .role(p.getRole().name())             // 역할 (OWNER / MEMBER 등)
                        .name(p.getUser().getName())
                        .active(p.getIsActive())            // 참여 상태 (Y/N)
                        .build())
                .collect(Collectors.toList());
    }
    
    //채팅 방 삭제 - 방장만 삭제 가능
    // 방 삭제 시 관련 참여자, 메시지 모두 제거
    public void deleteRoom(Long roomId, Long userId) {
        //채팅방 존재 여부 확인
        ChatRoom room = chatRoomRepo.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        //요청 사용자가 이 방의 참가자인지 확인
        ChatParticipant participant = participantRepo.findByRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new EntityNotFoundException("참여자 정보를 찾을 수 없습니다."));

        //권한 체크 (방장만 삭제 가능)
        if (participant.getRole() != ChatRole.OWNER) {
            throw new IllegalStateException("방장만 채팅방을 삭제할 수 있습니다.");
        }

        //채팅방 삭제 (cascade로 메시지/참가자 자동 제거)
        chatRoomRepo.delete(room);
    }
    
}
