<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<div class="col-md-4 col-md-offset-4">
		<table>
			<tr>
				<td>유저 Email: ${login.userEmail }</td>
			</tr>
			<tr>
				<td>유저 ID: ${login.userId }</td>
			</tr>
			<tr>
				<td>유저 Name : ${login.name }</td>
			</tr>
			<tr>
				<td>유저 Score : ${login.score }</td>
			</tr>
		</table>
	</div>

	<div class="col-md-4 col-md-offset-4" style="text-align: center; margin-bottom: 40px;">
		<span>
			<button type ="button" class="btn btn-default" id ="modifyInfo" onclick ="location.href ='/genious/user/modifyInfo'">회원정보수정</button>
			<button type ="button" class="btn btn-default" id ="modifyPw" onclick ="location.href ='/genious/user/changePw'">비밀번호 변경</button>
		</span>
	</div>
</body>
</html>