package kr.or.mari.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Cleanup;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity @Getter @NoArgsConstructor @AllArgsConstructor
@Builder @EqualsAndHashCode @ToString
public class ChatParticipant { //채팅방 참가자
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participant_seq_gen")
	@SequenceGenerator(name = "participant_seq_gen", sequenceName = "SEQ_CHAT_PARTICIPANT", allocationSize = 1)
	private Long id;
	
	@ToString.Exclude
	//다대일 관계 : 여러명의 참가자가 하나의 방에 속함
	@ManyToOne(fetch = FetchType.LAZY) // 필요한 시점에만 조인
	@JoinColumn(name = "ROOM_ID")
	private ChatRoom room;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime joinedAt; //입장 시각
	
	@Column
	private Long lastReadMsgId;// 마지막으로 읽은 메시지 ID
	
	@Column(length = 1)
	private String isActive = "Y"; // 현재 참여 상태(기본값)
	//DB에서 기본값 'Y'지정 되어있어도 JPA에서 null넣으면 안먹기 때문에..
	
	@Enumerated(EnumType.STRING)  // Enum을 DB에 "문자열" 형태로 저장
	@Column(nullable = false,length = 20)
	private ChatRole role; //방 내 역할
	
}
