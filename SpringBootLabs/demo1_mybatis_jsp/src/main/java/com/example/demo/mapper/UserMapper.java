package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.User;

@Mapper
public interface UserMapper {
	
	//<mapper namespace="com.example.demo.mapper.UserMapper"> 맞춰야함
	// Mapper인터페이스 메서드명 = .xml id 맞추기
	List<User> selectAll();
	User selectById(Long id);
	void insert(User user);
	void delete(Long id);
	void update(User user);
}
