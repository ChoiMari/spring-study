package kr.or.kosa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//메시지를 준게 user인지 AI인지 판단
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessageDto {
	private String sender; //user 또는 AI
	private String text;
}
