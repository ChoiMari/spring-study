package com.example.demo.domain;

import lombok.Data;

/*
create table user2(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL 
);


select * from user2;
  
  
 */
@Data
public class User {
   private long id;
   private String username;
   private String password;
   private String role;
}
