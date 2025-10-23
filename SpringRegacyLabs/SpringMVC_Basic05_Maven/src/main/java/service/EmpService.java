package service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.EmpDao;
import vo.Emp;

@Service
public class EmpService {
	private SqlSession sqlsession;
	@Autowired
	public void setSqlsession(SqlSession sqlsession) {
		this.sqlsession = sqlsession;
	}
	
	//전체 조회
	public List<Emp> getEmpList() {
		EmpDao dao = sqlsession.getMapper(EmpDao.class);
		List<Emp> empList = dao.getEmpList();
		System.out.println(empList.toString());
		return empList;
	}
	
	//조건 조회
	public Emp getEmpByEmpno(int empno) {
		EmpDao dao = sqlsession.getMapper(EmpDao.class);
		return dao.getEmp(empno);
	}
	
	//insert
	public void createEmp(Emp e) {
		EmpDao dao = sqlsession.getMapper(EmpDao.class);
		dao.insertEmp(e);
	}
	
	//update
	public void updateEmp(Emp e) {
		EmpDao dao = sqlsession.getMapper(EmpDao.class);
		dao.updateEmp(e);
	}
	
	//delete
	public void deleteEmpByEmpno(int empno) {
		EmpDao dao = sqlsession.getMapper(EmpDao.class);
		dao.deleteEmpByEmpno(empno);
	}
	
	
}
