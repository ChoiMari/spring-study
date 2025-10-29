package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequestMapping("/user")
@Controller
public class AdminController {
	@GetMapping
	public String index() {
		log.info("[[/user]]");
		return "/user/index";
	}
}
