package kr.or.mari.dto;

import java.time.LocalDateTime;

import kr.or.mari.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 알림 응답 DTO
 * ----------------------------
 * 사용자가 받은 알림 목록을 조회할 때 사용.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class NotificationResponse {
    private Long id; //알림 고유 식별자(PK)
    private NotificationType type; //알림 유형
    private String title; //알림 제목
    private String message; //알림 본문
    private boolean isRead; //읽음 여부 상태
    private LocalDateTime createdAt; //알림 생성 시간
    //프론트에서는 시간 순으로 정렬하거나, "n분 전" 형태로 표시할 수 있음.

}

