package kr.or.mari.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅 메시지 전송 요청 DTO
 * ----------------------------
 * 사용자가 특정 채팅방에 메시지를 보낼 때 사용.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatMessageRequest {
    private Long roomId;     // 채팅방 ID
    private Long senderId;   // 보낸 사용자 ID
    private String content;  // 메시지 내용
}
