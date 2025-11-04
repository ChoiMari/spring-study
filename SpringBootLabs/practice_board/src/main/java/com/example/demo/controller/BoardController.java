package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.BoardRequest;
import com.example.demo.dto.BoardResponse;
import com.example.demo.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
	private final BoardService boardService;

	// 게시글 목록(페이징)
	@GetMapping
	public ResponseEntity<ApiResponse<Map<String, Object>>> getBoards(
	        @RequestParam(name = "page", defaultValue = "1") int page,
	        @RequestParam(name = "size", defaultValue = "10") int size) {

	    List<BoardResponse> boards = boardService.getBoardList(page, size);
	    int totalCount = boardService.getBoardCount();

	    // 응답 데이터 구성
	    Map<String, Object> data = new HashMap<>();
	    data.put("boards", boards);
	    data.put("totalCount", totalCount);

	    if (boards.isEmpty()) {
	        return ResponseEntity.ok(ApiResponse.fail("등록된 게시글이 없습니다."));
	    }

	    return ResponseEntity.ok(ApiResponse.success(data));
	}

	// 단건 조회
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BoardResponse>> getBoardById(@PathVariable("id") Long id) {
		BoardResponse board = boardService.getBoardById(id);

		if (board == null) {
			return ResponseEntity.ok(ApiResponse.fail("해당 게시글을 찾을 수 없습니다."));
		}

		return ResponseEntity.ok(ApiResponse.success(board));
	}

	// 등록 //-> 등록 성공 시 data에 새로 생성된 id 반환
	@PostMapping
	public ResponseEntity<ApiResponse<Map<String, Object>>> createBoard(@RequestBody BoardRequest request) {
        int result = boardService.createBoard(request);

        if (result == 0) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("게시글 등록 실패"));
        }

        // ✅ MyBatis selectKey로 채워진 id 응답으로 포함
        Map<String, Object> data = new HashMap<>();
        data.put("id", request.getId());
        data.put("message", "게시글이 등록되었습니다.");

        return ResponseEntity.ok(ApiResponse.success(data));
	}

	// 수정 
	@PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateBoard(
            @PathVariable("id") Long id, @RequestBody BoardRequest request) {

        request.setId(id);
        int result = boardService.updateBoard(request);

        if (result == 0) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("게시글 수정 실패"));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("title", request.getTitle());
        data.put("writer", request.getWriter());
        data.put("message", "게시글이 수정되었습니다.");

        return ResponseEntity.ok(ApiResponse.success(data));
    }
	// 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable("id") Long id) {
        int result = boardService.deleteBoard(id);

        if (result == 0) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("게시글 삭제 실패"));
        }

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
