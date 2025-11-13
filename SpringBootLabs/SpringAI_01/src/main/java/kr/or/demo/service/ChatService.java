package kr.or.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	@Autowired
	private ChatModel chatModel;
	public void run() {
		ChatClient chatClient = ChatClient.builder(chatModel).build();
		//ChatClient객체 : 연결된 AI에 작업할 수 있는 객체
		//모델(open AI)를 아규먼트로 넣어둠
		
		String response = chatClient.prompt("스티븐 잡스의 언행 3개만 알려줘")
				.call().content();
		//질의를 던짐
		
		System.out.println("결과 : \n" + response);
	}
}
