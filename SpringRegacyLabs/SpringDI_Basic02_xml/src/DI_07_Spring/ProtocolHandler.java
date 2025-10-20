package DI_07_Spring;

import java.util.List;

public class ProtocolHandler {
	//여러개의 filter 사용
	List<MyFilter> filters;
	//MyFilter 인터페이스를 구현하고 있는 클래스들이 들어갈수있음
	
	//getter
	public List<MyFilter> getFilters() {
		return filters;
	}

	//setter  주입
	public void setFilters(List<MyFilter> filters) {
		this.filters = filters;
	}
	
	//검증 함수
	public int filter_Length() {
		return this.filters.size();
	}
	
	
}
