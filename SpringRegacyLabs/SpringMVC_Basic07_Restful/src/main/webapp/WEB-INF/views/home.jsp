<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi"
	crossorigin="anonymous">

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						function formview() {
							$("#formempno").val("");
							$("#formename").val("");
							$("#formsal").val("");

							if (document.getElementById("writeform").classList
									.item(2) == null) {
								document.getElementById("writeform").className += " visually-hidden";
							} else {
								document.getElementById("writeform").classList
										.remove("visually-hidden");
							}
						}

						function formupdateview() {

							if (document.getElementById("updateform").classList
									.item(2) == null) {
								document.getElementById("updateform").className += " visually-hidden";
							} else {
								document.getElementById("updateform").classList
										.remove("visually-hidden");
							}
						}

						//사원 전체 조회 함수
						function empList() {
							$
									.ajax({
										type : "get",
										url : "emp",
										contentType : "application/json; charset=utf-8",
										success : function(data) {

											$("#empList").empty();
											let html = "";

											$
													.each(
															data,
															function() {
																html += "<tr>";
																html += '<td>'
																		+ this.empno
																		+ '</td>';
																html += '<td>'
																		+ this.ename
																		+ '</td>';
																html += '<td>'
																		+ this.sal
																		+ '</td>';
																html += '<td><a class="btn btn-outline-primary empupdate" value2="' + this.empno + '">수정</a></td>';
																html += '<td><a class="btn btn-outline-primary empdelete" value2="' + this.empno + '">삭제</a></td>';
																html += "</tr>";
															});

											$("#empList").append(html);

										}
									});
						}

						//전체 조회
						empList();

						//특정 사원 검색
						function searchByEmpno(requestdata1) {
							$
									.ajax({
										type : "get",
										url : "emp/" + requestdata1,
										success : function(data) {

											console.log(data);

											$("#empList").empty();
											let html = "";

											html += "<tr>";
											html += '<td>' + data.empno
													+ '</td>';
											html += '<td>' + data.ename
													+ '</td>';
											html += '<td>' + data.sal + '</td>';
											html += '<td><a class="btn btn-outline-primary empupdate" value2="' + data.empno + '">수정</a></td>';
											html += '<td><a class="btn btn-outline-primary empdelete" value2="' + data.empno + '">삭제</a></td>';
											html += "</tr>";

											$("#empList").append(html);

										}
									});
						}

						//특정 사원 검색
						$("#empnosearchbtn")
								.click(
										function() {
											if ($("#empnosearch").val() == ""
													|| $("#empnosearch").val() == null) {
												empList();
											} else {
												searchByEmpno($("#empnosearch")
														.val());
											}
										});

						//사원등록 form 보이게 하거나 지우기
						$('#empwritebtn').click(function() {
							formview();
						});

						//사원 등록 비동기 처리
						$("#empwritesubmit")
								.click(
										function() {

											let requestdata = {
												"empno" : $("#formempno").val(),
												"ename" : $("#formename").val(),
												"sal" : $("#formsal").val(),
											}
											let data = JSON
													.stringify(requestdata); //문자열 (JSON 형태의) //"empno" 키 쿼테이션
											//let data = requestdata;
											console.log(data);
											$
													.ajax({
														type : "post",
														url : "emp",
														data : data,
														dataType : "text", //서버가 응답하는 데이터 형식
														contentType : "application/json; charset=utf-8",
														success : function(data) {
															formview();
															empList();
															alert(data);
														}
													});
										});

						//등록 취소
						$("#empwritereset").on("click", function() {
							formview();
						});

						//해당 emp 컬럼 삭제
						$(document).on("click", ".empdelete", function() {

							console.log($(this).attr("value2"));

							$.ajax({
								type : "DELETE",
								url : "emp/" + $(this).attr("value2"),
								success : function(data) {
									empList();
								}
							});
						});

						//수정 기능 함수
						function updateEmp() {
							let requestdata = {
								"empno" : $("#formempno1").val(),
								"ename" : $("#formename1").val(),
								"sal" : $("#formsal1").val(),
							}
							let data = JSON.stringify(requestdata);
							//data = requestdata;
							$
									.ajax({
										type : "put",
										url : "emp",
										data : data,
										dataType : "text",
										contentType : "application/json; charset=utf-8",
										success : function(data) {
											formupdateview();
											empList();
											alert(data);
										}
									});
						}

						//해당 emp 컬럼 수정 버튼 클릭
						$(document).on("click", ".empupdate", function() {
							formupdateview();

							var tr1 = $(this).closest('tr');

							$("#formempno1").val(tr1.find("td:eq(0)").text());
							$("#formename1").val(tr1.find("td:eq(1)").text());
							$("#formsal1").val(tr1.find("td:eq(2)").text());

						});

						//수정 기능 실행
						$(document).on("click", "#empwritesubmit1", function() {
							updateEmp();
						});

					});
</script>

</head>
<body>

	<div class="m-5">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>사원번호 : <input type="text" id="empnosearch"> <input
						type="submit" id="empnosearchbtn" value="검색">
					</th>
					<th><a class="btn btn-primary" id="empwritebtn">사원 등록</a></th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="writeform" class="m-5 row visually-hidden">
		<div class="col-2"></div>
		<div class="col-8 text-center">
			<h3>
				<strong>**사원**</strong>
			</h3>
			<table class="table table-striped">
				<tbody>
					<tr>
						<td>EMPNO</td>
						<td><input type="text" id="formempno" value="" required /></td>
					</tr>
					<tr>
						<td>ENAME</td>
						<td><input type="text" id="formename" value="" /></td>
					</tr>
					<tr>
						<td>SAL</td>
						<td><input type="text" id="formsal" value="" /></td>
					</tr>
				</tbody>
			</table>
			<a class="btn btn-outline-primary" id="empwritesubmit">확인</a> <a
				class="btn btn-outline-danger" id="empwritereset">취소</a>
		</div>
		<div class="col-2"></div>
	</div>

	<div id="updateform" class="m-5 row visually-hidden">
		<div class="col-2"></div>
		<div class="col-8 text-center">
			<h3>
				<strong>**사원**</strong>
			</h3>
			<table class="table table-striped">
				<tbody id="updatef">
					<tr>
						<td>EMPNO</td>
						<td><input type="text" id="formempno1" value="" required /></td>
					</tr>
					<tr>
						<td>ENAME</td>
						<td><input type="text" id="formename1" value="" /></td>
					</tr>
					<tr>
						<td>SAL</td>
						<td><input type="text" id="formsal1" value="" /></td>
					</tr>
				</tbody>
			</table>
			<a class="btn btn-outline-primary" id="empwritesubmit1">확인</a> <a
				class="btn btn-outline-danger" id="empwritereset1">취소</a>
		</div>
		<div class="col-2"></div>
	</div>

	<div class="m-3">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>EMPNO</th>
					<th>ENAME</th>
					<th>SAL</th>
					<th>수정</th>
					<th>삭제</th>
				</tr>
			</thead>
			<tbody id="empList">

				<c:forEach items="${list}" var="l">
					<tr>
						<td>${l.empno}</td>
						<td>${l.ename}</td>
						<td>${l.sal}</td>
						<td><a class="btn btn-outline-primary empupdate">수정</a></td>
						<td><a class="btn btn-outline-primary empdelete">삭제</a></td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3"
		crossorigin="anonymous"></script>

</body>
</html>