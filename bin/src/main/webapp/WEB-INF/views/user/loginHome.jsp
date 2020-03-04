<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<script type="text/javascript">
	var message = "${msg}";
	if (message != "") {
		alert(message);
	}
</script>
<script type ="text/javascript" src ="/genious/js/loginHome.js"></script>
<body>
	<%@ include file="/WEB-INF/views/includes/joinModal.jsp" %>
	
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
	
	<c:if test ="${empty login}">
		<div class="col-md-4 col-md-offset-4" style="margin-bottom: 50px;">
			<div>
				<a href="https://kauth.kakao.com/oauth/authorize?client_id=24ff4b9dce4ffc7531bff6ac4abb6bc2&redirect_uri=http://localhost:8003/genious/user/kakaoLogin&response_type=code">
					<img src="/images/btn_kakao_login.gif">
				</a>
			</div>
			<div id="naver_id_login" style="margin-bottom: 50px;">
				<a href="${url}"><img src="/images/btn_naver_login.gif"></a>
			</div>
		</div>
	</c:if>

	<form action="/genious/user/logout" method="get">
		<div class="col-md-3 col-md-offset-4">
			<c:if test="${!empty login}">
				<button type="submit" id="logout"
					class="btn btn-default btn-block login-btn">로그아웃</button>
			</c:if>
		</div>
	</form>
	
	<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
	
</body>

