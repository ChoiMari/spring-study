package kr.or.mari.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//로그인 요청 Dto
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequest {
	private String username;
    private String password;

}
