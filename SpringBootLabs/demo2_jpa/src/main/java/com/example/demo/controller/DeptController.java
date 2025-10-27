package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Dept;
import com.example.demo.service.DeptService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dept")
public class DeptController {
	private final DeptService deptSvc;
	
	@GetMapping
	public String list(Model model) {
		model.addAttribute("list", deptSvc.findAll());
		return "dept/list";
		//src/main/resources/templates/dept/list.html
		//접두사 : src/main/resources/templates/
		// 접미사 : .html까지 기본값으로 잡혀있음(따로 설정이 필요없다)
	}
	
	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("dept", new Dept());
		return "dept/add";
	}
	
	@PostMapping("/add")
	public String add(Dept dept) {
		//dto로 받는건 넘어오는 태그의 name 값들이 멤버필드명과 같으면 
		// 자동 바인딩 된다.
		deptSvc.save(dept);
		
		return "redirect:/dept";
	}
	
	@GetMapping("/edit/{deptno}")
	public String edit(@PathVariable("deptno") int deptno, 
			Model model) {
		model.addAttribute("dept", deptSvc.findById(deptno));
		return "dept/edit";
	}
	
	@PostMapping("/edit")
	public String edit(Dept dept) {
		deptSvc.save(dept); //update
		return "redirect:/dept";
	}
	
	@GetMapping("/delete/{deptno}")
	public String delete(@PathVariable("deptno") int deptno) {
		deptSvc.delete(deptno);
		return "redirect:/dept";
	}
	
	@GetMapping("/detail/{deptno}")
	public String detail(@PathVariable("deptno") int deptno,
			Model model) {
		
		model.addAttribute("dept", deptSvc.findById(deptno));

		return "dept/detail";
	}

}
