package kr.or.mari.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅방 참여자 조회 DTO
 * ----------------------------
 * 방에 참여 중인 사용자 목록을 보여줄 때 사용.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatParticipantResponse {
    private Long userId;
    private String username;
    private String name;
    private String role;      // OWNER / MEMBER
    private String active;   // 현재 방 참여 중인지
}
