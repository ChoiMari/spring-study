package kr.or.mari.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅 메시지 응답 DTO
 * ----------------------------
 * 메시지가 저장되거나 WebSocket으로 전달될 때 반환.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatMessageResponse {
    private Long messageId;
    private Long roomId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime createdAt;
    private boolean isMine; // 클라이언트에서 구분용
}
