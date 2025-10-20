package DI_06_Spring;

public class MysqlDao implements ArticleDao {

	@Override
	public void insert(Article article) {
		System.out.println("mysql insert 수행");
		
	}

}
