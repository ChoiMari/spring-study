package kr.or.mari.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.mari.domain.User;
//@Repository를 명시적으로 붙이지 않아도 된다.
// JpaRepository를 상속받으면 스프링 데이터 JPA가 자동으로 Bean 등록을 해주기 때문
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
