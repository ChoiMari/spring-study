package kr.or.mari.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import kr.or.mari.domain.ChatMessage;
import kr.or.mari.domain.ChatRoom;
import kr.or.mari.domain.NotificationType;
import kr.or.mari.domain.User;
import kr.or.mari.dto.ChatMessageRequest;
import kr.or.mari.dto.ChatMessageResponse;
import kr.or.mari.dto.NotificationCreateRequest;
import kr.or.mari.repository.ChatMessageRepository;
import kr.or.mari.repository.ChatParticipantRepository;
import kr.or.mari.repository.ChatRoomRepository;
import kr.or.mari.repository.UserRepository;
import lombok.RequiredArgsConstructor;
/**
 * 채팅 메시지 저장 및 조회 
 * - 채팅방 유효성
 * - 보내는 사람 검증
 * - DB 저장 후 DTO 변환 반환
 * - 최근 메시지 조회 기능
 * 
 * 메시지 알림 트리거 (다른 참가자 대상)
 */
@Service @RequiredArgsConstructor @Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMsgRepo;
    private final ChatRoomRepository chatRoomRepo;
    private final UserRepository userRepo;
    private final EntityManager em; // refresh() 호출용
    
    private final ChatParticipantRepository participantRepo;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    
    //메시지 저장 + 브로드캐스트 + 알림 트리거
    /**
     * 1. 메시지를 DB에 저장
     * 2. STOMP 브로커를 통해 구독 중인 방 참여자에게 실시간 전송
     * 3. 본인 외 모든 방 참여자에게 Notification 생성 + 푸시
     */
    public ChatMessageResponse saveMessage(ChatMessageRequest dto) {
    	// 채팅방, 발신자 검증
    	ChatRoom room = chatRoomRepo.findById(dto.getRoomId())
    		.orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
    	User sender = userRepo.findById(dto.getSenderId())
    		.orElseThrow(() -> new EntityNotFoundException("보낸 사용자를 찾을 수 없습니다."));
    	
    	//메시지 엔티티 생성 및 DB저장
        ChatMessage entity = ChatMessage.builder()
                .room(room)
                .user(sender)
                .content(dto.getContent())
                .build();

        chatMsgRepo.save(entity);
        chatMsgRepo.flush();  // 즉시 DB INSERT
        em.refresh(entity);   // Oracle SYSDATE 반영(createdAt)
    	
    	//응답 dto 생성
        ChatMessageResponse response = ChatMessageResponse.builder()
                .messageId(entity.getId())
                .roomId(room.getId())
                .senderId(sender.getId())
                .senderName(sender.getName())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                //.isMine(true)
                .build();
    	//실시간 브로드캐스트(같은 방 참여자 전체)
        messagingTemplate.convertAndSend("/topic/chat/" + room.getId(), response);
        
    	//메시지 알림 트리거(본인 제외)
        List<Long> participants = participantRepo.findActiveUserIdsByRoomId(room.getId());
        for (Long userId : participants) {
            if (!userId.equals(sender.getId())) {
                notificationService.create(NotificationCreateRequest.builder()
                        .userId(userId)
                        .type(NotificationType.CHAT)  
                        .title(room.getRoomName())
                        .message(sender.getName() + ": " + dto.getContent())
                        .build());
            }
        }
        return response;
    }
    
    /*
    //WebSocket으로 실시간 전송 하기 전, 
    // DB에 안전하게 저장하고 DTO로 변환
    public ChatMessageResponse saveMessage(ChatMessageRequest dto) {
    	//채팅방 검증
    	ChatRoom room = chatRoomRepo.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
    	//보내는 사용자 검증
    	 User sender = userRepo.findById(dto.getSenderId())
                 .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    	// 메시지 엔티티 생성 및 저장
    	 ChatMessage entity = ChatMessage.builder()
                 .room(room)
                 .user(sender)             // ChatMessage 엔티티 필드명에 맞춰야 함
                 .content(dto.getContent())
                 .build();
    	 chatMsgRepo.save(entity); // DB insert
    	 //createdAt(entity.getCreatedAt()) null 문제 때문에 사용함
    	 chatMsgRepo.flush(); //실제 insert 쿼리 즉시 실행
    	 em.refresh(entity); // DB 값(특히 sysdate)으로 엔티티 동기화
    	 
    	// 응답 DTO로 변환한 뒤 프론트로 응답 보냄
    	 return ChatMessageResponse.builder()
                 .messageId(entity.getId())
                 .roomId(room.getId())
                 .senderId(sender.getId())
                 .senderName(sender.getName())
                 .content(entity.getContent())
                 .createdAt(entity.getCreatedAt())
                 .isMine(true) // 내가 보낸 메시지
                 .build();
    }
    */
    
    // 최근 메시지 목록 조회(최대 30개)
    /**
     * 지정된 채팅방에대해 가장 최근의 메시지를 조회함
     * 조회된  ChatMessage 엔티티를 ChatMessageResponse DTO로 변환하여 반환.
     * 메시지는 최신순 정렬로 최대 30개로 설정
     * 트랜잭션은 읽기 전용으로 설정하여 성능 최적화
     */
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getRecentMessages(Long roomId){
    	//DB에서 최신 30개 메시지 조회 (id DESC)
    	List<ChatMessage> messages = chatMsgRepo.findRecentMessages(roomId);
        return messages.stream()
                .map((ChatMessage m) -> ChatMessageResponse.builder()
                        .messageId(m.getId())                 // 메시지 PK
                        .roomId(m.getRoom().getId())          // 채팅방 ID
                        .senderId(m.getUser().getId())        // 보낸 사용자 ID (★user필드!)
                        .senderName(m.getUser().getName())    // 보낸 사용자 이름
                        .content(m.getContent())              // 메시지 본문
                        .createdAt(m.getCreatedAt())          // 생성 시각 (DB default sysdate)
                        //.isMine(false)                        // 기본 false, 프론트에서 구분 처리
                        .build())
                .collect(Collectors.toList());
    }
    
}
