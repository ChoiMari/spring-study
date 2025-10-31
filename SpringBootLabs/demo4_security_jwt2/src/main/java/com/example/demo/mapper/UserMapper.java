package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.domain.User;

/*
 원칙 : @Mapper 인터페이스 -> 추상 메서드
  		mapper.xml -> SQL문
  이 2개를 통합 할 수 있다.
  
  xml대신 추상메서드 위에 @어노테이션으로 대체
 */
@Mapper
public interface UserMapper {
	@Select("select * from user2 where username=#{username}")
	User findByUsername(String username);
	@Insert("insert into user2(username, password, role) values(#{username}, #{password}, #{role})")
	void saveUser(User user);
}
