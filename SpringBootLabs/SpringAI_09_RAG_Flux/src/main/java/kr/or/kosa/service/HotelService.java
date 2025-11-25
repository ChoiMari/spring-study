package kr.or.kosa.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class HotelService {
	private final VectorStore vectorStore;
	private final JdbcClient jdbcClient;
	
	public void processUploadText(MultipartFile file) throws IOException {
		File tempFile = File.createTempFile("upload", ".text");
		//임시 파일 생성
		// temp폴더에 자동 생성을 하는데 uploadxxxx.txt 형태로 만듬
		
		file.transferTo(tempFile);//실제로 받은 물리적인 파일을
		// 임시 파일로 변환
		
		Resource fileResource = new FileSystemResource(tempFile);
		//Resource : 인터페이스로 import하기
		
		//이전에 입력한 임베딩한 데이터 건 수를 알고 싶으면
		// 쿼리 날리면 된다
		Integer count = jdbcClient.sql("select count(*) from hotel_vector")
							.query(Integer.class)
							.single();
		//-> 스프링이 가지고 있는 jdbc클라이언트
		
		try {
			if(count == 0) {
				List<Document> documents = Files.lines(fileResource.getFile().toPath())
												.map(Document :: new) //map(line -> new Document(line))와 같음 - 메서드 참조
												.collect(Collectors.toList());
				/*		
					Files.lines(...)
	
					파일을 한 줄씩 읽어서 Stream<String> 으로 반환
					.map(Document::new)
					각 줄을 Document 객체로 변환
					즉, new Document("파일의 한 줄") 형태로 리스트 생성
					.collect(Collectors.toList())
					Stream → List<Document> 로 변환
					결론:	
			   */
				
				//문서(Document)를 잘라야함 - 토큰 단위로 스플릿
				TokenTextSplitter splietter = new TokenTextSplitter();
				//개발자가 청크 단위로 설정 안해도 기본값으로 세팅하는게 있다..
				/*
					// 백터화 -> float타입의 백터 숫자배열로 변환
					TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true); 
					
					기본값 청크사이즈 500, 
							오버랩 50, 
							최소청크사이즈 100, 
							최대청크사이즈 정수의 최댓값, 
							false
					
				 */
				
				List<Document> splitDocuments = splietter.apply(documents);
				
				//pgvoctor store에 저장(백터 저장소에 임베딩된 데이터 저장)
				vectorStore.accept(splitDocuments);
	
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			tempFile.delete(); //임시 파일 삭제
		}
	}
}
