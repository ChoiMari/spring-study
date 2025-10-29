package com.example.demo.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/*
Spring security 에서 사용자 정보를 직접 정의 하겠다 (UserDetails 구현)
*/
public class CustomerUser implements UserDetails {

	//사용자 DTO
	private Users user;
	
	public CustomerUser(Users user) {
		this.user = user;
	}
	
	
	
	//현재 로그인한 사용자의 권한 정보 추출
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
			
		return user.getAuthList()  //권한 (ROLE_USER , ROLE_ADMIN , ROLE_SUPER...)
				   .stream()
				   .map((auth) -> new SimpleGrantedAuthority(auth.getAuth()))
				   //배열 생성 [UserAuth][UserAuth][UserAuth][UserAuth] .....
				   .collect(Collectors.toList());
		
					//권한 정보 리스트를 만들어서 리턴 (GrantedAuthority 객체형식 타입으로 ...)
					//권한 정보를 수집해서 리턴 .....
					
		            //1.user.getAuthList() >>>   List<UserAuth> authList; 
					//2.map 통해서 UserAuth 객체안에서 권한정보만 추출
					//3. [ROLE_USER][ROLE_ADMIN]
	}
	

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getUserPw();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUserId();
	}
	/*
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled() == 0 ? false : true;
    }
    */

}
