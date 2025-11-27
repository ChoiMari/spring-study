package kr.or.kosa.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 SyncMcpToolCallbackProvider toolCallbackProvider) {

        return ChatClient.builder(chatModel)
                // MCP 서버에서 가져온 GitTool 들을 기본 Tool로 붙인다
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
                .build();
    }
}
