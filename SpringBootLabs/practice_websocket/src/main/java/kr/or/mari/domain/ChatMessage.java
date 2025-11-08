package kr.or.mari.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
// 채팅 메시지 엔터티
@Entity @NoArgsConstructor @AllArgsConstructor
@Getter @EqualsAndHashCode @Builder @ToString
public class ChatMessage {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq_gen")
	@SequenceGenerator(name = "message_seq_gen", sequenceName = "SEQ_CHAT_MESSAGE", allocationSize = 1)
	private Long id;
	
	@ToString.Exclude //순환 참조 방지 - Lombok이 toString() 만들 때 이 필드는 빼줌
	// 여러 메시지가 하나의 방에 속함
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROOM_ID", nullable =  false)
	private ChatRoom room;
	
	@ToString.Exclude //순환 참조 방지
	//여러 메시지가 하나의 사용자에 의해 전송됨
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	
	@Column(nullable = false, length = 500)
	private String content;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt;//DB default sysdate

}
