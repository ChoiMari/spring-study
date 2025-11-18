package kr.or.kosa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;


@Service 
public class CustomerSupportService {
	//ChatClient AI 객체 선언
	private final ChatClient chatClient;
	
	//생성자 주입 - 환경변수의 키값을 가지고..
	public CustomerSupportService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build(); //open AI와 연결한 객체의 주소
	}
	//챗봇과의 대화 기록을 저장하고 싶다
	//-> 메모리에 쓸려면 컬렉션
	private final Map<String, List<Message>> chatHistory 
		= new ConcurrentHashMap<String, List<Message>>();
	
	//String : hong
	//[new Message()][new Message()][new Message()]
	
	//챗봇 시스템 프롬프트 구성
	//-> 나중에는 RAG로 만들어도 된다?
	// 지금은 콘솔로
	private final String systemPrompt = """
			당신은 "koda Tech"라는 회사의 E-커머스 고객 지원 챗봇입니다.
			항상 친절하고 명확하게 답변해야 합니다. 사용자가 상품에 대해서 물으면 아래 정보를 기반으로 답해주세요.
			
			- 상품명: 갤럭시 AI 북
			- 가격: 1,500,000원
			- 특징: 최신 AI 기능이 탑재된 고성능 노트북, 가볍고 배터리가 오래간다
			- 재고: 현재 구매 가능
			
			- 상품명: AI 스마트 워치
			- 가격: 350,000원
			- 특징: 건강 모니터링이 가능하고, 스마트폰과 연동 기능을 제공. 방수 기능을 포함
			- 재고: 품절(5일 후 재입고 예정)
			
			내부에 없는 정보일 경우, 정중히 "죄송합니다 제품이 없습니다"를 답변해주세요
			
			""";
	
	public String getChatResponse(String userId, String userMessage) {
		//기존에 메시지가 있는지 확인
		//Map안에서 기존 사용자의 대화가 있는지..
		// userId에 해당하는 대화 기록이 존재하는지를 확인하고 있다면 같이 끌고옴
		List<Message> history = chatHistory.computeIfAbsent(userId, k -> new ArrayList<>());
				//있으면 해당하는 리스트를 가지고 오고 없으면 새로 만듬
		/*
		  	chatHistory는 Map<String, List<Message>> 형태(보통)로,
			각 사용자(userId)의 이전 대화 목록(List<Message>)을 저장하고 있습니다.
			메세지 객체가 들어 있음
			
			computeIfAbsent()는 map에 값이 없으면 자동으로 생성하는 메서드입니다.
			
 			즉, userId의 대화 기록이 없으면 새로 ArrayList()를 만들고, 있으면 기존 기록을 꺼냄.
		
			hong 사용자가 기존 대화 내용이 있냐? 있으면 가져오고 
			없으면 새로 어레이리스트 만들어라
			k -> new ArrayList<>()
			k가 유저ID 
		 */
		
		//AI에게 메시지 보낼 때
		//시스템 프롬프트(메시지)와 사용자 메시지를 포함한 
		//전체 대화 내용을 만들어주어야한다
		List<Message> conversion = new ArrayList<Message>();
		
		//ArrayList에 시스템 프롬프트 담고, 기존 대화내용 담고, 현재 내용들을 담음
		// 그래야 기존것을 기반으로 해석해준다
		//-> system 프롬프트 + 사용자의 기존 대화 내용 + 현재 사용자 요청 메시지
		//Message인터페이스를 구현하는게 SystemMessage, UserMessage, ... 
		conversion.add(new SystemPromptTemplate(systemPrompt).createMessage()); // 시스템 프롬프트 저장
		
		conversion.addAll(history); // 기존의 대화 내용 저장 - 특정 사용자의 기존 대화 목록
		//예) - 사용자 김씨의 모든 대화 기록을 ArrayList에 저장함
		
		//현재 요청 메시지 저장
		conversion.add(new UserMessage(userMessage));
		
		//ChatClient 사용하여 OPEN AI에게 요청함
		Prompt prompt = new Prompt(conversion);
		/*
		 예시)
		 기존 대화가 있다고 가정함
		  [
		  	SystemMessage의 객체주소,
		  	history과거 내용들의 메시지 객체주소,
		  	useMessage("AI 폰 어때")현재대화메시지객체주소
		  ]
		  
		  systemPrompt = "넌 여행가이드야" // 시스템 프롬프트
		  history = [ //-> 기존 사용자 대화
		  	UserMessage("북해도 여행 추천해주"),
		  	AssistantMessage("북해도의 얼음 축제 좋아") 
		  ]
		  userMessage = "맛집도 추천해" //-> 현재 요청 메시지
		 */
		
		ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
		
		//응답에 대한 유효성 체크
		if(response == null || response.getResult() == null 
				|| response.getResult().getOutput() == null) {
			System.err.println("AI 응답이 유효하지 않습니다.");
			return "[네트워크 예외] 현재 AI가 응답을 처리할 수 없습니다";
		}
		
		//다시 대화 기록에 대한 현재 질문과 답변을 히스토리에 추가
		//문제는 히스토리가 많아질수있어서 개발자가 한도를 주고 쳐버려야 함
		history.add(new UserMessage(userMessage)); // user가 질문한 내용
		history.add(response.getResult().getOutput()); // AI가 응답한 내용
		
		//대화가 너무 길어져서
		//가장 단순한 방법(배열 길이 길면 쳐냄)
		//다중 사용자일 경우 사용자별로 다시 찾아내야한다고 함
		//지금은 한사람이라고 가정
	
		if(history.size() > 10) { // 대화기록이 10개보다 많아지면
			history.subList(0, history.size()-10).clear();
			//대화기록을 10개 남기고 지움(오래된 메시지부터 삭제)
			//최근 대화 10개만 기억
		}
		
		System.out.println("[사용자 : " + userId + "의 대화 기록]");
		System.out.println("======================================");
		//history는 Map객체, key(userId)를 던져서 메시지List를 꺼냄
		List<Message> message = chatHistory.get(userId);
		
		for(int i = 0; i < message.size(); i+=2) {
			//질의-> 응답 구조라서 +=2함 //AI 응답은 건너 뛴다는 소리
			Message question = message.get(i);
			System.out.println("Question : " + question.getText());
			
			if(i + 1 < message.size()) { // 답변만 
				Message answer = message.get(i+1);
				System.out.println("Answer : " + answer.getText());
			}
			System.out.println("*************************************");
		}
		
		return response.getResult().getOutput().getText();
	}
	/*
	 Spring AI Prompt 정의 이해가 중요하다
	 <메시지 클래스 구조>
	 클래스 			역할 		설명
	 SystemMessage 		"system" 	모델에게 행동 지침/컨텍스트를 주는 설정 
	 UserMessage		"user"		사람이 입력한 질문, 요청
	 AssistantMessage	"assistant"	모델이 생성한 응답
	
	 */
	
}
