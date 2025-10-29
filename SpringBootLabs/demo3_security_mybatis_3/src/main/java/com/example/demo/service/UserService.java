package com.example.demo.service;

import com.example.demo.dto.UserAuth;
import com.example.demo.dto.Users;

//CRUD 메서드(추상 메서드)
public interface UserService {
	//로그인 사용자 인증을 처리하는 메서드
	Users login(String username); // 로그인 아이디로 select
	
	//회원 가입
	int join(Users user) throws Exception;
	
	//회원 가입 권한 등록(트랜잭션, PL/SQL 트리거)
	int insertAuth(UserAuth usre) throws Exception;
}
