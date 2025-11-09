package kr.or.mari.dto;

import kr.or.mari.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 알림 생성 요청 DTO
 * ----------------------------
 * 새 채팅 메시지나 시스템 이벤트 발생 시 사용.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class NotificationCreateRequest {
    private Long userId;      // 알림 대상 사용자
    private NotificationType type;    // CHAT / SYSTEM / INVITE
    private String title;     // 알림 제목
    private String message;   // 알림 본문 내용
}
