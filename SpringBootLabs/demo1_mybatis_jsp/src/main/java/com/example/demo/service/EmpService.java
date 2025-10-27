package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.mapper.EmpMapper;
import com.example.demo.model.EmpDeptDto;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor // 롬복 어노테이션 통해서 주입
public class EmpService {
	private final EmpMapper empMapper;
	
	/*
	public EmpService(EmpMapper empMapper) {
		this.empMapper = empMapper;
	}
	*/
	
	public List<EmpDeptDto> getAllEmpWithDept(){
		return empMapper.findAllWithDept();
	}
}
