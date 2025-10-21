package com.service;

import org.springframework.stereotype.Service;

import com.model.NewArticleCommand;

/*
@Service 
public class ArticleService 

@Service 붙어있는 클래스를 빈객체로 만들 수 있다.
@Service 붙어 있으면 컴포넌트 스캔을 통해서 자동 빈객체 생성이 가능하다.

<context:component-scan base-package="com.service">
스캐너처럼 쫙 긁고 @Service 붙어 있으면 자동으로 bean생성(-> 스프링이 객체 생성해서 관리함)
 */
@Service 
public class ArticleService {
	public ArticleService() {
		System.out.println("ArticleService 서비스의 생성자 호출");
	}
	
	public void writeArticle(NewArticleCommand command) {
		// DAO가 있다고 가정함
		// Dao dao = new Dao(); 
		// dao.insert(command);
		System.out.println("글쓰기 작업 완료 : " + command.toString());
	}
}
