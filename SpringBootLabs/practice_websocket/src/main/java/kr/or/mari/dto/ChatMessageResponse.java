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
    private Long messageId; // 메시지 식별자(PK)
    private Long roomId; // 해당 메시지가 속한 채팅방 ID
    private Long senderId; // 메시지를 보낸 사용자 ID
    private String senderName; // 메시지를 보낸 사용자의 이름(UI 표시용)
    private String content; // 실제 채팅 내용
    private LocalDateTime createdAt;// 메시지 전송(저장) 시각
    
    private boolean isMine; // 클라이언트에서 구분용
    // 프론트 단에서 내 메시지 구분용 
    // → 현재 로그인한 사용자 ID와 senderId가 동일하면 true
    // DB에는 저장하지 않고 클라이언트 표시용 데이터로만 사용
    //보통 프론트엔드에서 "오른쪽 정렬" 메시지인지 판단하는 용도
    // 내 자신의 메시지는 오른쪽에 정렬하기 때문에..
}
// 활용
//-> 메시지 저장 직후 서버에서 반환함
// 웹소켓에서 브로드캐스트 시에 다른 참여자들에게 실시간 전송
