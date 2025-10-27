package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Dept;
import com.example.demo.repository.DeptRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeptService {
	private final DeptRepository deptRepo;
	
	public List<Dept> findAll(){
		
	return deptRepo.findAll();
	//findAll() : (제공한 Entity 클래스를 기반으로)JPA가 자동으로 만들어 준 메서드
	}
	
	public Dept findById(int deptno) {
		return deptRepo.findById(deptno).orElse(null);
		//값이 없으면 null을 리턴하겠다.
	}
	
	public void save(Dept dept) { //insert & update
		deptRepo.save(dept);
	}
	//update란 ?
	// 기존 데이터 delete + 다시 insert 하는 것
	
	public void delete(int deptno) {
		deptRepo.deleteById(deptno);
	}
}
