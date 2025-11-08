package kr.or.mari.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import kr.or.mari.domain.ChatParticipant;
import kr.or.mari.domain.ChatRole;
import kr.or.mari.domain.ChatRoom;
import kr.or.mari.domain.User;
import kr.or.mari.dto.ChatInviteRequest;
import kr.or.mari.dto.ChatInviteResponse;
import kr.or.mari.dto.LoginResponse;
import kr.or.mari.repository.ChatParticipantRepository;
import kr.or.mari.repository.ChatRoomRepository;
import kr.or.mari.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * 채팅방 초대 로직 서비스
 * - 이미 생성된 방에 초대
 * - 초대하려는 사용자가 이미 참여 중인지 확인
 * - 중복 초대 방지
 * - DB에서 채팅방/사용자 유효성 검증 후 참가자 정보(ChatParticipant) 추가
 * 
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChatInviteService {
	private final ChatRoomRepository chatRoomRepo;
    private final ChatParticipantRepository participantRepo;
    private final UserRepository userRepo;
    
    public ChatInviteResponse inviteUsers(ChatInviteRequest dto
    		,HttpSession session) {
        LoginResponse loginUser = (LoginResponse) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new SecurityException("로그인 정보가 없습니다.");
        }
        
    	//초대할 채팅방이 존재하는 지 확인
    	// 존재하지 않으면 예외 발생(트랜잭션 롤백)
    	ChatRoom room = chatRoomRepo.findById(dto.getRoomId())
    			.orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
    	
    	//초대한 사용자 존재여부 확인
    	User inviter = userRepo.findById(loginUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("초대한 사용자를 찾을 수 없습니다."));
    	
    	Set<Long> invitedIds = new HashSet<>();
        for (Long userId : dto.getInviteeIds()) {

            // 이미 참여 중인 사용자면 패스
            if (participantRepo.existsByRoomIdAndUserId(room.getId(), userId)) continue;

            // 초대 대상자 유효성 검사
            User invitee = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("초대 대상 사용자를 찾을 수 없습니다."));

            // 참가자 엔티티 생성
            ChatParticipant participant = ChatParticipant.builder()
                    .room(room)
                    .user(invitee)
                    .role(ChatRole.MEMBER)
                    .isActive("Y")
                    .build();

            participantRepo.save(participant);
            invitedIds.add(userId);
        }

        return ChatInviteResponse.builder()
                .roomId(room.getId())
                .invitedCount(invitedIds.size())
                .invitedUserIds(new ArrayList<>(invitedIds))
                .message(invitedIds.size() + "명이 초대되었습니다.")
                .build();
    } 
}
