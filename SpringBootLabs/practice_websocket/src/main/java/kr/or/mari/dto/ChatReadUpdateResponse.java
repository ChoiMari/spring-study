package kr.or.mari.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 읽음 처리 후 갱신된 상태 반환.
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatReadUpdateResponse {
	private Long roomId;//채팅방의 고유 식별자
    private Long userId; //읽음 처리를 수행한 사용자 ID
    private Long lastReadMsgId; //사용자가 마지막으로 읽은 메시지 ID
    private int unreadCount; // 안 읽은 메시지 수
}
