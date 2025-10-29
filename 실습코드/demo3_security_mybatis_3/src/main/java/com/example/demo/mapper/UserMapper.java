package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.dto.UserAuth;
import com.example.demo.dto.Users;

@Mapper
public interface UserMapper {
	//로그인 사용자 인증
	Users login(String username);
	
	//회원가입
	int join(Users user) throws Exception;
	
	
	//회원가입 권한 등록 (트랜잭션 , PL-SQL (trigger)
	int insertAuth(UserAuth userAuth) throws Exception;
}
