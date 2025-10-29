package com.example.demo.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
/*
 Spring security에서 사용자 정보를 직접 관리하겠다(UserDetails 인터페이스 구현)
  
 */
public class CustomerUser implements UserDetails {
	//사용자 DTO
	private Users user; // 받고 싶은 정보는 여기에 들어 있음
	
	//주입 받음
	public CustomerUser(Users user) {
		this.user = user;
	}
	
	//현재 로그인 한 사용자의 권한 정보를 추출하고 싶음..
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//Collection<? extends GrantedAuthority> 제네릭 
		// : GrantedAuthority를 상속한 타입이 올 수 있다고 명시함
		
		return user.getAuthList() //user안에 있는 getter함수 호출
				// 권한 목록이 들어 있음.(ROLE_USER, ROLE_ADMIN, ROLE_SUPER, ...)
				.stream()
				.map(auth -> new SimpleGrantedAuthority(auth.getAuth())) 
				// 새로운 배열을 만듬 [UserAuth][UserAuth][UserAuth][UserAuth][UserAuth]
				// 권한 정보 리스트를 만들어서 반환해라(extends GrantedAuthority 객체 타입으로)
				.collect(Collectors.toList());
		
		//권한 정보를 수집해서 반환함
		// user.getAuthList() -> 리턴 타입 List<UserAuth> 권한 목록 뽑음
		// map을 통해서 UserAuth 객체 안에서 권한 정보만 추출
		// [ROLE_USER][ROLE_ADMIN]
	}

	@Override
	public String getPassword() {
	
		return user.getUserPw();
	}

	@Override
	public String getUsername() {
		
		return user.getUserId();
	}

}
