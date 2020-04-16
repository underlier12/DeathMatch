<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>

<link href="/css/main/myPage.css" rel="stylesheet">
<body>

	<div>
		<div class="col-md-10 col-md-offset-1" style="text-align: center">
			<h2 id="title">회원 정보</h2>
		</div>
	</div>

	<div class ="row">
		<div class ="col-md-4 col-md-offset-2" style ="margin-top :80px; text-align:center" >
			<h3 id="userIdTitle">아이디 : <span id="userId"><c:out value ="${login.userId }"/></span> </h3>
			<h3 id="userEmailTitle">이메일 : <span id="userEmail"><c:out value ="${login.userEmail }"/></span></h3>
			<h3 id="userNameTitle">이름: <span id="userName"><c:out value ="${login.name }"/></span></h3>
		</div>

		<div class ="col-md-6" style ="margin-top :80px">
			<c:if test = "${login.auth ne 1}">
				<h3 id="pwTitle">비밀번호 수정</h3>
					<button type="button" class="btn btn-lg" id="changePwBtn" onclick ="location.href='/auth/user/pw-change'">수정</button>
			</c:if>
		</div>
	</div>
	
	
	<%@ include file="../includes/footer.jsp"%>
</body>
</html>