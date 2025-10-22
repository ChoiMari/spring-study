package ncontroller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.NoticeDao;
import vo.Notice;

@Controller
@RequestMapping("/customer/")  //부분 경로 설정 가능 
public class CustomerController {
	
	//1. CustomerController 는 NoticeDao 의존합니다
	private NoticeDao noticedao;

	@Autowired
	public void setNoticedao(NoticeDao noticedao) {
		this.noticedao = noticedao;
	}
	
	@RequestMapping("notice.do")   //   /customer/notice.do
	public String notices(String pg , String f , String q , Model model) {
		
		//1. 상황  String notices > return view 주소 
		//2. 상황  함수의 parameter dto 받지 않아요 
		//2.1 Model model 가지고 있으면 자동으로 Model 객체를 생성해서 객체 주입
		//2.2 model.addAttribute("list", list); // 자동으로 notice.jsp forward .... 
		//3. internalResourceViewResolver  구현하지 않으면  ... return "notice.jsp"; 주소를
		//3.1 http://localhost:8090/SpringMVC_Annotation/customer/notice.htm
		//3.2 /customer/notice.jsp 찿아요
		
		//각 parameter 기본값 설정
		//default 값 설정
		int page = 1;
		String field="TITLE";
		String query = "%%";
		
		if(pg != null   && ! pg.equals("")) {
			page  = Integer.parseInt(pg);
		}
		
		if(f != null   && ! f.equals("")) {
			field = f;
		}
		
		if(q != null   && ! q.equals("")) {
			query  = q;
		}
		
		//DAO 작업
		//DAO 작업  예외 throws ClassNotFoundException, SQLException
		List<Notice> list = null;
		try {
				list = noticedao.getNotices(page, field, query);
				
		} catch (ClassNotFoundException e) {
					e.printStackTrace();
		} catch (SQLException e) {
					e.printStackTrace();
		}
		
		//list View 전달 (저장 >> 전달)
		//1. public ModelAndView .... 
		//   mv.addObject("list",list)
		//   mv.setViewName("notice.jsp")
		//   return mv
		
		model.addAttribute("list", list); // 자동으로 notice.jsp forward ....
		
		return "customer/notice";
		//  /WEB-INF/views/ +    + .jsp
	}
	
	@RequestMapping("noticeDetail.do") //noticeDetail.do?seq=${n.seq}
	public String noticesDetail(String seq , Model model) {
		
		Notice  notice = null;
		
		try {
			
			notice = noticedao.getNotice(seq);
		
		} catch (ClassNotFoundException e ) {
				e.printStackTrace();
		} catch (SQLException e) {
				e.printStackTrace();
		}
				
		model.addAttribute("notice", notice); //1건 데이터 ....	request.setAttribute	
		
		return "customer/noticeDetail";
	}
	
	@GetMapping(value="noticeReg.do")  //  /customer/noticeReg.do  >> 전송 >> GET
	public String noticeReg() {
		return "customer/noticeReg";
	}
	
	//form method="post" action="" 현재 주소창에 있는 주소
	//customer/noticeReg.do  전송 >> POST
	@PostMapping(value="noticeReg.do")  
	public String noticeReg(Notice n , HttpServletRequest request) {
		  
		    String filename =n.getFile().getOriginalFilename();
			String path = request.getServletContext().getRealPath("/customer/upload"); //배포된 서버 경로 
			String fpath = path + "\\" + filename;
			System.out.println(fpath);
			
			FileOutputStream fs =null;
			try {
				     fs = new FileOutputStream(fpath);
				     fs.write(n.getFile().getBytes());
				     
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				 try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//파일명 (DTO)
			n.setFileSrc(filename);
			
			try {
					noticedao.insert(n);  //DB insert
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			//insert 나 update 하고 나면 ...(F5 누르면 계속 글이 ..Write)
			//리스트 (location.href    or   redirect )
			//서버에게 새로운 요청 ...목록보기
			//String :   redirect:notice.htm   
			//Servlet , jsp  :   location.href  or   response.sendRedirect 
			
			
		  return "redirect:notice.do"; //요청 주소
	}
	
	//글 수정하기 (noticeEdit.do) ... select (글번호) > 기존 글을 화면 출력 > Model 사용해서 view 전달
	//글 수정하기 (화면이면서 데이터 처리) GET
	//noticeEdit.do
	//글번호 받기와     (String seq , Model model) 사용
	//noticeDetail.jsp 아래부분 링크 수정하기
	//<a class="btn-edit button" href="noticeEdit.do?seq=${notice.seq}">수정</a>
	//<a class="btn-del button" href="noticeDel.do?seq=${notice.seq}">삭제</a>
	@GetMapping(value="noticeEdit.do")  //   /customer/noticeEdit.do
	public String noticeEdit(String seq, Model model) {
		
		Notice  notice = null;
		
		try {
			notice = noticedao.getNotice(seq);
		} catch (ClassNotFoundException e ) {
				e.printStackTrace();
		} catch (SQLException e) {
				e.printStackTrace();
		}
				
		model.addAttribute("notice", notice); 		
		
		return "customer/noticeEdit";
	
	}
	
	
	
	
	//글 수정하기 (noticeEdit.do) .... update (Notice) > 글쓰기 (파일 업로드) 파일처리 (insert) 동일
	//DB 작업 update 처리 
	//글 상세보기 (기존 가지고 있던 글번호)  "redirect:noticeDetail.do?seq="+n.getSeq();
	//form method="post"
	@PostMapping("noticeEdit.do")
	public String noticeEdit(Notice n , HttpServletRequest request) {
			  //파일 업로드 가능
		 String filename =n.getFile().getOriginalFilename();
		 String path = request.getServletContext().getRealPath("/customer/upload"); //배포된 서버 경로 
		 String fpath = path + "\\" + filename;
		 System.out.println(fpath);
			
			FileOutputStream fs =null;
			try {
				     fs = new FileOutputStream(fpath);
				     fs.write(n.getFile().getBytes());
				     
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				 try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 기존 파일 수정하지 않으면 (기존 파일명 ... / 대체 이미지 논리) 해결
			//파일명 (DTO)
			n.setFileSrc(filename);
		
			try {
					noticedao.update(n);  //DB insert
				} catch (Exception e) {
					e.printStackTrace();
				} 
		//처리가 끝나면 상세 페이지로 : redirect  글번호를 가지고 ....
		return "redirect:noticeDetail.do?seq="+n.getSeq();    //서버에게 새 요청 ....
		}
}
 