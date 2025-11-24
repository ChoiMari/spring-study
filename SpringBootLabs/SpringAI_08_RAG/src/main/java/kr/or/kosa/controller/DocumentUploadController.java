package kr.or.kosa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.or.kosa.service.EmbedingService;
import lombok.RequiredArgsConstructor;

@RestController @RequiredArgsConstructor @RequestMapping("/api/documents")
public class DocumentUploadController {
	private final ChatModel chatModel;
	private final EmbedingService embedingService;
	private final VectorStore vectorStore;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadPdfFile(
			@RequestParam("file") MultipartFile file){
		try {
			embedingService.processUploadPdf(file);
			return ResponseEntity.ok("pdf파일 업로드 임베딩 처리 완료");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 : " + e.getMessage());
		}
	}
	
	//LLM에 질의를 던지면 DB안에 저장시킨 임베딩(벡터데이터)를 참조해서
	//유사도를 확인하고 그걸 기반으로 LLM이 질의하도록 하게 해야함
	private String promptTempate = """
			다음 문서를 참고하여 질문에 대해 답변해 주세요.
			문서에서 답을 찾을 수 없다면, "관련 정보를 찾을 수 없습니다." 라고 답변해 주세요
			
			[문서]
			{context}
			
			[질문]
			{question}
			
			""";
	//만일 파일로 만들고 싶다면, *.st 파일로 생성하면 된다
	
	@PostMapping("/rag")
	public String ragChat(@RequestParam("question") String question) {
		PromptTemplate template = new PromptTemplate(promptTempate);
		Map<String, Object> promptParameters = new HashMap<>();
		promptParameters.put("question", question);
		//promptParameters.put("context", null);
		
		//1.VectorStore에게 유사도가 높은것을 찾아라...
		// 유사도가 높은 문서 n개를 검색하게 함
		List<Document> simmilartyDocuments = vectorStore.similaritySearch(SearchRequest.builder()
													.query(question)
													.topK(2) //유사도 2개 가져옴
													.build());
		//similaritySearch() :  요청 사항을 넣으면 된다.
		// 유사도 쿼리라고.. 쿼리 날리는 거와 비슷하다고 함
		/* 내부적으로 쿼리 동작..
		 select id,content,metadata
		 from vector_store
		 order by emedding <-> (select embedding from openai_embed($1)) 
		 LIMIT 2; 
		 백터 거리 연산자 거리(유사도)가 작을수록 유사하다.
		 */
		//2. 검색된 문서 내용을 하나의 문자열로 결합하여 출력
		String documents = simmilartyDocuments.stream()
							.map(document -> document.getFormattedContent().toString())
							.collect(Collectors.joining("\n")); // 줄바꿈, 엔터키를 기준으로 합침
		
		//검색된 것을 합해서 LLM에 질의 하는 것..
		
		//원하는 질의에 대해 유사도가 높은 것을 찾아서 1개의 문장으로 만들어 LLM에게 질의
	
		 promptParameters.put("context", documents);  // 검색해서 합친 문서 내용
		 
		return chatModel.call(template.create(promptParameters)).getResult().getOutput().getText();
	}
}
