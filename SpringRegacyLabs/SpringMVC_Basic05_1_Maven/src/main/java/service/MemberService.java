package service;


import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.MemberDao;
import vo.Member;

@Service
public class MemberService {
	
	private SqlSession sqlsession;

	@Autowired
	public void setSqlsession(SqlSession sqlsession) {
		this.sqlsession = sqlsession;
	}
	
	public Member getMember(String userid) {
		Member member = null;
		try {
			////////////////////////////////////////////////////////////
			MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
			
			//아니면 @Mapper
			//     public interface DeptMapper
			////////////////////////////////////////////////////////////

			member = memberdao.getMember(userid);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return member;
	}
	
	public String insert(Member member) {
		 try {
				////////////////////////////////////////////////////////////
			 MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
				////////////////////////////////////////////////////////////
			 memberdao.insert(member); // DB insert
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "redirect:/index.htm";
	 }

}
