<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>

<link href="/css/main/myPage.css" rel="stylesheet">
<body>

	<div id="userTable">
		<%@ include file="../main/sidebar.jsp"%>

		<div id ="info">
			<h2 id="title">회원 정보</h2>

			<table class="table table-bordered user">
				<tr>
					<td id="userIdTitle">아이디</td>
					<td><c:out value="${login.userId }" /></td>
				</tr>
				<tr>
					<td id="userEmailTitle">이메일</td>
					<td><c:out value="${login.userEmail }" /></td>
				</tr>
				<tr>
					<td id="userNameTitle">이름</td>
					<td><c:out value="${login.name }" /></td>
				</tr>
			</table>

			<div class="modifyDiv">
				<div class="btnDiv">
					<c:if test="${login.auth ne 1}">
						<button type="button" class="btn btn-lg" id="changePwBtn"
							onclick="location.href='/auth/user/pw-change'">비밀번호 수정</button>
					</c:if>
				</div>
				<div class="btnDiv">
					<c:if test="${login.auth ne 1}">
						<button type="button" class="btn btn-lg" id="deleteUserBtn"
							onclick="location.href='/auth/user/withdrawal'">회원 탈퇴</button>
					</c:if>
				</div>
				<div class="btnDiv">
					<c:if test="${login.auth ne 1}">
						<button type="button" class="btn btn-lg" id="mainBtn"
							onclick="location.href='/rooms'">돌아가기</button>
					</c:if>
				</div>
			</div>
			<%@ include file="../includes/footer.jsp"%>
		</div>
	</div>

</body>
</html>