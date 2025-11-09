package kr.or.mari.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.mari.domain.User;
import kr.or.mari.dto.LoginRequest;
import kr.or.mari.dto.LoginResponse;
import kr.or.mari.dto.RegisterRequest;
import kr.or.mari.dto.RegisterResponse;
import kr.or.mari.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class UserService {
	private final PasswordEncoder encoder; //config파일에서 bean으로 등록해야 주입 가능
	private final UserRepository userRepo;
	
	//중복검사
	public boolean existsByUsername(String username) {
	    return userRepo.existsByUsername(username);
	}
	
	// 회원가입
    public RegisterResponse register(RegisterRequest dto) {
    	//비밀번호 암호화
        String encodedPw = encoder.encode(dto.getPassword());

        //dto -> 엔티티 변환
        User user = User.builder()
                .username(dto.getUsername())
                .password(encodedPw)
                .name(dto.getName())
                .build();

        User savedUser = userRepo.save(user); //DB저장, User 엔티티 반환
       
        //엔티티 → DTO 변환
        return RegisterResponse.fromEntity(savedUser);
    }

    // 로그인
    public LoginResponse login(LoginRequest dto) {
        User user = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return LoginResponse.fromEntity(user); // 로그인 성공 시 User 리턴
    }
	
}
