<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<%@ include file="../includes/meta.jsp"%>
<link href="/css/main/myPage.css" rel="stylesheet">
<body>
<sec:authentication property="principal" var="user"/>
<c:set var="userText" value="${user.username}"/>

	<div id="userTable">
		<%@ include file="../main/sidebar.jsp"%>

		<div id ="info">
			<h2 id="title">회원 정보</h2>

			<table class="table table-bordered user">
				<tr>
					<td id="userIdTitle">아이디</td>
					<td><c:out value="${fn:substringBefore(userText,'@') }" /></td>
				</tr>
				<tr>
					<td id="userEmailTitle">이메일</td>
					<td><c:out value="${user.username }" /></td>
				</tr>
				<%--<tr>
					<td id="userNameTitle">이름</td>
					<td><c:out value="${login.name }" /></td>
				</tr>--%>
			</table>

			<div class="modifyDiv">
				<div class="btnDiv">
					<sec:authorize access = "isAuthenticated()">
						<button type="button" class="btn btn-lg" id="changePwBtn"
							onclick="location.href='/auth/user/pw-change'">비밀번호 수정</button>
					</sec:authorize>
				</div>
				<div class="btnDiv">
					<sec:authorize access = "isAuthenticated()">
						<button type="button" class="btn btn-lg" id="deleteUserBtn"
							onclick="location.href='/auth/user/withdrawal'">회원 탈퇴</button>
					</sec:authorize>
				</div>
				<div class="btnDiv">
					<sec:authorize access = "isAuthenticated()">
						<button type="button" class="btn btn-lg" id="mainBtn"
							onclick="location.href='/rooms'">돌아가기</button>
					</sec:authorize>
				</div>
			</div>
			<%@ include file="../includes/footer.jsp"%>
		</div>
	</div>

</body>
</html>