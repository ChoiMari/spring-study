package com.example.demo.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
//@ConfigurationProperties application.properies에 접근이 가능하다
@Component
@Data 
@ConfigurationProperties("kr.or.kosa")
public class JwtProps {
	private String secretKey;
	//롬복 getter를 사용해서 key값을 읽어오겠다(프로퍼티 파일에 있는)
}
