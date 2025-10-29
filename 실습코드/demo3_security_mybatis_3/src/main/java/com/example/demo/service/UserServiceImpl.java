package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserAuth;
import com.example.demo.dto.Users;
import com.example.demo.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl  implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	
	//회원가입 비밀번호 암호화
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public Users login(String username) {
		Users user = userMapper.login(username);
		return user;
	}

	 @Override
	 @Transactional //KEY POINT
	    public int join(Users user) throws Exception {
	        //비밀번호 암호화
	        String userPw = user.getUserPw();
	        String encodedUserPw = passwordEncoder.encode(userPw);
	        user.setUserPw(encodedUserPw);

	        //회원등록
	        int result = userMapper.join(user);

	        if(result > 0){
	          UserAuth userAuth = new UserAuth();
	          userAuth.setUserId(user.getUserId());
	          userAuth.setAuth("ROLE_USER");
	          result += userMapper.insertAuth(userAuth);
	        }
	        return result;
	    }


	@Override
	public int insertAuth(UserAuth userAuth) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
