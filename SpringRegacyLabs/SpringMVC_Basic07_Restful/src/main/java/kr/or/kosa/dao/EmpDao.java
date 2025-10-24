package kr.or.kosa.dao;
// EmpMapper로 지어도 된다 보통 매퍼를 사용하면 이렇게 짓는다고 함
// 회사 마다 스타일이 다르다

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.kosa.dto.Emp;
@Mapper
public interface EmpDao {
	//Mapper와 연동할 추상 메서드(CRUD)
	
	List<Emp> select();
	Emp selectByEmpno(int empno);
	int insert(Emp emp);
	int update(Emp emp);
	int delete(int empno);
}
