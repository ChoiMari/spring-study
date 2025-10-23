package dao;

import java.sql.SQLException;

import vo.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
	public Member getMember(String userid) throws ClassNotFoundException, SQLException;
	public int insert(Member member) throws ClassNotFoundException, SQLException;
	
}
