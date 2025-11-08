package kr.or.mari.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 알림 목록 조회용 DTO
 * ----------------------------
 * 사용자 알림함에 표시할 전체 목록 반환용.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class NotificationListResponse {
    private Long userId;
    private List<NotificationResponse> notifications;
}
