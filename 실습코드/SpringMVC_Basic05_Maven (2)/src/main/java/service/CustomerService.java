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

@Service  //@Service  > component-scan 통해서 빈으로 등록 할려는 목적 
public class CustomerService {

	//CustomerService 는 sqlSession 에 의존
	
	private SqlSession sqlSession;
                 //<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
	@Autowired   //Spring 컨테이너 안에 같은 타입의 객체가 존재하면 자동 주입
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	//서비스 코드 (DAO) 와 모양새는 거의 같다 
	
	//글목록보기 서비스
	public List<Notice> notices(String pg , String f , String q) {
		
		int page = 1;
		String field="TITLE";
		String query = "%%";
		
		if(pg != null   && ! pg.equals("")) {
			page  = Integer.parseInt(pg);
		}
		
		if(f != null   && ! f.equals("")) {
			field = f;
		}
		
		if(q != null   && ! q.equals("")) {
			query  = q;
		}
		
	
		List<Notice> list = null;
		try {
				// list = noticedao.getNotices(page, field, query); 기존 Dao 사용했던 코드
			
				// mybatis  /////////////////////////////////////////////////
			    NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
			    //마치 인터페이스를 그냥 사용하면 되는 것처럼 편하게
			    list =  noticeDao.getNotices(page,field,query); //mapper 사용
			    //////////////////////////////////////////////////////////////
				
		} catch (ClassNotFoundException e) {
					e.printStackTrace();
		} catch (SQLException e) {
					e.printStackTrace();
		}
		
		return list;
	}
	//글 상세보기 서비스
	public Notice noticesDetail(String seq) {
			
			Notice  notice = null;
			
			try {
				 
				 NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); //추가
				 notice = noticeDao.getNotice(seq);
			
			} catch (ClassNotFoundException e ) {
					e.printStackTrace();
			} catch (SQLException e) {
					e.printStackTrace();
			}
					
			return notice;
		}

	//글 쓰기 서비스
	public String noticeReg(Notice n , HttpServletRequest request) {
			  
		    String filename =n.getFile().getOriginalFilename();
			String path = request.getServletContext().getRealPath("/customer/upload"); //배포된 서버 경로 
			String fpath = path + "\\" + filename;
			System.out.println(fpath);
			
			FileOutputStream fs =null;
			try {
				     fs = new FileOutputStream(fpath);
				     fs.write(n.getFile().getBytes());
				     
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				 try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//파일명 (DTO)
			n.setFileSrc(filename);
			
			try {
				    NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); //추가
				    noticeDao.insert(n);  //DB insert
			
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
				
			
		  return "redirect:notice.do"; //요청 주소
	}

	//글 수정하기 서비스
	public Notice noticeEdit(String seq) {
			
			Notice  notice = null;
			
			try {
				  NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); //추가
				  notice = noticeDao.getNotice(seq);
				  
			} catch (ClassNotFoundException e ) {
					e.printStackTrace();
			} catch (SQLException e) {
					e.printStackTrace();
			}

			return notice;
		
		}

	//글 수정하기 처리 서비스
	public String noticeEdit(Notice n , HttpServletRequest request) {
			  //파일 업로드 가능
		 String filename =n.getFile().getOriginalFilename();
		 String path = request.getServletContext().getRealPath("/customer/upload"); //배포된 서버 경로 
		 String fpath = path + "\\" + filename;
		 System.out.println(fpath);
			
			FileOutputStream fs =null;
			try {
				     fs = new FileOutputStream(fpath);
				     fs.write(n.getFile().getBytes());
				     
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				 try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 기존 파일 수정하지 않으면 (기존 파일명 ... / 대체 이미지 논리) 해결
			//파일명 (DTO)
			n.setFileSrc(filename);
		
			try {
				     NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); //추가
				     noticeDao.update(n);  //DB update
				} catch (Exception e) {
					e.printStackTrace();
				} 
		 //처리가 끝나면 상세 페이지로 : redirect  글번호를 가지고 ....
		  return "redirect:noticeDetail.do?seq="+n.getSeq();    //서버에게 새 요청 ....
		}
		
	//글 삭제하기 서비스
	public String noticeDel(String seq) {
			
			 NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); //추가\
			 
			 try {
				    noticeDao.delete(seq);
			} catch (Exception e) {
				    e.printStackTrace();
			}
			
			return "redirect:notice.do";
			
		}
		
	}