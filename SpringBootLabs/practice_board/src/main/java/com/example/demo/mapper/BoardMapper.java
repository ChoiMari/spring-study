package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.BoardRequest;
import com.example.demo.dto.BoardResponse;

@Mapper
public interface BoardMapper {
	 // 게시글 목록 조회(페이징)
    List<BoardResponse> selectBoardList(@Param("start") int start, @Param("end") int end);

    // 전체 게시글 개수 조회
    int countBoard();
    
    //게시글 단건 조회
    BoardResponse selectBoardById(@Param("id") Long id); 
    
    //게시글 등록
    int insertBoard(BoardRequest request);
    
    //게시글 수정
    int updateBoard(BoardRequest request);
    
    //게시글 삭제
    int deleteBoard(@Param("id") Long id);
}
