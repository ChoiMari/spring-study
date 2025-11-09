package kr.or.mari.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
//알림 읽음에 사용됨
@Data
@AllArgsConstructor
@NoArgsConstructor @Builder @EqualsAndHashCode
public class NotifyEvent {
    private String type;     // 이벤트 타입 ("READ", "READ_ALL", "NEW" 등)
    private Long notifyId;   // 단일 알림일 경우 알림 ID
}
