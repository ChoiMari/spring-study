<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!--  파일 업로드
	초급자의 실수 1순위
	
	enctype="multipart/form-data"이걸 안씀
	
	나는 서버에서 파일과 텍스트를 같이 넘긴다라고 명시함
	이걸 안쓰면 텍스트만 넘어감
	multipart/form-data이게 있어야 텍스트와 파일 전송이 가능하다
	
	전송: 텍스트 + 파일 
	dto로도 파일을 받을 수 있다.
-->
	<form method="post" enctype="multipart/form-data">
		이름:<input type="text" name="name"><br>
		나이:<input type="number" name="age"><br>
		사진:<input type="file" name="file"><br>
		<input type="submit" value="파일 업로드">
	</form>
</body>
</html>