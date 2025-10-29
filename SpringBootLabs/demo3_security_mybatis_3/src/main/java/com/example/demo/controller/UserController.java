package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequestMapping("/admin")
@Controller
public class UserController {
	@GetMapping
	public String index() {
		log.info("[[/admin]]");
		return "/admin/index";
	}
}
