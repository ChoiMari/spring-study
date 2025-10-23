package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import vo.Emp;

@Repository
public interface EmpDao {
	// 인터페이스 - 기본 public abstract 생략
	int insertEmp(Emp e); //insert
	int updateEmp(Emp e); //update
	int deleteEmpByEmpno(@Param("empno") int empno); //delete
	
	List<Emp> getEmpList(); //전체 조회
	Emp getEmp(@Param("empno") int empno);//조건조회
}
