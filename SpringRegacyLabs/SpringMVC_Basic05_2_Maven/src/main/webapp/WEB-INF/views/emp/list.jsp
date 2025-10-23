<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Emp</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
</head>
<body class="bg-light">
<div class="container py-5">
    <h2 class="mb-4 text-center">사원 목록</h2>

    <div class="text-end mb-3">
        <a href="/emp/empCreate.do" class="btn btn-primary">새 사원 등록</a>
    </div>

    <table class="table table-hover align-middle table-bordered">
        <thead class="table-dark">
        <tr>
            <th>사번</th>
            <th>이름</th>
            <th>직무</th>
            <th>부서번호</th>
            <th>급여</th>
            <th>관리자번호</th>
            <th>상세보기</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="emp" items="${list}">
            <tr>
                <td>${emp.empno}</td>
                <td>${emp.ename}</td>
                <td>${emp.job}</td>
                <td>${emp.deptno}</td>
                <td>${emp.sal}</td>
                <td>${emp.mgr}</td>
                <td>
                    <a href="/emp/empDetail.do?empno=${emp.empno}" class="btn btn-sm btn-outline-secondary">
                        보기
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>