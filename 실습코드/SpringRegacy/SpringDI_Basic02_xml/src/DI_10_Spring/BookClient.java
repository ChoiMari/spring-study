package DI_10_Spring;

import java.util.Properties;

public class BookClient {
	private Properties config;  //Properties 클래스는  collection 
	
	public void setConfig(Properties config) {
		this.config = config;
	}
	
	//일반함수
	//주입에 대한 확인 함수
	public void connect() {
		String server = config.getProperty("server");
		String timeout = config.getProperty("connectionTimeOut");
		System.out.println("server : " +  server);
		System.out.println("timeout : " +  timeout);
	}
}
