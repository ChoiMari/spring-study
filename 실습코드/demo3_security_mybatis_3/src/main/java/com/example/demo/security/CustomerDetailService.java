package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CustomerUser;
import com.example.demo.dto.Users;
import com.example.demo.mapper.UserMapper;
//UserDetailsService는 **"사용자 이름(username)을 받아 사용자 정보(UserDetails)를 반환하는 인터페이스"**입니다.
//인증에 대한 처리 개발자가 원하는 대로 ...UserDetailsService  재정의 여러분 마음 : mybatis , jpa , 원하는 방법 제공
//loadUserByUsername 재정의

/*
사용자가 로그인 시도 (/login POST)
스프링 시큐리티는 내부적으로 UserDetailsService.loadUserByUsername() 호출
이 메서드를 통해 DB에서 사용자 정보를 가져옴
반환된 UserDetails 객체의 비밀번호, 권한 등을 기준으로 인증 진행 
*/

@Service
public class CustomerDetailService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//1. JPA
		//2. Mybatis > UserMapper
		
		Users user = userMapper.login(username); //실 DB조회 select ....
		if(user == null) {
			throw new UsernameNotFoundException("요청하신  ID 없습니다" + username);
		}
		
		//public class CustomerUser implements UserDetails 를 사용해서 
		CustomerUser customerUser = new CustomerUser(user);
		//getAuthorities() ,  getPassword()  등을 사용가능 
		return customerUser;
	}

}










