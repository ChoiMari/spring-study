package kr.or.kosa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.kosa.dao.DeptDao;
import kr.or.kosa.dto.Dept;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeptService {
	private final DeptDao deptDao;
	
	//전체 조회
	public List<Dept> getDeptAll(){
		return deptDao.getDeptAll();
	}
	//조건 조회
	public Dept getDept(int deptno) {
		return deptDao.getDept(deptno);
	}
	//삽입
	public int insert(Dept dept) {
		return deptDao.insert(dept);
	}
	//수정
	public int update(Dept dept) {
		return deptDao.update(dept);
	}
	
	//삭제
	public int delete(int deptno) {
		return deptDao.delete(deptno);
	}
}
