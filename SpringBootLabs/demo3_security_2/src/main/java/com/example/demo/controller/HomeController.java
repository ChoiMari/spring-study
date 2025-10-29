package com.example.demo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
	
	@GetMapping({"", "/"})
	public String home(Model model, Principal principal) {
		//인증 객체를 파라미터로 받을 수 있다 -> 프린시펄 객체
		// 프린시펄 객체는 인증정보가 있다
		String loginId= principal != null ? principal.getName() : "guest";
		model.addAttribute("loginId",loginId);
		return "index";
	}
	
}
