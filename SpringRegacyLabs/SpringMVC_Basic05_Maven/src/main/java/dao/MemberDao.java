package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vo.Member;

public interface MemberDao {
	public Member getMember(String uid) throws ClassNotFoundException, SQLException;
	
	public int insert(Member member) throws ClassNotFoundException, SQLException;
}
