<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>EMP</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
</head>
<body>
	<div class="container py-5">
	    <h2 class="text-center mb-4">사원 상세 정보</h2>
	
	    <table class="table table-bordered bg-light">
	        <tr><th>사번</th><td>${empDetail.empno}</td></tr>
	        <tr><th>이름</th><td>${empDetail.ename}</td></tr>
	        <tr><th>직무</th><td>${empDetail.job}</td></tr>
	        <tr><th>관리자 번호</th><td>${empDetail.mgr}</td></tr>
	        <tr><th>입사일</th><td>${empDetail.hiredate}</td></tr>
	        <tr><th>급여</th><td>${empDetail.sal}</td></tr>
	        <tr><th>커미션</th><td>${empDetail.comm}</td></tr>
	        <tr><th>부서번호</th><td>${empDetail.deptno}</td></tr>
	    </table>
	
	    <div class="text-center mt-4">
	        <a href="/emp/empUpdate.do?empno=${empDetail.empno}" class="btn btn-warning">수정</a>
	        <a href="/emp/empDelete.do?empno=${empDetail.empno}" class="btn btn-danger"
	           onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
	        <a href="/emp/emplist.do" class="btn btn-secondary">목록으로</a>
	    </div>
	</div>
</body>
</html>