package kr.or.mari.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 사용자가 채팅방의 마지막으로 읽은 메시지를 갱신할 떄 사용
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatReadUpdateRequest {
	private Long roomId;      // 채팅방 ID
    private Long userId;      // 사용자 ID
    private Long lastMessageId; // 마지막으로 읽은 메시지 ID
}
