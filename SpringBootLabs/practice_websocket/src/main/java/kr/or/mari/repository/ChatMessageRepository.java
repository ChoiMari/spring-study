package kr.or.mari.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.or.mari.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	
	// 특정 방의 최근 메시지 목록(최신순)
	//오라클 11g여서 fetch 못씀, ROWNUM 사용함
//	@Query("""
//		       SELECT m
//		       FROM ChatMessage m
//		       WHERE m.room.id = :roomId
//		       ORDER BY m.id DESC
//		       """)
//        List<ChatMessage> findRecentMessages(Long roomId);
	
    @Query(value = """
            SELECT *
            FROM (
                SELECT m.*
                FROM CHAT_MESSAGE m
                WHERE m.ROOM_ID = :roomId
                ORDER BY m.ID DESC
            )
            WHERE ROWNUM <= 30
            """, nativeQuery = true)
    List<ChatMessage> findRecentMessages(@Param("roomId") Long roomId);
	
}
