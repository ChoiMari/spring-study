<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Emp List</title>
    <!-- Bootstrap 5 적용 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">

<h1 class="mb-4">Employee List (조인 결과)</h1>

<table class="table table-bordered table-hover">
    <thead class="table-light">
    <tr>
        <th>EmpNo</th>
        <th>Ename</th>
        <th>Job</th>
        <th>Sal</th>
        <th>DeptNo</th>
        <th>DName (부서명)</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="emp" items="${emps}">
        <tr>
            <td>${emp.empno}</td>
            <td>${emp.ename}</td>
            <td>${emp.job}</td>
            <td>${emp.sal}</td>
            <td>${emp.deptno}</td>
            <td>${emp.dname}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
