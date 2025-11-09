package kr.or.mari.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.or.mari.domain.ChatParticipant;
import kr.or.mari.dto.ChatParticipantResponse;
import kr.or.mari.dto.ChatReadUpdateRequest;
import kr.or.mari.dto.ChatReadUpdateResponse;
import kr.or.mari.repository.ChatParticipantRepository;
import lombok.RequiredArgsConstructor;

// 사용자가 특정 방의 마지막 읽은 메시지를 갱신하고
// 안 읽은 메시지 수를 계산
@Service @Transactional @RequiredArgsConstructor
public class ChatReadService {
	private final ChatParticipantRepository chatPartRepo;
	
	// 특정 방에서 사용자의 마지막 읽은 메시지 ID를 갱신하고 
	// 남은 안 읽은 메시지 수를 반환
	public ChatReadUpdateResponse updateLastRead(ChatReadUpdateRequest dto) {
		//방 사용자 존재 확인
		ChatParticipant particpant = chatPartRepo.findByRoomAndUser(dto.getRoomId(), dto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("해당 사용자는 방에 참여하고 있지 않습니다."));
		
		//읽음 정보 갱신
		chatPartRepo.updateLastReadMessage(dto.getRoomId(), dto.getUserId(), dto.getLastMessageId());
		
		//안 읽은 메시지 수 계산
		int unreadCount = chatPartRepo.countUnreadMessages(dto.getRoomId(), dto.getLastMessageId());
		
		// 결과 반환
		return ChatReadUpdateResponse.builder()
				.roomId(dto.getRoomId())
				.userId(dto.getUserId())
				.lastReadMsgId(dto.getLastMessageId())
				.unreadCount(unreadCount)
				.build();
	}
}
