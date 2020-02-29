<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>데스 매치</title>
<!-- BootStrap 적용 -->
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="/genious/css/bootstrap.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript">
	var message = "${msg}";
	if (message != "") {
		alert(message);
	}
</script>
</head>
<body>
	<div style="margin-top: 80px">
		<div class="col-md-10 col-md-offset-1" style="text-align: center">
			<p>Death Match</p>
		</div>
	</div>

	<form action="/genious/user/loginPost" method="post" id="loginProc">
		<div class="row loginForm-row" style="margin-top: 150px;">
			<div class="col-md-4 col-md-offset-4">
				<input type="email" class="form-control" name="userEmail"
					placeholder="아이디">
			</div>
			<div class="col-md-4 col-md-offset-4">
				<input type="password" class="form-control" name="pw"
					placeholder="비밀번호">
			</div>
			<div class="col-md-4 col-md-offset-4">
				<c:if test="${empty login}">
					<button type="submit" id="loginBtn"
						class="btn btn-default btn-block login-btn">로그인</button>
				</c:if>
			</div>
			<div class="col-md-4 col-md-offset-4">
				<div class="checkbox">
					<label><input type="checkbox" id="checkId"> <span>아이디
							저장</span></label>
				</div>
			</div>
		</div>
	</form>

	<div class="col-md-4 col-md-offset-4"
		style="text-align: center; margin-bottom: 40px;">
		<span>
			<button type ="button" class="btn btn-default" id="regiBtn">회원가입</button>
			<button type ="button" class="btn btn-default">아이디 찾기</button>
			<button type ="button" class="btn btn-default">비밀 번호 찾기</button>
		</span>
	</div>

	<div class="col-md-4 col-md-offset-4" style="margin-bottom: 50px;">
		<div>
			<a
				href="https://kauth.kakao.com/oauth/authorize?client_id=24ff4b9dce4ffc7531bff6ac4abb6bc2&redirect_uri=http://localhost:8003/genious/user/kakaoLogin&response_type=code">
				<img src="/images/btn_kakao_login.gif">
			</a>
		</div>
		<div id="naver_id_login" style="margin-bottom: 50px;">
			<a href="${url}" /> <img src="/images/btn_naver_login.gif">
		</div>
	</div>


	<form action="/genious/user/logout" method="get">
		<div class="col-md-3 col-md-offset-4">
			<c:if test="${!empty login}">
				<button type="submit" id="logout"
					class="btn btn-default btn-block login-btn">로그아웃</button>
			</c:if>
		</div>
	</form>


	<!-- 회원가입 Modal 추가 -->
	<div class="modal fade" id="registerModal" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 id="modal-title" class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<table class="table table-bordered noticeView-table">
						<tr>
							<td class="view_title">제목</td>
							<td><div id="modalTitle"></div></td>
						</tr>

						<tr>
							<td class="view_title">작성자</td>
							<td><div id="modalWriter"></div></td>
						</tr>
						
						<tr>
							<td class="view_title">Grade</td>
							<td><div id="modalGrade"></div></td>
						</tr>

						<tr>
							<td colspan="2"><div style = "width:300px; height:200px;"
									id="modalContent"></div></td>
						</tr>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default btn-sm"
						data-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript">
	$(document).ready(function() {
		var loginForm = $("#loginProc");
		
		$("#loginBtn").click(function(e) {
			if ($('input[name="userEmail"]').val() == ""|| $('input[name="userEmail"]').val() == null) {
					alert("ID를 입력해주세요");
					return false;
			}
			if ($('input[name="pw"]').val() == "" || $('input[name="pw"]').val() == null) {
					alert("PW를 입력해주세요");
					return false;
			}
			loginForm.submit();
		});
		
		$("#regiBtn").click(function(e){
			alert("hello");
			$("#registerModal").modal();
		});
	
	
	});
	
</script>
</html>