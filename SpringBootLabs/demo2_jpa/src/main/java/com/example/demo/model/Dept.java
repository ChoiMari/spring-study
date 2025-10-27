package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// DB : Dept테이블과 매핑됨. 엔티티
// Entity : 이 클래스를 정보를 가지고 create 할거다
// create table DEPT ...컬럼명(PK) 타입, ... 
@Entity
@Data
@Table(name="DEPT")
public class Dept {
	@Id //PK
	private int deptno;
	private String dname;
	private String loc;
	
}
