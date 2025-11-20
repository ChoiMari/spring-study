package kr.or.kosa.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VisionController {
	//ChatClient AI 객체 선언
	private final ChatClient chatClient;
	
	//생성자 주입 - 환경변수의 키값을 가지고..
	public VisionController(ChatClient.Builder builder) {
		this.chatClient = builder.build(); //open AI와 연결한 객체의 주소
	}
	
	@GetMapping
	public String visionPage() {
		return "vision";
	}
	@PostMapping("/upload") @ResponseBody
	public ResponseEntity<Map<String , String>> vision(@RequestParam("file") MultipartFile file,
															@RequestParam("message") String message){
		
		Map<String, String> response = new HashMap<>();
		try {
			String useMessage = (message == null || message.isEmpty()) ? "이미지 분석해주" : message;
			
			//AI 요청
			String result = chatClient.prompt().user(user -> user.text(useMessage)
					.media(MimeType.valueOf(Objects.requireNonNull(file.getContentType())), file.getResource())).call().content();
			response.put("result", result); // 분석결과 저장
			System.out.println("결과 result : " + response);
		} catch (MaxUploadSizeExceededException e) {
			response.put("error", "업로드한 파일이 너무 큽니다. 최대 10MB");
			return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
		}catch (Exception e) {
			response.put("error", "[업로드 예외] 파일 업로드 중 ERROR");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		return ResponseEntity.ok(response);
		//텍스트 + 음성 처리 -> 멀티 모달
		//media()메서드
		/*
		 * media(MimeType, Resource)
		 	이미지 타입 - image/png, image/jpeg 뽑아내는게 MimeType
		 	AI가 읽을 수 있는 형태로 변환해주는 메서드
		 	Resource
		 */
	}
}
