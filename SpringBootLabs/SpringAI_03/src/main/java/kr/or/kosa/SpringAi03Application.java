package kr.or.kosa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import kr.or.kosa.service.OllamaChatService;

@SpringBootApplication
public class SpringAi03Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringAi03Application.class, args);
		OllamaChatService ollamaChatService = context.getBean(OllamaChatService.class);
		String response = ollamaChatService.getQuestion();
		System.out.println("결과 : \n" + response);
	}

}
