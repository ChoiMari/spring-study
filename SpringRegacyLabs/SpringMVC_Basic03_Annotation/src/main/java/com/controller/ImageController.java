package com.controller;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.model.Photo;

@Controller
@RequestMapping("/image/upload.do")
public class ImageController {
	// 요청 주소가 동일 할 때 판단 방식 
	//GET - 화면 보여주세요
	@GetMapping
	public String form() {
		return "image/image";
	}
	
	
	//POST - 로직 처리해주세요
	@PostMapping
	public String submit(Photo photo,
			//이렇게 하면 자동으로 new Photo()되고 setter이용해서 자동 주입됨
			//근데 photo.setImage()는 자동 주입이 안된다.
			//photo.setFile(CommonsMultipartFile file)에서 뽑아내야됨
			//imageFile.getOriginalFilename() 파일이름 추출해서 수동 저장
			//-> 파라미터로 dto선언. 텍스트도 받고 파일도 받는다
			//enctype="multipart/form-data"와 private CommonsMultipartFile file; 해놓음
			HttpServletRequest request) {
		/*
		1. Photo DTO 타입으로 데이터 받기
		1.1 자동화 : name 속성값이 Photo 타입클래스의 member field 명과 동일
		2. public String submit(Photo photo) 내부적으로 ...  
		   >> Photo photo = new Photo();
		   >> photo.setName("홍길동");
		   >> photo.setAge(20);
		   >> photo.setImage() >> 자동 주입 안되요 >> 수동으로 >> 가공 CommonsMultipartFile 추출(이름)
		   >> photo.setFile(CommonsMultipartFile file) 파일 자동으로 들어와요
	   
	 */
	System.out.println(photo.toString());
	//Photo [name=hong, age=100, image=null, 
	//file=MultipartFile[field="file", filename=w3schools.jpg, contentType=image/jpeg, size=4377]]
	
	CommonsMultipartFile imageFile = photo.getFile();
	System.out.println("imagefile getName()" + imageFile.getName() );
	System.out.println("imagefile getContentType()" + imageFile.getContentType() );
	System.out.println("imagefile getOriginalFilename()" + imageFile.getOriginalFilename() );
	System.out.println("imagefile getBytes().length" + imageFile.getBytes().length );
	
	//필요한 정보가 있다면 추출해서 DB > Table > insert 해야 되요
	
	//POINT 파일명 추출 image=null
	photo.setImage(imageFile.getOriginalFilename()); //수동 ...
	
	//upload (서버에 파일쓰기)
	//자동화 : cos.jar (무료) ,  덱스트 업로드(제품 구매) 
	
	//수동으로 코딩( I/O)
	String fileName = imageFile.getOriginalFilename();
	//HttpServletRequest request
	String path = request.getServletContext().getRealPath("/upload"); //실 배포 경로
	String fpath = path + "\\" + fileName;  // C:\\Web\\upload\\a.jpg
	
	System.out.println(fpath);
	
	FileOutputStream fs = null;
	
	try {
		   fs = new FileOutputStream(fpath); //파일이 없으면 빈 파일 ( a.jpg) 자동
		   fs.write(imageFile.getBytes()); //image생성
		   //업로드한 파일을 서버에서 쓰는 코드
		   //만들어진 파일명에 write하는것
		   
	} catch (Exception e) {
		   e.printStackTrace();
	}finally {
		 try {
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//요기까지 작업 서버에 특정 폴더에 (upload) : 파일 생성
	//DB 연결 > DAO > 게시판 테이블 > Insert  했다고 치고
	
	
	return "image/image"; //업로드 페이지로 다시 이동함
	}
}
