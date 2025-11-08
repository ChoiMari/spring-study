package kr.or.mari.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DB USERS테이블과 매핑됨
 * Spring Boot + JPA(Hibernate)는 기본적으로 
 * 자바의 camelCase 필드명 ↔ DB의 snake_case 컬럼명을 자동으로 변환
 * JPA는 반드시 기본 생성자가 필요하다
 */
@Entity
@Table(name = "USERS") // DB 테이블명과 달라서 명시 필요
@Getter @NoArgsConstructor @AllArgsConstructor
@Builder @EqualsAndHashCode
public class User {
	@Id //PK지정
	//오라클 11g여서 SEQUENCE 기반으로 생성
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
	//생성된 값은 전략을 생성타입->시퀀스로 하고, 생성기 이름 user_seq_gen
	@SequenceGenerator(
	    name = "user_seq_gen",          // generator 이름(생성기 이름 지정, 맘대로 지어도 됨)
	    sequenceName = "SEQ_USER",      // DB에서 만든 시퀀스 이름
	    allocationSize = 1              // 시퀀스 1씩 증가
	)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 50)
	private String username;//nn, uk, VARCHAR2(50)
	
	@Column(nullable = false, length = 200)
	private String password; // BCrypt 암호화된 비밀번호
	
	@Column(length = 100)
	private String name;
	
	/**
	 * 가입 일자
	 * DB의 default sysdate값 사용
	 * 오라클의 DATE타입은 년월시분초까지 저장됨
	 * JPA가 insert 시 값을 직접 넣지 않게 설정
	 */
	@Column(insertable = false, updatable = false)
	//insert할 때도, update할 때도 JPA가 SQL에 포함시키지 않게 설정함
	//-> DB가 알아서 기본값을 채우게 두겠다
	private LocalDateTime regDate;
}
