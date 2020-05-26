<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../includes/meta.jsp"%>
<title>Content</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/notice/content.css" rel="stylesheet">

</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-10 col-md-offset-1">
				<p id="notice-title">공지게시판</p>
				<hr id="hr">
			</div>

			<div class="col-md-10 col-md-offset-1" id="contents">
				<form id="registForm" method="post">
					<input type="hidden" name="userId" value='<c:out value = "${login.userId }"/>'>
					<table class="table table-bordered req" id="req">
						<tr>
							<td class="tdTitle">제목</td>
							<td class="tdTitle">
								<input type="text" class="title" name="title" value="${notice.title }" readonly="readonly">
							</td>
						</tr>
						<tr>
							<td class="tdContent">내용</td>
							<td class="tdContent">
								<textarea class="content" name="content" cols="110" readonly="readonly">
									${notice.content }
								</textarea>
							</td>
						</tr>
						<tr>
							<td id="tdSecret">비밀</td>
							<td id="secretContent"><input type="radio" id="public" class="secret" name="secret" value="0" checked> 
								<label for="public">공개글</label>&nbsp; 
								<input type="radio" id="secret" class="secret" name="secret" value="1"> 
								<label for="secret">비밀글</label>
							</td>
						</tr>
					</table>
				</form>
			</div>

			<div class="col-md-10 col-md-offset-1">
				<c:if test="${login.userId eq notice.userId || login.auth eq 99}">
					<button type="button" class="btn btn-default btn-sm" id="modifyBtn">수정</button>
					<button type="button" class="btn btn-default btn-sm" id="deleteBtn">삭제</button>
				</c:if>
				<button type="button" class="btn btn-default btn-sm" id="goListBtn">목록</button>
			</div>
		</div>
	</div>

	<form id="contentForm" action="/notice/post-edit" method="get">
		<input type="hidden" name="bno"
			value='<c:out value = "${notice.bno}"/>'> <input
			type="hidden" name="page" value='<c:out value = "${cri.page}"/>'>
		<input type="hidden" name="perPageNum"
			value='<c:out value = "${cri.perPageNum}"/>'> <input
			type="hidden" name="type"
			value='<c:out value = "${pageMaker.cri.type }"/>'> <input
			type="hidden" name="keyword"
			value='<c:out value = "${pageMaker.cri.keyword }"/>'>
	</form>

	<%@ include file="../includes/footer.jsp"%>
</body>
<script src="/js/notice/content.js"></script>
</html>
