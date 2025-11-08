package kr.or.mari.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.or.mari.domain.ChatRoom;
import kr.or.mari.dto.ChatRoomResponse;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	//특정 사용자(userId)가 속한 채팅방 목록 조회
	/**
	 * participant 테이블과 join하여 
	 * 사용자가 참여한 채팅방의 ID, 이름, 생성일을 반환함.
	 */
	@Query("""
		    SELECT new kr.or.mari.dto.ChatRoomResponse(
		        r.id,
		        r.roomName,
		        u.id,
		        COUNT(p2.id),
		        r.createdAt
		    )
		    FROM ChatRoom r
		    JOIN r.participants p
		    JOIN p.user u
		    JOIN r.participants p2
		    WHERE u.id = :userId
		    GROUP BY r.id, r.roomName, u.id, r.createdAt
		    ORDER BY r.createdAt DESC
		""")
		List<ChatRoomResponse> findRoomsByUserId(@Param("userId") Long userId);

	
}
