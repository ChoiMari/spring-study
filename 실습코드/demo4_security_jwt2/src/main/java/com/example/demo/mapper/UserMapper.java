package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.domain.User;


//원칙 : @Mapper > 인터페이스 > 함수(추상)
//    : userMapper.xml () 대신 아래처럼 단순한 작업 


@Mapper
public interface UserMapper {
	
	@Select("select * from user2 where username=#{username}")
	User findByUsername(String username);
	
	@Insert("insert into user2(username,password,role) values(#{username},#{password},#{role})" )
	void saveUser(User user);
}
