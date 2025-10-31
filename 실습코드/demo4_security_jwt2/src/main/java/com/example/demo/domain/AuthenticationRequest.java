package com.example.demo.domain;

import lombok.Data;

//클라이언트 username , password 객체 받아서
//가공

@Data
public class AuthenticationRequest {
	private String username;
	private String password;
}
