package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public String listUsers(Model model) {
		// 스프링에서 지원하는 데이터를 담는 그릇(인터페이스)
		// 메서드의 파라미터로, 모델 인터페이스를 사용하면
		// 자동으로 모델 객체가 주입이 된다.
		// 모델 객체?
		// 1. ModelAndView : 데이터 담고 뷰를 정의
		// 근데, 데이터만 담고 싶다? -> Model인터페이스 사용
		// ->파라미터로 Model model을 하면 스프링이 객체를 넣어줍니다
		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);
		return "user/list";
	}

	@GetMapping("/new") // /users/new
	public String showCreateForm() {
		return "user/form"; // form.jsp
	}

	@PostMapping
	public String createUser(User user) {
		userService.createUser(user);
		return "redirect:/users";
		// /users요청으로 리다이렉트함
	}

	// 수정하기
	// 1. 기존 데이터 보여주기 select
	// 2. 데이터 수정하고 저장하기 udpate

	@GetMapping("/{id}/edit") // 기존 데이터 보여주기 (한건 데이터 출력) 동일
	public String showEditForm(@PathVariable("id") long id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);

		return "user/edit";
		// /WEB-INF/views/ + user/edit + .jsp
	}

	@PostMapping("/{id}/edit") // /users/${user.id}/edit
	public String updateUser(@PathVariable("id") long id, User user) {
		// user.setId(id);
		// user.setId(id); 문제 해결
		userService.updateUser(user);

		return "redirect:/users";

	}

	@GetMapping("/{id}/delete")
	public String deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);

		return "redirect:/users";
	}
}
