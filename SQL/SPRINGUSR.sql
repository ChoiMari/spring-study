create table users
	(
  		id varchar2(20) primary key,
  		name varchar2(20) not null,
  		password varchar2(20) not null
	);	
    
    select * from users;
    delete from users;
    
    alter table member
    rename column "UID" to userid;
    
select * from emp;



CREATE TABLE EMP
(EMPNO number not null,
ENAME VARCHAR2(10),
JOB VARCHAR2(9),
MGR number ,
HIREDATE date,
SAL number ,
COMM number ,
DEPTNO number );


INSERT INTO EMP VALUES
(7369,'SMITH','CLERK',7902,'1980-12-17',800,null,20);
INSERT INTO EMP VALUES
(7499,'ALLEN','SALESMAN',7698,'1981-02-20',1600,300,30);
INSERT INTO EMP VALUES
(7521,'WARD','SALESMAN',7698,'1981-02-22',1250,200,30);
INSERT INTO EMP VALUES
(7566,'JONES','MANAGER',7839,'1981-04-02',2975,30,20);
INSERT INTO EMP VALUES
(7654,'MARTIN','SALESMAN',7698,'1981-09-28',1250,300,30);
INSERT INTO EMP VALUES
(7698,'BLAKE','MANAGER',7839,'1981-04-01',2850,null,30);
INSERT INTO EMP VALUES
(7782,'CLARK','MANAGER',7839,'1981-06-01',2450,null,10);
INSERT INTO EMP VALUES
(7788,'SCOTT','ANALYST',7566,'1982-10-09',3000,null,20);
INSERT INTO EMP VALUES
(7839,'KING','PRESIDENT',null,'1981-11-17',5000,3500,10);
INSERT INTO EMP VALUES
(7844,'TURNER','SALESMAN',7698,'1981-09-08',1500,0,30);
INSERT INTO EMP VALUES
(7876,'ADAMS','CLERK',7788,'1983-01-12',1100,null,20);
INSERT INTO EMP VALUES
(7900,'JAMES','CLERK',7698,'1981-10-03',950,null,30);
INSERT INTO EMP VALUES
(7902,'FORD','ANALYST',7566,'1981-10-3',3000,null,20);
INSERT INTO EMP VALUES
(7934,'MILLER','CLERK',7782,'1982-01-23',1300,null,10);
--alter session set nls_date_format='YYYY-MM-DD HH24:MI:SS';
COMMIT;

select * from dept;

select deptno, dname, loc from dept order by deptno;

delete from dept where deptno
select * from emp;
CREATE TABLE DEPT
(DEPTNO number,
DNAME VARCHAR2(14),
LOC VARCHAR2(13) );

select deptno, dname, loc from dept;
select deptno, dname, loc from dept where deptno = 10;


INSERT INTO DEPT VALUES (10,'ACCOUNTING','NEW YORK');
INSERT INTO DEPT VALUES (20,'RESEARCH','DALLAS');
INSERT INTO DEPT VALUES (30,'SALES','CHICAGO');
INSERT INTO DEPT VALUES (40,'OPERATIONS','BOSTON');

COMMIT;


CREATE TABLE SALGRADE
( GRADE number,
LOSAL number,
HISAL number );

INSERT INTO SALGRADE VALUES (1,700,1200);
INSERT INTO SALGRADE VALUES (2,1201,1400);
INSERT INTO SALGRADE VALUES (3,1401,2000);
INSERT INTO SALGRADE VALUES (4,2001,3000);
INSERT INTO SALGRADE VALUES (5,3001,9999);
COMMIT;



insert into member(userid, pwd , name)
values('admin','1004','관리자');
commit;

select * from member;

alter table member
add point number;

alter table member 
add constraint ck_member_hit check(point < 3);


select * from user_constraints; 

--게시판의 글이 insert되면 이 쿼리 날림
update member
set point = nvl(point,0) + 1
where userid = 'admin'; 

alter table member 
add constraint ck_member_hit check(point < 3);


select * from user_constraints; 

--게시판의 글이 insert되면 이 쿼리 날림
update member
set point = nvl(point,0) + 1
where userid = 'admin'; 

rollback;

-----------------------------------------
-- 20251027

create table users(
    id number primary key,
    username varchar2(50),
    password varchar2(50),
    email varchar2(50)
);

create sequence user_id;


select * from users;

insert into users(id, username, password,email)
values(user_id.nextval ,'admin','1004','admin@naver.com');

select * from users;

commit;


