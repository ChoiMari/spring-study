package service;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import dao.MemberDao;
import lombok.RequiredArgsConstructor;
import vo.Member;

@Service
@RequiredArgsConstructor  //롬복
public class MemberService {
	/*
	private SqlSession sqlsession;

	@Autowired
	public void setSqlsession(SqlSession sqlsession) {
		this.sqlsession = sqlsession;
	}
	*/
	
	private final MemberDao deptMapper;
	
	public Member getMember(String userid) {
		Member member = null;
		try {
			////////////////////////////////////////////////////////////
			//MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
			
			
			//아니면 @Mapper
			//     public interface DeptMapper
			////////////////////////////////////////////////////////////

			member = deptMapper.getMember(userid);
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
			    //MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
				////////////////////////////////////////////////////////////
			 deptMapper.insert(member); // DB insert
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "redirect:/index.do";
	 }
}