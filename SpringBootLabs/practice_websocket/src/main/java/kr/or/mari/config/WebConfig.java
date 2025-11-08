package kr.or.mari.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 설정 클래스 - Bean등록용 //스프링 컨테이너에게 알려주는 역할
//@Configuration 클래스는 스프링 컨테이너가 읽어서 Bean(빈) 을 등록
public class WebConfig {

	@Value("${frontend.url}")
	private String frontendUrl;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins(frontendUrl) // 리액트 주소
						.allowedMethods("GET", "POST", "PUT", "DELETE").allowCredentials(true); // 쿠키 허용!
			}
		};
	}
}
