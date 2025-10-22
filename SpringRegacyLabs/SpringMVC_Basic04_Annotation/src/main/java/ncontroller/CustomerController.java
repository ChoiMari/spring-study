package ncontroller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;


import dao.NoticeDao;
import vo.Notice;
import vo.Photo;

@Controller
@RequestMapping("/customer") //web.xml에서 디스패처서블릿에 걸리는 요청주소를 작성해야함
// 안그러면 404
public class CustomerController {
	
	private NoticeDao noticsdao;
	@Autowired
	public void setNoticsdao(NoticeDao noticsdao){
		this.noticsdao = noticsdao;
	}
	
	@GetMapping("/notice.htm") //고객센터 보여주세요
	public ModelAndView list(@RequestParam(value = "pg", required = false, defaultValue = "1") int page,
            @RequestParam(value = "f", required = false, defaultValue = "TITLE") String field,
            @RequestParam(value = "q", required = false, defaultValue = "") String query) throws ClassNotFoundException, SQLException {
		//pg페이지번호, f검색필드명, q검색어
		System.out.println("GET 호출됨 : /customer/notice.htm");
		System.out.printf("page=%d, field=%s, query=%s\n", page, field, query);
		List<Notice> list = noticsdao.getNotices(page, field, query);
		System.out.println(list.toString());
		ModelAndView  mv = new ModelAndView("customer/notice");
		//ModelAndView생성자 호출시 아규먼트 1개넣으면 뷰경로 지정이 됨
		mv.addObject("list", list); 
		
		//페이지네이션
	    int totalCount = noticsdao.getCount(field, query); // 전체 행 개수
	    int totalPage = (int) Math.ceil(totalCount / 5.0);
	    mv.addObject("page", page);
	    mv.addObject("totalPage", totalPage);
	    
		return mv; //뷰로 포워드 - 뷰리졸브가 접두사 접미사 앞뒤로 붙임
	}
	
	@GetMapping("/noticeDetail.htm") //상세페이지 화면 보여주세요
	public ModelAndView detail(@RequestParam("seq") String seq) throws Exception{
		Notice  notice = noticsdao.getNotice(seq);
		ModelAndView  mv = new ModelAndView("customer/noticeDetail");
		mv.addObject("notice", notice); //request.setAttribute("key",value)와 같음
		// 리퀘스트 객체에 저장해서 포워드된 뷰에서 사용가능
		return mv;
	}
	
	@GetMapping("/noticeReg.htm") // 글쓰기 화면 보여주세요
	public String insertView() { 
		System.out.println("GET 호출됨 : /customer/noticeReg.htm");
		return "customer/noticeReg";
	}
	
	@PostMapping("/image/upload.htm")
	public String insertSearchForm(Photo photo, HttpServletRequest request) throws ClassNotFoundException, SQLException {
		System.out.println("POST 호출됨 : /customer/image/upload.htm");
		
		System.out.println(photo.toString());
		CommonsMultipartFile imageFile = photo.getFile();
		 // 파일을 첨부하지 않을 경우
	    if (imageFile == null || imageFile.isEmpty()) {
	        System.out.println("첨부된 파일이 없습니다. 기본 로직으로 진행합니다.");

	        // DB insert 시 파일명을 null 처리
	        Notice n = new Notice();
	        n.setTitle(photo.getTitle());
	        n.setContent(photo.getContent());
	        n.setFileSrc(null);  // 파일 없음
	        noticsdao.insert(n);

	        System.out.println("DB INSERT 완료 (파일 없음)");
	        return "redirect:/customer/notice.htm";
	    }
	    
	    //파일을 첨부할 경우
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
		
		//DB에 insert하기
		Notice n = new Notice();
		n.setTitle(photo.getTitle());
		n.setContent(photo.getContent());
		n.setFileSrc(photo.getImage());
		noticsdao.insert(n);
		System.out.println("DB INSERT 완료");
		
		return "redirect:/customer/notice.htm"; //목록보기 화면으로 돌아감
	}
	
}
