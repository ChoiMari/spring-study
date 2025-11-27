package kr.or.kosa.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kosa.tool.SpringTools;

@RestController @RequestMapping("/tool")
public class TollController {
	private final ChatClient chatClient;
	public TollController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	//LLM 요청
	@GetMapping("/simple")
	public String tool_01() {
		return chatClient.prompt("내일은 며칠일까?")
				.call()
				.content();
	}
	
	@GetMapping("/date")
	public String tool_02() {
		return chatClient.prompt("내일은 며칠일까?")
				.tools(new SpringTools())
				.call()
				.content();
	}
	
	@GetMapping("/emp")
	public String tool_03(@RequestParam("name") String name) {
		String promptText = String.format(
				"아래 도구를 사용하여 직원 '%s'의 취미를 알려줘. 만약 지원이 없으면 '해당 직원이 없습니다'라고 답변해줘\n"
				+ "도구: userHobby(name) -  직원 이름을 넣으면 취미를 알려줌"
				, name);
		return chatClient.prompt(promptText)
							.tools(new SpringTools())
							.call()
							.content();
	}
}
