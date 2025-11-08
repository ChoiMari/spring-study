package kr.or.mari.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.mari.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {}
