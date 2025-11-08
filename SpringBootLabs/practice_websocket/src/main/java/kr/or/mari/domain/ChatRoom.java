package kr.or.mari.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
@Builder @EqualsAndHashCode
public class ChatRoom {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq_gen")
	@SequenceGenerator(name = "room_seq_gen", sequenceName = "SEQ_CHAT_ROOM", allocationSize = 1)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String roomName;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt; //채팅방 생성 시간(DB dafualt sysdate)
	
	@ToString.Exclude
	// 관계 컬럼 - DB에는 없음
    // 양방향 매핑: 한 채팅방에는 여러 참가자가 존재
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipant> participants;

	@ToString.Exclude
    // 양방향 매핑: 한 채팅방에는 여러 메시지가 존재
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;
}
