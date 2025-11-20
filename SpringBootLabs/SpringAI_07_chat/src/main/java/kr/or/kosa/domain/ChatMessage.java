package kr.or.kosa.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

//테이블과 동일하게 테이블 만들고 설정
@Entity @Getter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class ChatMessage {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id; //식별값(PK)

	private String userId; // 사용자 ID
	
	@Lob
	private String content; // 메시지
	private boolean isUser; // true : 사용자, false : AI
	// 누구의 답변인지 판별
	
	private LocalDateTime createdAt; // 생성일
	
	public ChatMessage(String userId, String content, boolean isUser) {
		super();
		this.userId = userId;
		this.content = content;
		this.isUser = isUser;
		this.createdAt = LocalDateTime.now(); //서버의 현재시간
	}
}
