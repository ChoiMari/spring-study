package ncontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.CustomerService;
import service.EmpService;
import vo.Emp;

@Controller
@RequestMapping("/emp/")
public class EmpController {
	
	//CustomerService 의존 (객체의 주소 필요)
	private EmpService empSvc;

	@Autowired
	public void setEmpSvc(EmpService empSvc) {
		this.empSvc = empSvc;
	}
		
	//사원 생성 화면 보여주세요
	@GetMapping("empCreate.do")
	public String empCteate(Emp emp) {
		return "emp/create";
	}
	
	//사원 생성 처리해주세요
	@PostMapping("empCreate.do")
	public String empCreate(Emp emp) {
		empSvc.createEmp(emp); //DB insert
		return "redirect:/emp/emplist.do"; //리다이렉트 - 목록으로
	}
	
	//전체 EMP 목록 보여주세요
	@GetMapping("emplist.do")
	public String empList(Model model) {
		System.out.println("GET : emplist.do");
		model.addAttribute("list", empSvc.getEmpList());
		return "emp/list";
	}
	
	//상세 EMP화면 보여주세요
	@GetMapping("empDetail.do")
	public String empDetail(@RequestParam("empno") int empno, Model model) {
		
		model.addAttribute("empDetail", empSvc.getEmpByEmpno(empno));
		
		return "emp/detail";
	}
	
	//EMP 수정 화면보여주세요
	@GetMapping("empUpdate.do")
	public String empUpdate(@RequestParam("empno") int empno, Model model) {
		model.addAttribute("empUpdate", empSvc.getEmpByEmpno(empno)) ;
		return "emp/update";
	}
	
	//EMP 수정 처리해주세요 -> 상세 페이지로 리다이렉트
	@PostMapping("empUpdate.do")
	public String empUpdate(Emp emp) {
		empSvc.updateEmp(emp);
		return "redirect:/emp/empDetail.do?empno=" + emp.getEmpno();
	}
	
	//EMP 삭제 처리해주세요 -> 목록으로 리다이렉트
	@GetMapping("empDelete.do")
	public String empDelete(@RequestParam("empno") int empno) {
		empSvc.deleteEmpByEmpno(empno);
		return "redirect:/emp/emplist.do"; //리다이렉트 - 목록으로
	}


}
