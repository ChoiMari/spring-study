package kr.or.kosa.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kosa.dto.ChatRequestDto;
import kr.or.kosa.dto.ChatResponseDto;
import kr.or.kosa.service.CustomerSupportService;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor
public class ChatController {
	private final CustomerSupportService svc;
	
	@PostMapping("/chat")
	public ChatResponseDto chat(@RequestBody ChatRequestDto request) {
		String responseMessage = svc.getChatResponse(request.getUserId(), request.getMessage());
		
		return new ChatResponseDto(responseMessage);
	}
}
