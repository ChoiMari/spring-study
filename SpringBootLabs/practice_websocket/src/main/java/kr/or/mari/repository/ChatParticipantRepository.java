package kr.or.mari.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.mari.domain.ChatParticipant;

public interface ChatParticipantRepository 
			extends JpaRepository<ChatParticipant, Long> {
	//특정 채팅방에 해당 사용자가 이미 참여 중인지 여부 확인
	boolean existsByRoomIdAndUserId(Long roomId, Long userId);
	/*
	 내부적으로 JPA가 이 쿼리를 만듬
	 SELECT CASE WHEN COUNT(cp.id) > 0 THEN 1 ELSE 0 END
		FROM chat_participant cp
		WHERE cp.room_id = ? AND cp.user_id = ? 
	 */
	
	Optional<ChatParticipant> findByRoomIdAndUserId(Long roomId, Long userId);
    List<ChatParticipant> findByRoomIdAndIsActive(Long roomId, String isActive);
}
