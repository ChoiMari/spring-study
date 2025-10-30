package com.example.demo.domain;

import lombok.Data;
//클라이언트한테 username, password를 객체 형태로 받아서 가공.
@Data 
public class AuthenticationRequest {
	private String username;
	private String password;
	
}
