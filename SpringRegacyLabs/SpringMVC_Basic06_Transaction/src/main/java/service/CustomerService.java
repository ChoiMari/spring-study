package service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	//작성자의 포인트도 증가하는 서비스 추가
	// 글 등록 + 포인트 증가 -> 트랜잭션 처리
	@Transactional
	public String noticeReg(Notice n , HttpServletRequest request) throws Exception {
			  
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
				/*
				트랜잭션으로 묶을 DB작업 2가지
				1. noticeDao.insert(n) : insert
				2. noticeDao.updateOfMemberPoint("admin") : update
				
				commit 또는 rollback 되도록
				
				처리 방법?
				트랜잭션 매니저가 감시하고 있다가 걸리면 처리함
				롤백에 대한 디폴트가 Exception(예외 이벤트 발생 시 롤백 처리)
				문제는 롤백 되었을 때, 데이터가 돌아갈 공간이 없으면 안되어서 처리함
				
				1. @Transactional >> tx:annotation-driven 통해서 ...
				2. 둘다 성공(commit) , 둘중에 하나만 실패 (rollback)
				3. 처리하는 다양한 방법중 default 예외 발생하면 > rollback 
			    4. transaction-manager="transactionManager" 감시하고 있다가 예외가 발생하면 
			       모든 DML rollback .....
			
				 */
				
				    NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class); //추가
				    noticeDao.insert(n);  //DB insert
			
				    noticeDao.updateOfMemberPoint("admin"); //DB 포인트 증가
				    // 여기서 터지면 catch문
				    // 여기까지 만약 도달하면 정상 실행 되었다는 의미 - 정상권
				    System.out.println("글쓰기 insert 성공 + 포인트 update 성공");
			} catch (Exception e) {
				System.out.println("트랜잭션 예외 발생함 : 롤백 처리됨" + e.getMessage());
				e.printStackTrace();
				
				throw e; //호출부로 예외 던짐
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