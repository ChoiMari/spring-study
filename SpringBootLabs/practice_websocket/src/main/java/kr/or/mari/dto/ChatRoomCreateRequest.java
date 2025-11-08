package kr.or.mari.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅방 생성 요청 DTO
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatRoomCreateRequest {

	private String roomName; // 생성할 채팅방 이름
    private Long creatorId;           // 방장 ID
    private Set<Long> participantIds; // 초대할 사용자 ID 목록
}
