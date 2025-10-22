package ncontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/index.do")
	public String index() {
		System.out.println("index.do");
		return "index";
		
		//   /WEB-INF/views/ + index + .jsp
	}
}
