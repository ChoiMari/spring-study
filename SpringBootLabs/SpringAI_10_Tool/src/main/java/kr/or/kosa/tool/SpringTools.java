package kr.or.kosa.tool;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

public class SpringTools {
	//시스템 날짜 (POJO)
	@Tool
	String getCurrentDateTime() {
		return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
	}
	
	//DB처럼 흉내
	private static final Map<String, String> dictionary =
			new HashMap<String, String>();
	
	static {
		dictionary.put("김민수", "등산");
		dictionary.put("이지은", "독서");
		dictionary.put("박준형", "축구");
		dictionary.put("최유리", "영화 감상");
		
	}
	
	@Tool
	public String userHobby(String name) {
		return dictionary.getOrDefault(name, "해당 직원 없음");
		//getOrDefault() : 해당되는 키가 없으면 default값("해당 직원 없음")을 리턴
	}
}
