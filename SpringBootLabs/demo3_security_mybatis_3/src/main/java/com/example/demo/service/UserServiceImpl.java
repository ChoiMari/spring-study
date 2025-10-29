package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserAuth;
import com.example.demo.dto.Users;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// 트랜잭션 처리 - 서비스 계층
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
//UserServiceImpl는 UserMapper에 의존합니다(작동이 되기 위해서는 UserMapper의 주소가 필요합니다)
	
	private final UserMapper userMapper;
	
	// 회원가입 하려면 비밀번호 암호화가 필요하다
	private final PasswordEncoder pwEncoder; //시큐리티 설정에서 bean으로 등록해놓아서
	// 스프링 컨테이너에 존재하기 때문에 스프링이 주입해준다
	
	@Override
	public Users login(String username) {
		Users user = userMapper.login(username);
		
		return user;
	}

	//회원가입 *
	@Override
	@Transactional
	public int join(Users user) throws Exception {
		// 지금 회원가입 insert + 권한 부여 insert 하고 있음
		//강사님은 트랜잭션말고 트리거 쓰신다고 하심
		//1. 비밀번호 암호화
		String userPw = user.getUserPw();
		String encodedUserPw = pwEncoder.encode(userPw);
		user.setUserPw(encodedUserPw);
		
		int result = 0;
		try {
			result = userMapper.join(user); // 작업1 - 회원가입 insert
			if(result > 0) {
				UserAuth userAuth = new UserAuth();
				userAuth.setUserId(user.getUserId());
				userAuth.setAuth("ROLE_USER");
				result += userMapper.insertAuth(userAuth); // 작업2 - 권한 insert
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		
		
		return result;
	}

	@Override
	public int insertAuth(UserAuth usre) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
