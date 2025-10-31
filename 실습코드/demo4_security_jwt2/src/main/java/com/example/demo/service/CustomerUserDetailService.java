package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;

@Service
public class CustomerUserDetailService implements UserDetailsService {

	//사용자 정의 (mybatis, jpa) 사용시
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//DB에서 사용자 조회
		User user = userMapper.findByUsername(username);
		if(user == null) {
			//throw new UsernameNotFoundException("User not found with username :" + username);
			return null; 
		}
		 /*
		  	String username, 
			String password, 
			Collection <? extends GrantedAuthority> authorities 
		  */
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
		//role >> admin , user , super  	
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
			    user.getPassword(), 
			    authorities);
	}

}
