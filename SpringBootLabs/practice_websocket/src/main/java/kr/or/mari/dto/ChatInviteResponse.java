package kr.or.mari.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 채팅방 초대 응답 DTO
 * ----------------------------
 * 초대 성공 후 초대된 사용자 목록 반환.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class ChatInviteResponse {
    private Long roomId;
    int invitedCount;
    private List<Long> invitedUserIds;
    private String message; // "3명이 초대되었습니다." 등
}
