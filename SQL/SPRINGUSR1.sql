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
select * from emp;

select e.empno, e.ename, e.job, e.sal, e.deptno, d.dname
from emp e join dept d
on e.deptno = d.deptno;

commit;

