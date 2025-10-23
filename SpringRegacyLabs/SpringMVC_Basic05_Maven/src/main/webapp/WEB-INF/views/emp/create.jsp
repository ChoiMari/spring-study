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
<body>
	<div class="container py-5">
	    <h2 class="text-center mb-4">새 사원 등록</h2>
	
	    <form method="post" action="/emp/empCreate.do" class="p-4 border rounded bg-light shadow-sm">
	        <div class="mb-3">
	            <label class="form-label">사번</label>
	            <input type="number" name="empno" class="form-control" required>
	        </div>
	
	        <div class="mb-3">
	            <label class="form-label">이름</label>
	            <input type="text" name="ename" class="form-control" required>
	        </div>
	
	        <div class="mb-3">
	            <label class="form-label">직무</label>
	            <input type="text" name="job" class="form-control">
	        </div>
	
	        <div class="mb-3">
	            <label class="form-label">관리자 번호</label>
	            <input type="number" name="mgr" class="form-control">
	        </div>
	
	        <div class="mb-3">
	            <label class="form-label">입사일</label>
	            <input type="date" name="hiredate" class="form-control">
	        </div>
	
	        <div class="mb-3">
	            <label class="form-label">급여</label>
	            <input type="number" name="sal" class="form-control">
	        </div>
	
	        <div class="mb-3">
	            <label class="form-label">커미션</label>
	            <input type="number" name="comm" class="form-control">
	        </div>
	
	        <div class="mb-3">
	            <label class="form-label">부서번호</label>
	            <input type="number" name="deptno" class="form-control">
	        </div>
	
	        <div class="text-center mt-4">
	            <button type="submit" class="btn btn-success">등록</button>
	            <a href="/emp/emplist.do" class="btn btn-secondary">목록으로</a>
	        </div>
	    </form>
	</div>
</body>
</html>