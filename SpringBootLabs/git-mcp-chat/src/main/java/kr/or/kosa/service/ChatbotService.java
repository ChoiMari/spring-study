package kr.or.kosa.service;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;


@Service
public class ChatbotService {

    private final ChatClient chatClient;

    public ChatbotService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String question) {
        // 여기에서는 system 프롬프트에 "Git 관련 작업은 MCP 도구를 활용하라" 정도를 넣어주면 좋음
        return chatClient
                .prompt()
                .system("""
                        당신은 Git 전문가 어시스턴트입니다.
                        사용자가 Git 저장소 상태/브랜치/로그/파일 목록 등을 물어보면
                        가능하면 MCP로 제공되는 Git 도구를 먼저 사용해서 실제 결과를 확인한 뒤
                        한국어로 이해하기 쉽게 설명해 주세요.
                        """)
                .user(question)
                .call()
                .content();
    }
}