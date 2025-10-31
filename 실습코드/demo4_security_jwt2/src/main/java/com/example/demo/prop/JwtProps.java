package com.example.demo.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

//@ConfigurationProperties  application.properties 접근가능 
//kr.or.kosa.secret-key=HS`{|T0vkYS2Q~?T2S9$WqYdrX>kP1{,b(OiZ-g_s_Gz1.{?t2Kz8PYu|a)V3;/z

@Component
@Data
@ConfigurationProperties("kr.or.kosa")
public class JwtProps {
	private String secretkey;
	//롬복 getter 를 사용해서 
}
