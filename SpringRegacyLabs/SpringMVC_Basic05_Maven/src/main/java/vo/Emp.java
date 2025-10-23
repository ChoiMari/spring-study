package vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

public class Emp implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer empno;
	private String ename;
	private String job;
	private Integer mgr;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	private Date hiredate;
	
	private Integer sal;
	private Integer comm;
	private Integer deptno;
	public Integer getEmpno() {
		return empno;
	}
	public void setEmpno(Integer empno) {
		this.empno = empno;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public Integer getMgr() {
		return mgr;
	}
	public void setMgr(Integer mgr) {
		this.mgr = mgr;
	}
	public Date getHiredate() {
		return hiredate;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	public Integer getSal() {
		return sal;
	}
	public void setSal(Integer sal) {
		this.sal = sal;
	}
	public Integer getComm() {
		return comm;
	}
	public void setComm(Integer comm) {
		this.comm = comm;
	}
	public Integer getDeptno() {
		return deptno;
	}
	public void setDeptno(Integer deptno) {
		this.deptno = deptno;
	}
	@Override
	public int hashCode() {
		return Objects.hash(comm, deptno, empno, ename, hiredate, job, mgr, sal);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Emp other = (Emp) obj;
		return Objects.equals(comm, other.comm) && Objects.equals(deptno, other.deptno)
				&& Objects.equals(empno, other.empno) && Objects.equals(ename, other.ename)
				&& Objects.equals(hiredate, other.hiredate) && Objects.equals(job, other.job)
				&& Objects.equals(mgr, other.mgr) && Objects.equals(sal, other.sal);
	}
	@Override
	public String toString() {
		return "Emp [empno=" + empno + ", ename=" + ename + ", job=" + job + ", mgr=" + mgr + ", hiredate=" + hiredate
				+ ", sal=" + sal + ", comm=" + comm + ", deptno=" + deptno + "]";
	}
	public Emp(Integer empno, String ename, String job, Integer mgr, Date hiredate, Integer sal, Integer comm,
			Integer deptno) {
		super();
		this.empno = empno;
		this.ename = ename;
		this.job = job;
		this.mgr = mgr;
		this.hiredate = hiredate;
		this.sal = sal;
		this.comm = comm;
		this.deptno = deptno;
	}
	
	public Emp() {
		
	}
	
}
