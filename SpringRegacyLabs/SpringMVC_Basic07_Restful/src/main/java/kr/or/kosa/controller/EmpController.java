package kr.or.kosa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kosa.dto.Emp;
import kr.or.kosa.service.EmpService;
import lombok.RequiredArgsConstructor;

@RestController //-> 이 클래스는 전부 비동기 메서드
// @컨트롤러 + @리스판스바디
@RequestMapping("/emp")
@RequiredArgsConstructor // 롬복 안쓰면 수동으로 써야함
public class EmpController {
	// method로 판단함
	// 주의 - 컨트롤러는 서비스에 의존합니다
	// 서비스의 주소가 필요합니다
	private final EmpService empService;
	
	//전체 조회  - GET
	@GetMapping
	public ResponseEntity<List<Emp>> empList(){
		//제네릭은 리턴 타입을 쓰면 되는데 리스판스엔터티로 한 번 포장하는 것
		
		List<Emp> list = new ArrayList<Emp>();
		try {
			System.out.println("try 블록 시작 - 실행");
			list = empService.selectAllEmpList();
			return new ResponseEntity<List<Emp>>(list, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("catch 비정상 실행");
			return new ResponseEntity<List<Emp>>(list, HttpStatus.BAD_REQUEST);
			// 원래는 데이터 없어도 됨
		}
	}
	
	//조건 조회 - GET
	//http://192.168.2.8/emp/7902
	//url 일부를 데이터로 받음 -> 패스베이러블
	/*
	 @RequestMapping(value="{empno}", method=RequestMethod.GET)와 같음
	  
	 */
	@GetMapping("{empno}")
	public Emp empListByEmpno(@PathVariable("empno") int empno) {
		return empService.selectEmpByEmpno(empno);
	}
	
	//삽입 - POST
	// 요청 url /emp + (JSON 형태의 문자열)데이터가 넘어와야함 + POST
	//받는 방법 고민.. 동기 방식에서는 dto로 파라미터 값 받았음
	// 비동기도 dto로 받기 가능. 단, 파라미터를 동기식처럼 그냥 dto타입으로는 못쓰고
	//@RequestBody로 받아야한다
	@PostMapping
	public ResponseEntity<String> insert(@RequestBody Emp emp){
		try {
			System.out.println("insert");
			//dto타입으로 받음(자동 데이터 바인딩, 내부에서 자동으로 new하고 setter로 값변경해서 주입해줌)
			System.out.println(emp.toString());
			empService.insert(emp);
			return new ResponseEntity<String>("insert success", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("insert fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	//수정 - PUT/PETCH
	
	//삭제 - DELETE
	
}
