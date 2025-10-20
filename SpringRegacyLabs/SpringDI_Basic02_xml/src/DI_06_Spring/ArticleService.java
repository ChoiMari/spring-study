package DI_06_Spring;


//ArticleService 는 ArticleDao 의존한다
//연관관계 (복합, 집합) 여러분이 결정

public class ArticleService {

	private ArticleDao articleDao; //주입 (객체의 주소) 현재  null
	//주입 (생성자 ,  setter)
	public ArticleService(ArticleDao articleDao) { //다형성 ... ArticleDao 구현하는 모든 객체의 주소(자식)
		this.articleDao = articleDao;
		System.out.println("ArticleService 생성자호출");
	}
	
	
	//글쓰기 서비스
	public void write(Article article) {
		this.articleDao.insert(article);
	}
}
