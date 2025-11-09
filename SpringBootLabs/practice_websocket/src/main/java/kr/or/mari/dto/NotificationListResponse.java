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
 * 사용자 알림 전체 목록을 한 번에 묶어서 반환
 * 
 * - REST API에서 /api/notifications 요청 시 반환 타입으로 사용됨.
 * 
 * 예시 응답 구조:
 * {
 *   "userId": 5,
 *   "notifications": [
 *       { "id": 10, "type": "MESSAGE", "title": "새 메시지 도착", ... },
 *       { "id": 11, "type": "INVITE",  "title": "초대가 도착했습니다", ... }
 *   ]
 * }
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class NotificationListResponse {
    private Long userId; //알림을 조회한 사용자 ID
    private List<NotificationResponse> notifications;//알림 목록 데이터
}
