package kr.or.kosa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kosa.dto.Dept;
import kr.or.kosa.service.DeptService;
import lombok.RequiredArgsConstructor;

@RestController //-> 모든 메서드 비동기
@RequiredArgsConstructor
@RequestMapping("/dept")
public class DeptController {
	private final DeptService deptSvc;
	
	//전체 조회
	@GetMapping
	public ResponseEntity<List<Dept>> deptList(){
		List<Dept> list = new ArrayList<Dept>();
		try {
			System.out.println("GET : deptList()");
			list = deptSvc.getDeptAll();
			System.out.println(list.toString());
			return new ResponseEntity<List<Dept>>(list, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("[예외] : " + e.getMessage());
			return new ResponseEntity<List<Dept>>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	//조건 조회
	@GetMapping("{deptno}")
	public ResponseEntity<Dept> selectDeptByDeptno(@PathVariable("deptno") int deptno){
		try {
			System.out.println("selectDeptByDeptno(deptno="+ deptno +")");
			return new ResponseEntity<Dept>(deptSvc.getDept(deptno), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("[예외] : " + e.getMessage());
			return new ResponseEntity<Dept>(HttpStatus.BAD_REQUEST);
		}
	}
	
	//삽입
	@PostMapping
	public ResponseEntity<String> insertDept(@RequestBody Dept dept){
		try {
			System.out.println("insertDept(dept=" + dept + ")");
			//Dept에서 toString() 오버라이드 있음
			deptSvc.insert(dept);
			return new ResponseEntity<String>("insert success", HttpStatus.CREATED);
		} catch (Exception e) {
			System.out.println("[예외] : " + e.getMessage());
			return new ResponseEntity<String>("insert fail", HttpStatus.BAD_REQUEST);
		}

	}
	
	//수정
	@PutMapping
	public ResponseEntity<String> updateDept(@RequestBody Dept dept){
		try {
			System.out.println("updateDept(dept=" + dept + ")");
			//Dept에서 toString() 오버라이드 있음
			deptSvc.update(dept);
			return new ResponseEntity<String>("update success", HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("[예외] : " + e.getMessage());
			return new ResponseEntity<String>("update fail", HttpStatus.BAD_REQUEST);
		}

	}
	
	//삭제
	@DeleteMapping("{deptno}")
	public ResponseEntity<String> deleteDeptByDeptno(@PathVariable("deptno") int deptno){
		try {
			System.out.println("deleteDeptByDeptno(deptno="+ deptno +")");
			deptSvc.delete(deptno);
			return new ResponseEntity<String>("delete success", HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("[예외] : " + e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
