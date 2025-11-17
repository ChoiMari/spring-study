package kr.or.kosa.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;


@Service 
public class OllamaChatService {
	private final ChatClient chatClient;
	
	public OllamaChatService(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	public String getQuestion() {
		return chatClient.prompt("거북선을 만든 한국 위인은?").call().content();
	}
}
