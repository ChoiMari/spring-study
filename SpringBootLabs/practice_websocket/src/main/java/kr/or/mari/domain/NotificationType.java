package kr.or.mari.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 알림 유형
@Getter
@RequiredArgsConstructor
public enum NotificationType {
    CHAT("채팅 메시지"),
    INVITE("방 초대"),
    SYSTEM("시스템 알림");

    private final String description;
}
