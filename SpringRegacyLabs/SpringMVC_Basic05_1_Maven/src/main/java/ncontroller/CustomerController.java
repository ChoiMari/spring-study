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
import service.CustomerService;
import vo.Notice;

@Controller
@RequestMapping("/customer/")  //부분 경로 설정 가능 
public class CustomerController {
	
	//컨트롤러는 서비스를 의존합니다
	//= 컨트롤러가 작동하기 위해서는 서비스의 주소가 필요합니다
	private CustomerService customerService;
	
	@Autowired
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@RequestMapping("notice.do")
	public String notices(String pg, String f, String q, Model model) {
		
		List<Notice> list = customerService.notices(pg, f, q);
		model.addAttribute("list", list);
		return "customer/notice";
	}
	
	@RequestMapping("noticeDetail.do") 
	public String noticesDetail(String seq , Model model) {
		Notice  notice =  customerService.noticesDetail(seq);
		model.addAttribute("notice", notice); 
		return "customer/noticeDetail";
	}
	
	@GetMapping(value="noticeReg.do")
	public String noticeReg() {
		return "customer/noticeReg";
	}
	
	
	@PostMapping(value="noticeReg.do")  
	public String noticeReg(Notice n , HttpServletRequest request) {
		 String url = null;
		 
		 try {
			    url = customerService.noticeReg(n, request);
			    
		} catch (Exception e) {
			    e.printStackTrace();
		}
		 
		 return url;
		 
	}
	
	
	@GetMapping(value="noticeEdit.do") //수정하기 화면 (데이터)
	public String noticeEdit(String seq, Model model) {
		
		Notice  notice = null;
		
		try {
			  notice = customerService.noticeEdit(seq);
		} catch (Exception e ) {
			  e.printStackTrace();
		} 
		
		model.addAttribute("notice", notice); 		
		return "customer/noticeEdit";
	
	}
	
	
	@PostMapping("noticeEdit.do")  //update 
	public String noticeEdit(Notice n , HttpServletRequest request) {
		 return customerService.noticeEdit(n, request);
	}

	@GetMapping("noticeDel.do")
	public String noticeDel(String seq) {
		return customerService.noticeDel(seq);
	}

}


 