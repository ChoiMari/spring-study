package kr.or.mari.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
/**
 * WebSocket 설정 클래스
 * STOMP 기반 메시징을 활성화 하고, 
 * 클라이언트 <-> 서버 양방향 실시간 통신 경로를 정의함
 */
@Configuration
@EnableWebSocketMessageBroker //STOMP 메시징 기능 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
    @Value("${frontend.url}")
    private String frontendUrl; // 애플리케이션.프로퍼티스 파일에서 읽어옴
	
	/**
	 * 메시지 브로커 설정
	 * 클라이언트가 구독 할 수 있는 경로(prefix)
	 * 서버가 클라이언트로 메시지를 전송할 때 사용하는 경로 설정
	 * 클라이언트가 메시지를 보낼 때 사용하는 prefix도 정의
	 */
	@Override
	    public void configureMessageBroker(MessageBrokerRegistry registry) {
	        // 서버 → 클라이언트로 메시지 전달 시 사용할 경로 (subscribe용)
			// 브로드캐스트 경로 /topic
			// 1:1 채팅 경로 /queue
	        registry.enableSimpleBroker("/topic", "/queue");
	        // 클라이언트 → 서버로 메시지 보낼 때 사용할 prefix
	        registry.setApplicationDestinationPrefixes("/app");
	    }
	
	//STOMP 엔드포인트 등록
	/**
	 * 클라이언트가 최초로 WebSocket 연결을 시도하는 엔드포인트 URL정의
	 * SockJS를 활성화 하여, 브라우저가 웹소캣을 지원하지 않아도 
	 * fallback가능
	 * CORS 정책 허용도 함께 설정
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")       // 연결 URL (예: ws://localhost:8090/ws-stomp)
        //.setAllowedOriginPatterns(frontendUrl)  // CORS 허용
        // 개발 중엔 로컬 전체 허용 (IP나 포트가 바뀌어도 됨)
        .setAllowedOriginPatterns(
            "http://localhost:*",
            "http://127.0.0.1:*",
            "http://192.168.*:*"
        )
        .withSockJS();                  // SockJS fallback 활성화
	}

}
