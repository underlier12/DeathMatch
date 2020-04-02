<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="/css/bootstrap.css">
<script src="/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/css/joinModal.css">

</head>

<body>
	<div class="modal fade" id="registerModal" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content" id="joinModal">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id ="modal-title">Welcome To DeathMatch</h4>
				</div>
				<div class="modal-body">
					<!-- 이메일 -->
					<label for="userEmail1" id="idTitle">아이디</label>
					<div class="form-group">
						<input type="text" style="width: 150px; display: inline-block;"
							class="form-control" id="userEmail1" name="userEmail1"
							placeholder="ID" maxlength="12" /> @ <input type="text"
							style="width: 160px; display: inline-block;" class="form-control"
							id="userEmail2" name="userEmail2" /> <select id="select_email"
							onchange="changeEmail()">
							<option value="">-- 직접 입력 --</option>
							<option>naver.com</option>
							<option>daum.net</option>
							<option>nate.com</option>
							<option>gmail.com</option>
							<option>hotmail.com</option>
							<option>yahoo.com</option>
						</select> <span id="email_check_text"></span> <span style="float: right;">
							<button type="button" class="btn btn-warning" id="checkEmail">
								Check</button>
						</span>
					</div>
				</div>

				<!-- 아이디 -->
				<input type="hidden" name="userId" />

				<!-- 비밀번호 -->
				<div class="form-group">
					<label for="pw" id="pwTitle">비밀번호</label> <input type="password"
						class="form-control" id="pw" name="pw" placeholder="PASSWORD"
						maxlength="15">
					<div class="check_font" id="pw_check"></div>
				</div>

				<!-- 비밀번호 확인 -->
				<div class="form-group">
					<label for="pw" id="checkPwTitle">비밀번호 확인</label> <input type="password"
						class="form-control" id="checkPw" name="checkPw"
						placeholder="PASSWORD" maxlength="15">
					<div class="check_font" id="pw_check"></div>
				</div>

				<!-- 이름 -->
				<div class="form-group">
					<label for="userName" id="nameTitle">이름</label> <input type="text"
						class="form-control" id="userName" name="name" placeholder="Name"
						maxlength="8">
					<div class="check_font" id="name_check"></div>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">
					<div class="reg_button">
						<button type="button" class ="btn btn-default" data-dismiss="modal">
							<i class="fa fa-rotate-right pr-2" aria-hidden="true"></i>닫기
						</button>
						<button type="button" id="join" class="btn btn-info">
							<i class="fa fa-heart pr-2" aria-hidden="true"></i>Join
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>