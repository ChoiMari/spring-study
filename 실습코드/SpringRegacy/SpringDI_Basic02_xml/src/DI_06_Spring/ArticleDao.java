package DI_06_Spring;

//OracleDao , MysqlDao 클래스 사용한 공통함수 구현 강제(추상함수)
public interface ArticleDao {
	
	//CURD
	//Insert  가정
	
	void insert(Article article);
}
