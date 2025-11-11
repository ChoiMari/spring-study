package Network;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/*
java api 는 네트워크 작업을 지원하기 위해서 ....
java.net패키지에 있음 .... 다양한 클래스 제공

직접 사이트에 접근해서 내용을 다 가지고 올 수 있다.
자바는 url객체가 있음
이걸 사용하면 내가 넣은 인터넷 주소와 연결이 가능하다.

1. 크로스 도메인 오류 (java 코드 처리 : read)
2. 크롤링 (특정 페이지 원하는 정보 추출)

샘플) https://qi-b.qoo10cdn.com/goods_image/5/2/6/3/356775263o.jpg
*/
public class Ex02_URLConnection {
	public static void main(String[] args) throws Exception {
		//url 객체를 통해서 해당 사이트와 연결
		// 스트림(빨대)를 꽂아서 읽어온다고 함
		String urlstr = "https://qi-b.qoo10cdn.com/partner/goods_image/8/2/1/3/356778213g.jpg";
		
		URL url = new URL(urlstr); //연결함 (인터상에 주소와 연결)
		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		
		//연결은 URL객체가 담당
		// 연결된 곳의 정보 파악은 URLConnection이 담당한다.
		URLConnection uc = url.openConnection();
		
		// URLConnection 연결된 주소에서 원하는 추출하기
		int filesize = uc.getContentLength(); //파일 크기 확인
		
		System.out.println("파일 크기 : " + filesize); 
		System.out.println("파일형식 : " + uc.getContentType()); 
		
		// read 복제 .... 빈 이미지 파일 만들고 여기다가 씀(write)
		FileOutputStream fos = new FileOutputStream("copy2.jpg"); //파일 생성 (프로젝트 폴더)
		
		byte[] buffer = new byte[2048];
		int n = 0;
		int count =0;
		
		while((n = bis.read(buffer)) != -1) {
			//fos.write(buffer,0,buffer.length);
			fos.write(buffer,0,n); //^^ gpt가 해결  .....
			fos.flush();
			System.out.println("n : " + n);
			count+=1;
		}
		System.out.println("count : "+ count);
		fos.close();
		bis.close();
	}

}
