package kr.or.kosa.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.kosa.dao.EmpDao;
import kr.or.kosa.dto.Emp;

@Service
public class EmpService {
	
	private SqlSession sqlsession;

	@Autowired
	public void setSqlsession(SqlSession sqlsession) {
		this.sqlsession = sqlsession;
	}
	
	/*
	int insert(Emp emp);
	
	List<Emp> select();
	
	Emp selectByEmpno(int empno);
	
	int update(Emp empno);
	
	int delete(int empno);  
	  
	*/
	
	public List<Emp> selectAllEmpList(){
		EmpDao empdao = sqlsession.getMapper(EmpDao.class);
		//인터터이스를 mapper 구현했다(mybatis)
		List<Emp> list = empdao.select();
		return list;
	}
	
	public Emp selectEmpByEmpno(int empno) {
		EmpDao empdao = sqlsession.getMapper(EmpDao.class);
		return empdao.selectByEmpno(empno);
	}	
	
	//작성해 보세요
	
	public int insert(Emp emp) {
		EmpDao empdao = sqlsession.getMapper(EmpDao.class);
		return  empdao.insert(emp);
	}
	
	public int update(Emp emp) {
		EmpDao empdao = sqlsession.getMapper(EmpDao.class);
		return  empdao.update(emp);
	}
	
	public int delete(int empno) {
		EmpDao empdao = sqlsession.getMapper(EmpDao.class);
		return  empdao.delete(empno);
	}
	
}
