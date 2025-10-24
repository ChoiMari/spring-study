package kr.or.kosa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.kosa.dto.Dept;

@Mapper //-> 서비스에서 SqlSession 객체 주입 자동으로 해줌 
public interface DeptDao {
	//기본 public abstract 추상 메서드
	
	List<Dept> getDeptAll(); // 전체조회
	Dept getDept(int deptno); // 조건조회
	int insert(Dept dept);//삽입
	int update(Dept dept);//수정
	int delete(int deptno);//삭제
}
