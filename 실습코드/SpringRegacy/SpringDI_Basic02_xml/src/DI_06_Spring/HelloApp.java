package DI_06_Spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloApp {

	public static void main(String[] args) {
	
		//Controller 가정
		//service 사용
		
		//ArticleService service = new ArticleService(new OracleDao());
		//ArticleService service = new ArticleService(new MysqlDao());
		//Article article = new Article(); //사용자 요청 정보
	    //service.write(article);
		
		
		ApplicationContext context = 
				new  GenericXmlApplicationContext("classpath:DI_06_Spring/DI_06.xml");
		
		 //service.write(article);
		ArticleService articleService = context.getBean("articleService",ArticleService.class);
		Article article =  context.getBean("article",Article.class);
		articleService.write(article);
	}

}
