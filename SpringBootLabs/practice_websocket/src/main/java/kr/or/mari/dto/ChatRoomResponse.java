package kr.or.mari.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅방 생성/조회 응답 DTO
 * ----------------------------
 * 방 생성 후 또는 방 목록 조회 시 반환.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatRoomResponse {
    private Long roomId;              // 채팅방 PK
    private String roomName;          // 채팅방 이름
    private Long ownerId;             // 방장 ID
    private int participantCount;     // 현재 참여자 수
    private LocalDateTime createdAt;  // 생성 시간
    
    public ChatRoomResponse(Long roomId, String roomName, Long ownerId, Long participantCount, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.ownerId = ownerId;
        this.participantCount = participantCount.intValue(); // Long → int 변환
        this.createdAt = createdAt;
    }
}
