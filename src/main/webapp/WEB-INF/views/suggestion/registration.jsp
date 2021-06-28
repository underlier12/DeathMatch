<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../includes/meta.jsp"%>
<title>Register</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/suggestion/registration.css" rel="stylesheet">

</head>
<body>
<sec:authentication property="principal" var="user"/>
<c:set var="userText" value="${user.username}"/>

	<div class="container">
		<div class="row">
			<div class="col-md-10 col-md-offset-1">
				<p id="suggestTitle">건의 게시판
				<hr id="hr">
			</div>

			<div class="col-md-10 col-md-offset-1 register">
				<form id="registForm" action ="/suggestion/registration" method="post">
					<%--<input type ="hidden" name ="userId" value ='<c:out value = "${login.userId }"/>'>--%>
					<input type ="hidden" name ="userId" value ='<c:out value = "${fn:substringBefore(userText,'@')}"/>'>
					<table class="table table-bordered req" id="req">
						<tr>
							<td class="tdTitle">제목</td>
							<td class="tdTitle"><input type="text" class="title" name="title"></td>
						</tr>
						<tr>
							<td class="tdContent">내용</td>
							<td class="tdContent"><textarea class="content" name="content" cols="110"></textarea>
							</td>
						</tr>
						<tr>
							<td class="tdSecret">비밀글</td>
							<td class="tdSecret"><input type="radio" id="public" class="secret"
								name="pw" value="0" checked> <label for="public">공개글</label>&nbsp;
								<input type="radio" id="secret" class="secret" name="pw"
								value="1"> <label for="secret">비밀글</label></td>
						</tr>
						
					</table>
				</form>
			</div>

			<div class="col-md-10 col-md-offset-1 reg_qna_footer">
				<button type="button" class="btn btn-default btn-sm" id="regiBtn">등록</button>
				<button type="button" class="btn btn-default btn-sm" id="cancleBtn"
					onclick="history.back()">취소</button>
			</div>
		</div>
	</div>
	<%@ include file="../includes/footer.jsp"%>
</body>
<script src="/js/suggestion/registration.js"></script>
</html>
