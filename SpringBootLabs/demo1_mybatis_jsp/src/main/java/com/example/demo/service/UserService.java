package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class UserService {
	//UserMapper 의존합니다
	// = 주소가 필요합니다
	
	private final UserMapper userMapper;
	//스프링은 원칙적으로 setter주입 또는 contructor주입
	// member field 주입도 가능함(@Autowired)
	
	public List<User> getAllUsers(){
		return userMapper.selectAll(); 
		// @Mapper를 통해 연결됨(= 주입받아서 주소를 가지고 있음)
	}
	public User getUserById(long id) {
		return userMapper.selectById(id);
	}
	
	public void createUser(User user) {
		userMapper.insert(user);
	}
	
	public void updateUser(User user) {
		userMapper.update(user);
	}
	
	public void deleteUser(long id) {
		userMapper.delete(id);
	}
	
	
}
