package DI_07_Spring;

import java.util.List;

public class ProtocolHandler {
	//여러개의 filter 사용
	List<MyFilter> filters;

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
