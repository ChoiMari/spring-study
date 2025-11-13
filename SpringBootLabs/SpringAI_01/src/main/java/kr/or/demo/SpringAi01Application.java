package kr.or.demo;

import kr.or.demo.service.ChatService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringAi01Application {

    private final ChatService chatService;

    SpringAi01Application(ChatService chatService) {
        this.chatService = chatService;
    }

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringAi01Application.class, args);
		
		
		ChatService chatService = context.getBean(ChatService.class);
		chatService.run();
	}

}
