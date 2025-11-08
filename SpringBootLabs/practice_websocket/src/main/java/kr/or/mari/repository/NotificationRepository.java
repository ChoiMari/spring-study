package kr.or.mari.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.mari.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {}
