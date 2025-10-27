package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 create table users(
    id number primary key,
    username varchar2(50),
    password varchar2(50),
    email varchar2(50)
); 

 */
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class User { // users 테이블과 매핑됨
	private Long id;
	private String username;
	private String password;
	private String email;
}
