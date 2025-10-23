package ncontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.MemberDao;
import service.MemberService;
import vo.Member;

@Controller
@RequestMapping("/joinus/")
public class JoinController {
private MemberService memberService;
	
	@Autowired
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}


	//GET 요청
	//join.jsp
	@GetMapping("join.do")  //   /joinus/join.do
	public String join() {
		return "joinus/join";
	}
	
	
	//POST 요청
	@PostMapping("join.do")  //   /joinus/join.do 
	public String join(Member member) {
		System.out.println(member.toString());
		String url = null;
		try {
			  url = memberService.insert(member);
		} catch (Exception e) {
			  e.printStackTrace();
		}
		return url;
	}

	
	//로그인 처리 
	//@GetMapping("login.do") 
}
