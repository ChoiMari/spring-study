package kr.or.kosa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.or.kosa.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{
	//자동으로 CRUD 메서드 
	
	/*
	    find
		데이터를 조회한다는 의미
		select * from ... 와 동일 의미
		
		2) By
		조건절(WHERE)을 시작하는 키워드
		
		3) UserId
		WHERE 절의 컬럼 이름
		→ where user_id = ?
		
		4) OrderBy
		ORDER BY 절 시작
		
		5) CreatedAt
		정렬 기준 컬럼
		
		6) Asc
		오름차순 정렬
		(Asc 또는 Desc) 

	 */
	
	//쿼리 메서드
	List<ChatMessage> findByUserIdOrderByCreatedAtAsc(String userId);
	
	//JPQL - 문장 직접 사용
	//@Query
	
}
