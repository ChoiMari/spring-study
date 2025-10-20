package DI_06_Spring;

public class OracleDao implements ArticleDao {

	@Override
	public void insert(Article article) {
		System.out.println("oracle insert 수행");
		
	}

}
