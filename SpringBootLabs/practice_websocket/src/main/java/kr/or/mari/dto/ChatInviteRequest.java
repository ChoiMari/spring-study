package kr.or.mari.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅방 초대 요청 DTO
 * ----------------------------
 * 기존 채팅방에 새로운 사용자를 초대할 때 사용.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatInviteRequest {
    private Long roomId;            // 초대할 방 ID
    //private Long inviterId;         // 초대한 사람 (보낸 사람) - 서버에서 세션에서 꺼내와서 필요없음
    private Set<Long> inviteeIds;   // 초대할 사용자 ID 목록
}
