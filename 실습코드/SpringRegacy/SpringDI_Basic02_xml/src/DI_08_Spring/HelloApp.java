package DI_08_Spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloApp {

	public static void main(String[] args) {
	
		/*
		
		JobExecute jobExecute = new JobExecute("hong", 100);
		JobExecute jobExecute2 = new JobExecute("kim", 100L);
		JobExecute jobExecute3 = new JobExecute("park", "kim");
		
		ArticleDao articleDao = new ArticleDao();
		jobExecute.setArticleDao(articleDao);
		
		jobExecute.setData(500);
		*/
		
		ApplicationContext context = 
				new  GenericXmlApplicationContext("classpath:DI_08_Spring/DI_08.xml");
		
		 
		
	}

}
