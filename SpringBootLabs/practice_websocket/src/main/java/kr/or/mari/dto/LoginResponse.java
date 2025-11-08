package kr.or.mari.dto;

import kr.or.mari.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
	 private Long id;
	    private String username;
	    private String name;
	    private String loginTime; // 로그인 시각 표시용 (옵션)

	    public static LoginResponse fromEntity(User user) {
	        return LoginResponse.builder()
	                .id(user.getId())
	                .username(user.getUsername())
	                .name(user.getName())
	                .build();
	    }
}
