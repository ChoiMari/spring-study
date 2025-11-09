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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode @ToString @Getter
public class Notification {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq_gen")
	@SequenceGenerator(name = "notification_seq_gen", sequenceName = "SEQ_NOTIFICATION", allocationSize = 1)
	private Long id;

	@ToString.Exclude
	@JoinColumn(name = "USER_ID", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user; //알림 대상 사용자
	
	@Enumerated(EnumType.STRING) //Enum을 문자열로 저장
	@Column(nullable = false, name = "type", length = 50)
	private NotificationType type; // 알림 유형
	
	@Column(length = 200)
	private String title; //알림 제목
	
	@Column(length = 500)
	private String message; // 알림 본문 내용
	
	@Column(length = 1)
	private String isRead = "N"; // 읽음여부(기본값 N)
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt; // 알림 생성 시간
}
