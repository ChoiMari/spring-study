package kr.or.kosa.controller;



import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kosa.service.HotelService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController @RequiredArgsConstructor @RequestMapping("/api/hotel")
@CrossOrigin(origins = "http://localhost:5173/") //-> config파일 만드는게 낫다.. 지금은 연습이니까..
public class HotelController {
	
	private final HotelService hotelService;
	private final DocumentUploadController documentUploadController;
	
	private final VectorStore vectorStore;
	private final ChatModel chatModel;

	/*
	 미디어타입이 중요한 역할을 한다. - 실시간 스트림이 가능 3개가 있음
	 	1. MediaType.TEXT_EVENT_STREAM_VALUE는 상수값이 이벤트스트림
	 	
		  MediaType.TEXT_EVENT_STREAM_VALUE : 
		   public static final String TEXT_EVENT_STREAM_VALUE = "text/event-stream"; 
		   이것이 의미하는 것: SSE(Server-Sent Events) 전송 타입
	       이 MIME 타입은 클라이언트가 끊기지 않는 HTTP 연결을 통해 실시간으로 데이터를 받겠다는 의미입니다.
		  
		2.  MediaType.APPLICATION_NDJSON_VALUE는 마인타입상수값 제이슨
		  제이슨 객체를 여러개로 연속적으로 전송하지만 각각 따로따로 실시간으로 던지겠다
		  
		  목적에 따라서 사용함
		  
		  MediaType.APPLICATION_NDJSON_VALUE :
		   public static final String APPLICATION_NDJSON_VALUE = "application/x-ndjson";
		   NDJSON(Newline Delimited JSON) 형식이 뭐지?
	       JSON 객체 여러 개를 연속으로 전송하지만, 배열 형태가 아니라 줄바꿈으로 구분하는 방식

			예시:
			{"message":"첫 번째 응답"}
			{"message":"두 번째 응답"}
			{"message":"세 번째 응답"}
		  
		
		3. MediaType.TEXT_PLAIN_VALUE
		  
		  MediaType.TEXT_PLAIN_VALUE : 그냥 텍스트로 흘려보냄
		  public static final String TEXT_PLAIN_VALUE = "text/plain";
		  순수 텍스트(String)를 그대로 보내고 싶을 때
			HTML 아님
			JSON 아님
			XML 아님
			SSE 아님
			파일 아님
			그냥 “문자열만” 전송
		  
		  
		 ChatModel을 사용해 직접 응답 생성
	  	 return chatModel.stream(template
	            .replace("{context}", results.toString())
	            .replace("{question}", question));
		 */
	//Flux - 이게 붙으면 실시간처리
	@GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
	//MediaType : import org.springframework.http.MediaType; 으로 import
	//클라이언트에 응답을 보낼때 text형태로 스트리밍하겠다.
	//응답을 JSON이 아니라 "text"로 스트림으로 흘림 - 실시간 출력 텍스트(스트리밍)
	//Flux<String> : Reactor 비동기 스트림 타입 -> 클라이언트에 "실시간 타자치는 것처럼" 여러 조각 문자열을 전송 가능
	public Flux<String> hotelChatbot(@RequestParam("question") String question){
		ChatClient chatClient = ChatClient.builder(chatModel)
										.build();
		//-> open ai 쓰겠다.
		
		//LLM 생성
		//similaritySearch () 유사도 검색
		List<Document> result = vectorStore.similaritySearch(SearchRequest.builder()
									.query(question)
									.similarityThreshold(0.5)
									.topK(1) //질의 상위 1개만 가져옴
									.build());
		System.out.println("Vector store 유사도 검색 결과 : " + result);
		
		//내 질문과 유사도 검색 결과와 합쳐서 LLM에게 질의함
		String template = """
				당신은 어느 호텔의 직원입니다. 문맥에 따라서 고객의 질문에 정중하게 답변해주세요.
				컨텍스트가 질문에 대답할 수 없는 경우, "죄송합니다. 모르겠습니다."라고 대답하세요.
				컨텍스트:
				{context}
				질문:
				{question}
				""";
		
		//파일기반으로 만들고 싶으면 확장자를 .st로 해야함
		
		//프롬프트 3가지 - 유저, 시스템, 어시스턴트
		
		
		return chatClient.prompt().user(PromptUserSpec -> PromptUserSpec.text(template)
				.param("context", result) //text에서 읽은 정보
				.param("question", question))
				.stream() // 실시간 처리
				.content();
	}
				
}
