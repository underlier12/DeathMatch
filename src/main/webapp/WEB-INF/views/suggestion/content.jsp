<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../includes/meta.jsp"%>
<title>Register</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/suggestion/content.css" rel="stylesheet">

</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-10 col-md-offset-1">
				<p id="suggestTitle">건의 게시판
				<hr id="hr">
			</div>

			<div class="col-md-10 col-md-offset-1 register">
				<form id="registForm" method="post">
					<input type="hidden" name="userId" value='<c:out value = "${login.userId }"/>'>
					<table class="table table-bordered req" id="req">
						<tr>
							<td id="tdTitle">제목</td>
							<td><input type="text" class="title" name="title"
								value="${Suggestion.title }" readonly="readonly"></td>
						</tr>
						<tr>
							<td id="tdContent">내용</td>
							<td><textarea class="content" name="content" cols="110"
									readonly="readonly">
								${Suggestion.content }
								</textarea></td>
						</tr>
						<tr>
							<td id="tdSecret">비밀글</td>
							<td><input type="radio" id="public" class="secret"
								name="secret" value="0" checked> <label for="public">공개글</label>&nbsp;
								<input type="radio" id="secret" class="secret" name="secret"
								value="1"> <label for="secret">비밀글</label></td>
						</tr>
					</table>
				</form>
			</div>

			<div class="col-md-10 col-md-offset-1">
				<c:if test="${login.userId eq Suggestion.userId}">
					<button type="button" class="btn btn-default btn-sm" id="modifyBtn">수정</button>
					<button type="button" class="btn btn-default btn-sm" id="deleteBtn">삭제</button>
					<button type="button" class="btn btn-default btn-sm" id="cancleBtn"
						onclick="history.back()">취소</button>
				</c:if>
				<%-- <c:if test ="${login.auth eq 1}">
					<button type="button" class="btn btn-default btn-sm" id="answerBtn">삭제</button>
				</c:if> --%>
			</div>
		</div>
		
		<form id ="contentForm" action ="/suggestion/post-edit" method ="get">
			<input type="hidden" name="bno" value='<c:out value = "${Suggestion.bno}"/>'>
		</form>
	</div>

	<%@ include file="../includes/footer.jsp"%>
</body>
<script src="/js/suggestion/content.js"></script>
</html>
