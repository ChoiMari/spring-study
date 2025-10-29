package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CustomerUser;
import com.example.demo.dto.Users;
import com.example.demo.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

//로그인하면 로그인 아이디와 비밀번호 가로채서 토큰에 넣고
// UserDetailsService한테 전달
//원래는 자동화 되었는데 마이바티스 쓴다고 이걸 이제 수동으로 고쳐주어야한다고 함

//UserDetailsService는 
//**"사용자 이름(username)을 받아 사용자 정보(UserDetails)를 반환하는 인터페이스"**입니다.
//인증에 대한 처리 개발자가 원하는 대로 ...
//UserDetailsService  재정의 여러분 마음 : mybatis , jpa , 원하는 방법 제공
//loadUserByUsername 재정의

/*
사용자가 로그인 시도 (/login POST)
스프링 시큐리티는 내부적으로 UserDetailsService.loadUserByUsername() 호출
이 메서드를 통해 DB에서 사용자 정보를 가져옴
반환된 UserDetails 객체의 비밀번호, 권한 등을 기준으로 인증 진행 

*/
@RequiredArgsConstructor
@Service
public class CustomerDetailService implements UserDetailsService{
//UserDetailsService : 아이디와 패스워드를 받아서 검증하는 역할을 하는 인터페이스
	private final UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 1. JPA 
		// 2. Mybatis -> UserMapper
		Users user = userMapper.login(username); //실 DB조회 select...
		if(user == null) {
			throw new UsernameNotFoundException("요청한 아이디가 없습니다." + username);
		}
		
		//public class CustomerUser implements UserDetails를 
		//사용해서 객체를 만들어서 넘김
		CustomerUser customerUser = new CustomerUser(user);
		//getAuthorities()
		//getPassword() 등 사용 가능
		return customerUser;
	}
	
}
