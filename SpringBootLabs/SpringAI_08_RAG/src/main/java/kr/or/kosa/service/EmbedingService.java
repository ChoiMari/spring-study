package kr.or.kosa.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;


@Service @RequiredArgsConstructor
public class EmbedingService {
	private final VectorStore vectorStore; 
	
	//문서, text 파일을 읽어서 백터화(float타입의 백터숫자배열로 만드는것 -> 임베딩)
	//LLM이 알아들을 수 있도록 변환
	//변환해야 LLM이 유사도를 판단하고 읽을 수 있다
	public void processUploadPdf(MultipartFile file) throws IOException {
		//1. 사용자가 업로드한 pdf파일을 바로 읽으면 성능이 떨어짐..
		// 다이렉트로 읽으면 성능이 떨어져서 임시파일로 만들어서 메모리에서 읽고 사용
		File temFile = File.createTempFile("upload", "pdf");
		//임시 파일 생성
		// temp폴더에 자동 생성을 하는데 uploadxxxx.pdf 형태로 만듬
		
		file.transferTo(temFile);//실제로 받은 물리적인 파일을
		// 임시 파일로 변환
		
		Resource fileResource = new FileSystemResource(temFile);
		//Resource : 인터페이스로 import하기
		
		try {
			//pdf문서 읽기 전 (세팅,설정, 옵션)읽기 형식 정의
			PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
					.withPageTopMargin(0) //pdf 상단 여백 설정
					.withPageExtractedTextFormatter(//페이지에서 추출된 텍스트에 대한 포맷팅 정의
							ExtractedTextFormatter.builder()
							.withNumberOfBottomTextLinesToDelete(0) // 하단에서 삭제할 텍스트 줄 수 
							.build()) //들여쓰기
					.withPagesPerDocument(1) //한 번에 처리할 페이지 수 설정
					.build();
			
			PagePdfDocumentReader pdfDocumentReader= new PagePdfDocumentReader(fileResource, config);
			//1번째 아규먼트: 읽을 pdf 임시 파일, 2번째 아규먼트 : 읽기 옵션 설정 객체
			
			List<Document> documents = pdfDocumentReader.get();
			//Document : ai로 import
			
			//백터화 - 청크(LLM이 사용하는 단위 조각 최소 단위, 토큰) 단위로 나눔
			// 단어 기준으로 자르면 문제가 생긴다. LLM은 청크 단위로 잘라야 한다.
			// 토큰 수 기반 청크를 써야함
			// 백터화 -> float타입의 백터 숫자배열로 변환
			TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true);
			//1000 : 청크 사이즈 설정
			//문서를 1000씩 자르되,
			//다음 청크에 400토큰을 중복 포함해서 포함
			//왜? 문맥 유지하려고
			//true 구분바 포함(문맥유지) - 일반적으로 권장
			//false(구분자 버림)
			
			/*
			  “ 문서를 1000 토큰씩 자르되,
				다음 청크에 400 토큰을 중복 포함해서 문맥을 잇고,
				너무 작은 청크는 버리고,
				최대 5000 토큰까지 허용하며,
				개행/마침표 같은 구분자도 포함해라.” 

			    TokenTextSplitter(
		    			int chunkSize,
		    			int chunkOverlap,
		    			int minChunkSize,
		    			int maxChunkSize,
		    			boolean keepSeparators
				)
				
				1000 토큰 = 1000단어가 아니다.
				한국어 기준으로 약 500~800 단어 정도,
				영어 기준으로는 약 700~900 단어 정도 분량입니다.
				즉, A4로 치면 약 1~2페이지 분량입니다.
				
				
				chunkOverlap = 400
				✔ 앞 청크 일부(400 tokens)를 다음 청크에 중복해서 포함
				왜 사용? → 문맥 유지!!
				예시
				Chunk1: A B C D E F G
				Chunk2: E F G H I J K
			 400이면 꽤 많은 중복
          → 정확도↑
          → 임베딩 비용↑ (청크 양 증가)
			 
			 
			keepSeparators = true
			문장 구분자(개행, 마침표 등)를 청크에 포함할지 여부
			true → "구분자 포함"
				 → 읽기 편하고 문맥 유지

          false → 구분자 버림
                → 임베딩 저장 크기 줄어듦

           일반적으로 true 권장
           → 자연스러운 문맥 유지에 도움.
		 
			 */
			
			List<Document> spDocuments = splitter.apply(documents);
			
			//pgvoctor store에 저장
			vectorStore.accept(spDocuments);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			temFile.delete(); //임시 파일 삭제
		}
		
	}
	
}
