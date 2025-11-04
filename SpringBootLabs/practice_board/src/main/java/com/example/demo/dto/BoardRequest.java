package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * 
CREATE TABLE board (
    id NUMBER PRIMARY KEY, --게시글 번호(PK)
    title VARCHAR2(200), -- 제목
    writer VARCHAR2(100), -- 작성자
    content CLOB, -- 본문
    created_at DATE DEFAULT SYSDATE --작성일시
);

 
 */
@Getter @Builder @ToString @Setter @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public class BoardRequest {
	private Long id; //수정 시 필요함
	private String title;
	private String writer;
	private String content;
}
