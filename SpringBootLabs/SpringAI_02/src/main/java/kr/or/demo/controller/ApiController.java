package kr.or.demo.controller;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor
public class ApiController {
	private final OpenAiChatModel chatModel;
	//자동 주입되는 이유 : 그레이들에 의존성 걸어놔서
	
	@GetMapping("/api/chat")
	public String getChat(@RequestParam("message") String message) {
		return chatModel.call(message); //OPEN AI 연동
		//call : LLM 호출
	}
}
