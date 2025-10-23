package ncontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import service.MemberService;
import vo.Member;

@Controller
@RequiredArgsConstructor
@RequestMapping("/joinus/")
public class JoinController {
	
	private  final MemberService memberService;

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
