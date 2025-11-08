package kr.or.mari.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.mari.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
