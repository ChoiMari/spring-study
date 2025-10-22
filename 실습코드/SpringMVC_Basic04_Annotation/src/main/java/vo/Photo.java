package vo;
/*
 게시판 글쓰기 + 파일 업로드
 create table photo(
 	name
 	age
 	image_path --> 실제 파일 DB에 저장 X
 	-- 1.jsp 
 );
 
 실제 파일은 웹서버에 저장(디스크) 근데 이것 보단...
 클라우드 S3(파일서버, AWS)- 정적 데이터 관리
 (S3에 vue 또는 리엑트 배포하기도 함) 하지만 파일 서버로써의 목적이 크다
 
 파일 업로드
 1. 파일서버(S3)에 파일을 써야함 -> File객체, inputStream, outputStream 사용
 2. 파일 정보를 DB에 insert -> 파일 이름, 파일 용량 등 메타데이터 저장
 
 Spring에서는 dto를 통해서 파일 객체를 받아요
 파일을 받을 수 있는 타입(클래스)이 존재한다.
 -> CommonsMultipartFile
 -> 이걸 사용하면 dto로 파일 스트림 형태의 바이트 코드를 받을 수 있다
 */

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class Photo {
	private String title;
	private String content;
	private String image;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	//---------------------------------------------
	//파일을 담을 수 있는 객체가 필요하다
	// 파일 여러개 업로드 하고 싶으면 List<CommonsMultipartFile> fileList = new ArrayList<>();
	//(중요)
	private CommonsMultipartFile file;
	// 파일을 던지면 이게 바이트 코드로 받는다
	// 업로드한 파일 정보 받는다
	// 단, 조건이 있음
	// -> (약속)file 이름과 input태그의 name값이 같아야한다.(name="file")
	//사진:<input type="file" name="file"><br>
	// 그리고 <form method="post" enctype="multipart/form-data">
	//뷰에서 multipart/form-data로 보내야함
	
	public CommonsMultipartFile getFile() {
		return file;
	}
	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
	//----------------------------------------------
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}






	
	
}
