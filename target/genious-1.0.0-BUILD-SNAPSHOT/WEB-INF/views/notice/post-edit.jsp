<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../includes/meta.jsp"%>
<title>Edit</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/notice/post-edit.css" rel="stylesheet">

</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-10 col-md-offset-1">
				<p id="notice-title">공지게시판</p>
				<hr id="hr">
			</div>

			<div class="col-md-10 col-md-offset-1 register">
				<form id="modifyForm" action="/notice/post-edit" method="post">
					<input type="hidden" name="page" value='<c:out value="${cri.page }" />'> 
					<input type="hidden" name="perPageNum" value='<c:out value="${cri.perPageNum}" />'> 
					<input type="hidden" name="bno" value="${notice.bno }"> 
					<input type="hidden" name="userId" value="${notice.userId }">
					<input type="hidden" name="type" value='<c:out value = "${pageMaker.cri.type }"/>'> 
					<input type="hidden" name="keyword" value='<c:out value = "${pageMaker.cri.keyword }"/>'>
					
					<table class="table table-bordered req" id="req">
						<tr>
							<td id="tdTitle">제목</td>
							<td><input type="text" class="title" name="title" value="${notice.title }">
							</td>
						</tr>
						<tr>
							<td id="tdContent">내용</td>
							<td>
								<textarea class="content" name="content" cols="110">"${notice.content}"</textarea>
							</td>
						</tr>
						<tr>
							<td id="tdSecret">비밀글</td>
							<td>
								<input type="radio" id="public" class="secret" name="secret" value="0" checked> 
								<label for="public">공개글</label>&nbsp;
								<input type="radio" id="secret" class="secret" name="secret" value="1"> 
									<label for="secret">비밀글</label>
							</td>
						</tr>
					</table>
				</form>
			</div>

			<div class="col-md-10 col-md-offset-1">
				<sec:authorize access ="hasRole('ROLE_ADMIN')">
						<button type="button" class="btn btn-default btn-sm" id="modifyBtn">수정</button>
						<button type="button" class="btn btn-default btn-sm" id="deleteBtn">삭제</button>
						<button type="button" class="btn btn-default btn-sm" id="cancleBtn" onclick="history.back()">취소</button>
				</sec:authorize>
			</div>
		</div>
	</div>
</body>
<script src="/js/notice/post-edit.js"></script>
<%@ include file="../includes/footer.jsp"%>
</html>
