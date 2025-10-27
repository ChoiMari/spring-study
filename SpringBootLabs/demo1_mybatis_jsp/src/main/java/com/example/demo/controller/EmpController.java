package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.EmpDeptDto;
import com.example.demo.service.EmpService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/emp") 
@RequiredArgsConstructor
public class EmpController {
	
	private final EmpService empService;
	
	@GetMapping
	public String getEmpList(Model model) {
		model.addAttribute("emps", empService.getAllEmpWithDept());
		return "emp/list";
	}
}
