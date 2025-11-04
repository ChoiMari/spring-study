package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//응답 구조 통일을 위해 사용함
@Getter @Builder @ToString @Setter @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class ApiResponse<T> {
	private boolean success;  // 성공 여부
    private String message;   // 결과 메시지
    private T data;           // 실제 데이터 (nullable)

    // 성공 응답 헬퍼
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("요청이 성공했습니다.")
                .data(data)
                .build();
    }

    // 실패 응답 헬퍼
    public static <T> ApiResponse<T> fail(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
