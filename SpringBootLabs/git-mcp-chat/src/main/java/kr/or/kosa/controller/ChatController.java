package kr.or.kosa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.or.kosa.service.ChatbotService;


@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatbotService chatbotService;

    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // 간단하게 JSON {"question": "..."} 형태로 받는다고 가정
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String answer = chatbotService.chat(request.question());
        return ResponseEntity.ok(new ChatResponse(answer));
    }

    public record ChatRequest(String question) {}
    public record ChatResponse(String answer) {}
}