package kr.or.mari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.or.mari.domain.ChatParticipant;

public interface ChatParticipantRepository 
			extends JpaRepository<ChatParticipant, Long> {
	//특정 채팅방에 해당 사용자가 이미 참여 중인지 여부 확인
	//오라클 버전이 11g여서 JPQL 사용함 //FETCH FIRST n ROWS ONLY 문법을 사용 못함
	//boolean existsByRoomIdAndUserId(Long roomId, Long userId);
	/*
	 내부적으로 JPA가 이 쿼리를 만듬
	 SELECT CASE WHEN COUNT(cp.id) > 0 THEN 1 ELSE 0 END
		FROM chat_participant cp
		WHERE cp.room_id = ? AND cp.user_id = ? 
	 */
	
	@Query("""
	        SELECT COUNT(cp) > 0
	        FROM ChatParticipant cp
	        WHERE cp.room.id = :roomId
	          AND cp.user.id = :userId
	    """)
	    boolean existsUserInRoom(@Param("roomId") Long roomId,
	                             @Param("userId") Long userId);
	
	
	Optional<ChatParticipant> findByRoomIdAndUserId(Long roomId, Long userId);
    List<ChatParticipant> findByRoomIdAndIsActive(Long roomId, String isActive);
    
    
    //특정 채팅방(roomId)과 특정 사용자(userId)에 해당하는 참여자 정보를 조회
    @Query("SELECT cp FROM ChatParticipant cp WHERE cp.room.id = :roomId AND cp.user.id = :userId")
    Optional<ChatParticipant> findByRoomAndUser(@Param("roomId") Long roomId, @Param("userId") Long userId);

    //읽음 처리 시, 사용자의 '마지막 읽은 메시지 ID'를 갱신하는 메서드.
	/*
	 @Modifying : JPA의 @Query는 기본적으로 SELECT(조회) 전용이므로,
	      UPDATE/DELETE/INSERT 같은 변경 쿼리를 수행하려면 반드시 이 어노테이션이 필요하다.
	      사용자가 채팅방을 열었을 때, 마지막으로 본 메시지를 기록.
	       이 값을 기준으로 안 읽은 메시지 수를 계산할 수 있음.
	*/
    @Modifying(clearAutomatically = true) //clearAutomatically 실제 DB의 UPDATE 결과 간 싱크가 어긋나는 걸 방지
    @Query("UPDATE ChatParticipant cp SET cp.lastReadMsgId = :lastMsgId WHERE cp.room.id = :roomId AND cp.user.id = :userId")
    int updateLastReadMessage(
        @Param("roomId") Long roomId,
        @Param("userId") Long userId,
        @Param("lastMsgId") Long lastMsgId
    );

    //사용자의 마지막 읽은 메시지 이후에 남아 있는 '안 읽은 메시지 수'를 계산하는 메서드.
    //@Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.room.id = :roomId AND m.id > :lastMsgId")
    @Query("""
    	    SELECT COUNT(m)
    	    FROM ChatMessage m
    	    WHERE m.room.id = :roomId
    	      AND m.id > COALESCE(:lastMsgId, 0)
    	""")
    int countUnreadMessages(@Param("roomId") Long roomId, @Param("lastMsgId") Long lastMsgId);
    
    //특정 채팅방에 참여 중인 모든 사용자 ID 조회
    //실시간 알림/브로드캐스트 시 수신자 목록으로 활용됨
    @Query("SELECT cp.user.id FROM ChatParticipant cp " +
            "WHERE cp.room.id = :roomId AND cp.isActive = 'Y'")
     List<Long> findActiveUserIdsByRoomId(Long roomId);
}
