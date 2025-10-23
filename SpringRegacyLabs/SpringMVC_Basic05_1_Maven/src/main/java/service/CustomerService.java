package service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.NoticeDao;
import vo.Notice;

@Service // -> 스프링 Ioc 컨테이너가 관리하는 빈 등록 목적
// componet-scan을 통해서 빈으로 등록 하려는 목적
public class CustomerService {
	// CustomerService는 SQL 세션 템플릿(sqlSession)에 의존한다(= 주소가 필요해요).
	private SqlSession sqlSession;
	// 루트 컨테이너 파일에서 미리 bean객체로 등록함

	/*
	 * 루트컨테이너 xml파일에 등록 시켜놓음 <bean id="sqlSession"
	 * class="org.mybatis.spring.SqlSessionTemplate"> <constructor-arg index="0"
	 * ref="sqlSessionFactoryBean"/> </bean> 싱글톤으로 유지되어서 close없이 그냥 쓰면 된다고 함
	 */
	@Autowired // 스프링 컨테이너 안에 같은 타입의 객체가 존재하면 자동 주입
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	// 서비스 코드는 DAO와 모양새는 거의 같다

	// 글 목록 보기 서비스
	// public String notices(String pg , String f , String q , Model model) {

	public List<Notice> notices(String pg, String f, String q) {
		int page = 1;
		String field = "TITLE";
		String query = "%%";

		if (pg != null && !pg.equals("")) {
			page = Integer.parseInt(pg);
		}

		if (f != null && !f.equals("")) {
			field = f;
		}

		if (q != null && !q.equals("")) {
			query = q;
		}

		List<Notice> list = null;
		try {
			// getMapper() : 아규먼트로 인터페이스의 형식을 제공해야함
			NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);

			// 마치 인터페이스를 그냥 사용하면 되는 것 처럼 편하게 사용하면 된다
			list = noticeDao.getNotices(page, field, query);
			// 마이바티스를 부른다. id와 메서드 이름이 같은 경우
			// mapper 사용

			// 더 간단한 코드도 있다고 함.

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;

	}

	// 글 상세보기 서비스
	public Notice noticesDetail(String seq) {

		Notice notice = null;

		try {

			NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); // 추가
			notice = noticeDao.getNotice(seq);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notice;
	}

	// 글 쓰기 서비스
	public String noticeReg(Notice n, HttpServletRequest request) {

		String filename = n.getFile().getOriginalFilename();
		String path = request.getServletContext().getRealPath("/customer/upload"); // 배포된 서버 경로
		String fpath = path + "\\" + filename;
		System.out.println(fpath);

		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(fpath);
			fs.write(n.getFile().getBytes());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 파일명 (DTO)
		n.setFileSrc(filename);

		try {
			NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); // 추가
			noticeDao.insert(n); // DB insert

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:notice.do"; // 요청 주소
	}

	// 글 수정하기 서비스
	public Notice noticeEdit(String seq) {

		Notice notice = null;

		try {
			NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); // 추가
			notice = noticeDao.getNotice(seq);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notice;

	}

	// 글 수정하기 처리 서비스
	public String noticeEdit(Notice n, HttpServletRequest request) {
		// 파일 업로드 가능
		String filename = n.getFile().getOriginalFilename();
		String path = request.getServletContext().getRealPath("/customer/upload"); // 배포된 서버 경로
		String fpath = path + "\\" + filename;
		System.out.println(fpath);

		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(fpath);
			fs.write(n.getFile().getBytes());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 기존 파일 수정하지 않으면 (기존 파일명 ... / 대체 이미지 논리) 해결
		// 파일명 (DTO)
		n.setFileSrc(filename);

		try {
			NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); // 추가
			noticeDao.update(n); // DB update
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 처리가 끝나면 상세 페이지로 : redirect 글번호를 가지고 ....
		return "redirect:noticeDetail.do?seq=" + n.getSeq(); // 서버에게 새 요청 ....
	}

	// 글 삭제하기 서비스
	public String noticeDel(String seq) {

		NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); // 추가\

		try {
			noticeDao.delete(seq);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:notice.do";

	}

}
