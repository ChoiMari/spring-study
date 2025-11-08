package kr.or.mari.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kr.or.mari.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private String name;
	private String loginTime; // 로그인 시각 표시용 (옵션)

	public static LoginResponse fromEntity(User user) {
		return LoginResponse.builder()
				.id(user.getId())
				.username(user.getUsername())
				.name(user.getName())
				.loginTime(LocalDateTime.now()
	                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.build();
	}
}
