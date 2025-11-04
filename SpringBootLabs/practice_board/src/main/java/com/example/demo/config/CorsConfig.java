package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**") // CORS 허용할 경로
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://127.0.0.1:5173",
                        "http://192.168.2.229:5173"
                    ) // React 개발 서버 주소
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용 메서드
						.allowedHeaders("*") // 허용 헤더
						.allowCredentials(true); // 쿠키/인증정보 허용
			}
		};
	}
}
