package kr.or.mari.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅방 목록 조회용 DTO
 * ----------------------------
 * 사용자가 속한 모든 채팅방의 간단 요약 정보 반환.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatRoomListResponse {
    private Long roomId;
    private String roomName;
    private int unreadCount;      // 안 읽은 메시지 수
    private String lastMessage;   // 마지막 메시지 미리보기
    private String lastMessageTime; // 마지막 메시지 시각
}
