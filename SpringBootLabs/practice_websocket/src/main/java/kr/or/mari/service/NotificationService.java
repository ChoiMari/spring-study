package kr.or.mari.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import kr.or.mari.domain.Notification;
import kr.or.mari.domain.User;
import kr.or.mari.dto.NotificationCreateRequest;
import kr.or.mari.dto.NotificationListResponse;
import kr.or.mari.dto.NotificationResponse;
import kr.or.mari.repository.NotificationRepository;
import kr.or.mari.repository.UserRepository;
import lombok.RequiredArgsConstructor;
/**
 * 알림(Notification) 관련 핵심 비즈니스 로직 담당 서비스 클래스.
 * 
 * 주요 기능:
 *  1. 알림 저장 및 생성
 *  2. 사용자별 알림 목록 조회
 *  3. 개별 알림 읽음 처리
 *  4. 전체 알림 읽음 처리
 *  5. WebSocket 기반 실시간 푸시 전송
 */
@Service @RequiredArgsConstructor @Transactional
public class NotificationService {
    private final NotificationRepository notifyRepo;
    private final UserRepository userRepo;
    private final EntityManager em; // DB createdAt 동기화용
    private final SimpMessagingTemplate messagingTemplate;//WebSocket 메시지 전송 템플릿
    
    //알림 생성 + 실시간 푸시 전송
    /**
     * WebSocket 전송 경로 : 
     *  /topic/notify/{userId}
     *  	-> 클라이언트가 이 토픽을 구독하고 있으면 실시간으로 알림 수신
     */
    public NotificationResponse create(NotificationCreateRequest dto) {
    	//사용자 검증
    	User user = userRepo.findById(dto.getUserId())
    			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    	
    	//알림 엔티티 생성
    	Notification entity = Notification.builder()
						    	.user(user)
						    	.type(dto.getType())
						    	.title(dto.getTitle())
						    	.message(dto.getMessage())
						    	.build();
    	//DB 저장 후 flush(알림 id 사용해야되서, createdAt값도 응답에 넣어주려면 필요함)
    	//flush() : 즉시 DB에 반영되게 하는 기능(commit은 안함, 트랜젝션 유지)
    	notifyRepo.save(entity);
    	notifyRepo.flush(); //insert 즉시 실행(created_at default sysdate 기록) 
    	em.refresh(entity); //DB값으로 entity동기화(createdAt 갱신됨)
    	
    	//WebSocket을 통한 실시간 push 전송
    	NotificationResponse payload = NotificationResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .isRead(false)
                .createdAt(entity.getCreatedAt()) // Oracle SYSDATE 기반 생성시각
                .build();

    	// /topic/notify/{userId} 구독 중인 클라이언트에게 payload(NotificationResponse 객체) 실시간 송신
    	messagingTemplate.convertAndSend("/topic/notify/" + user.getId(), payload);
    	
    	return payload;
    }
    
    //사용자별 알림 목록 조회
    @Transactional(readOnly = true)
    public NotificationListResponse getUserNotifications(Long userId) {
    	//DB에서 해당 사용자의 모든 알림 조회(최신순 정렬)
    	List<NotificationResponse> notifications = notifyRepo.findAllByUserId(userId)
    			.stream()
    			.map(n -> NotificationResponse.builder()
    					.id(n.getId())
    					.type(n.getType())
    					.title(n.getTitle())
    					.message(n.getMessage())
    					.isRead("Y".equals(n.getIsRead())) //프론트에는 boolean 타입으로 보냄
    					.createdAt(n.getCreatedAt())
    					.build())
    			.collect(Collectors.toList()); //Stream -> List 변환
    	
    	return NotificationListResponse.builder()
                .userId(userId)
                .notifications(notifications)
                .build();
    }
    
    
    //특정 알림 읽음 처리
    // 사용자가 특정 알림을 클릭 했을 때 호출됨
    // 해당 알림의 isRead 값을 'Y'로 변경
    public void markAsRead(Long userId, Long notifyId) {
        notifyRepo.markAsRead(userId, notifyId);
    }
    
    // 전체 알림 읽음 처리
    // 사용자가 모두 읽음 버튼 클릭시 호출
    // 해당 사용자의 모든 알림을 한꺼번에 읽음 상태로 변경
    public void markAllAsRead(Long userId) {
        notifyRepo.markAllAsRead(userId);
    }

}
