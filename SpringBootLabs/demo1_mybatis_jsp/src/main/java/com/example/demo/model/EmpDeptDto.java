package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 select e.empno, e.ename, e.job, e.sal, e.deptno, d.dname
 from emp e join dept d
 on e.deptno = d.deptno;

join 되는 쿼리문도 별도의 DTO 생성하면 된다
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EmpDeptDto {
	private Integer empno;
	private String ename;
	private String job;
	private Integer sal;
	private Integer deptno;
	private String dname;
}
