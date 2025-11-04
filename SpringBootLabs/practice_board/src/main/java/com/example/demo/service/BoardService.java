package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BoardRequest;
import com.example.demo.dto.BoardResponse;
import com.example.demo.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j @RequiredArgsConstructor
public class BoardService {
	private final BoardMapper boardMapper;
	
	//전체 목록 조회(페이징)
    public List<BoardResponse> getBoardList(int page, int pageSize) {
        int start = (page - 1) * pageSize; // 0부터 시작
        int end = page * pageSize;
        return boardMapper.selectBoardList(start, end);
    }
    // 게시글 개수
    public int getBoardCount() {
        return boardMapper.countBoard();
    }
    // 단건 조회
    public BoardResponse getBoardById(Long id) {
        return boardMapper.selectBoardById(id);
    }
    //게시글 등록
    public int createBoard(BoardRequest req) {
        return boardMapper.insertBoard(req);
    }
    //게시글 수정
    public int updateBoard(BoardRequest req) {
        return boardMapper.updateBoard(req);
    }
    //게시글 삭제
    public int deleteBoard(Long id) {
        return boardMapper.deleteBoard(id);
    }
}
