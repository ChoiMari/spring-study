package kr.or.mari.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.or.mari.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	// 사용자 별 알림 전체 조회
	// 특정 사용자가 받은 모든 알림을 최신순으로 조회
	@Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findAllByUserId(@Param("userId") Long userId);

	/*
	 @Modifying이 붙은 메서드는 데이터를 변경하므로,
		반드시 Service 계층에서 @Transactional로 감싸줘야 커밋이 반영 
	 */
	// 개별 알림 읽음 처리
	// 사용자가 특정 알림을 클릭했을 때, 해당 알림의 읽음 상태를 Y로 업데이트
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = 'Y' WHERE n.user.id = :userId AND n.id = :notifyId")
    int markAsRead(@Param("userId") Long userId, @Param("notifyId") Long notifyId);

    // 사용자 알림 전체 읽음 처리
    // 모두 읽음 버튼 클릭시 사용함, 해당 사용자의 모든 알림의 isRead 상태를 'Y'로 일괄 변경함.
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = 'Y' WHERE n.user.id = :userId")
    int markAllAsRead(@Param("userId") Long userId);
}
