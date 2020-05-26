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
<link rel="stylesheet" href="/css/user/findModal.css">
</head>

<body>
	<div class="modal fade" id="findModal" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content" id="findModalContent">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 id="modal-title" class="modal-title"
						style="text-align: center;">Welcome To DeathMatch</h4>
				</div>
				<div class="modal-body">
					<!-- 이메일 -->
					<label for="findPwEmail1" id="idTitle">아이디</label>
					<div class="form-group">
						<input type="text" style="width: 150px; display: inline-block;"
							class="form-control" id="findPwEmail1" name="userEmail1"
							placeholder="ID" maxlength="12" /> @ <input type="text"
							style="width: 200px; display: inline-block;" class="form-control"
							id="findPwEmail2" name="userEmail2" /> <select id="select_email2"
							onchange="changeEmail2()">
							<option value="">-- 직접 입력 --</option>
							<option>naver.com</option>
							<option>daum.net</option>
							<option>nate.com</option>
							<option>gmail.com</option>
							<option>hotmail.com</option>
							<option>yahoo.com</option>
						</select> <span id="email_check_text"></span> <span style="float: right;">
							<button type="button" class="btn btn-warning" id="findPwCheckEmail">
								Check</button>
						</span>
					</div>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">
					<div class="reg_button">
						<button type="button" id="findUserPw" class="btn btn-info">
							<i class="fa fa-heart pr-2" aria-hidden="true"></i>찾기
						</button>
						<button type="button" class="btn btn-primary px-3"
							data-dismiss="modal">
							<i class="fa fa-rotate-right pr-2" aria-hidden="true"></i>닫기
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>