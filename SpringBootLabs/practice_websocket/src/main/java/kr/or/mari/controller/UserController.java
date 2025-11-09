package kr.or.mari.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.or.mari.domain.User;
import kr.or.mari.dto.LoginRequest;
import kr.or.mari.dto.LoginResponse;
import kr.or.mari.dto.RegisterRequest;
import kr.or.mari.dto.RegisterResponse;
import kr.or.mari.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * 로그인
 * spring-security-crypto 의존성 + PasswordEncoder Bean + 세션 사용
 */
@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userSvc;
	
	// 아이디 중복 검사
	@GetMapping("/check")
	public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
	    boolean exists = userSvc.existsByUsername(username);
	    return ResponseEntity.ok(exists);
	}

	
	 // 회원가입
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest dto) {

    	RegisterResponse response = userSvc.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest dto,
            HttpSession session) {

    	LoginResponse loginUser = userSvc.login(dto);
        session.setAttribute("loginUser", loginUser); // 세션에 저장함

        return ResponseEntity.status(HttpStatus.OK)
        		.body(loginUser);

    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate(); // 세션에서 삭제
        return ResponseEntity.noContent().build(); // HTTP 204
    }
    
    //세션 유지 확인용 
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        LoginResponse user = (LoginResponse) session.getAttribute("loginUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("로그인이 필요합니다.");
        }
        return ResponseEntity.ok(user);
    }
	
}
