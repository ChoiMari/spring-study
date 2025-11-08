package kr.or.mari.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//회원가입 요청 Dto
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
}
