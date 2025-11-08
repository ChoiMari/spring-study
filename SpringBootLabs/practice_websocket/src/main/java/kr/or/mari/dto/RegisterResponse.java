package kr.or.mari.dto;

import kr.or.mari.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterResponse {
	private Long id;
    private String username;
    private String name;

    public static RegisterResponse fromEntity(User user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}
