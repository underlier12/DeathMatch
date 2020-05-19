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

			<div class="col-md-10 col-md-offset-1" id="contents">
				<form id="registForm" method="post">
					<input type="hidden" name="userId"
						value='<c:out value = "${login.userId }"/>'>
					<table class="table table-bordered req" id="req">
						<tr>
							<td class="tdTitle">제목</td>
							<td class="tdTitle"><input type="text" class="title"
								name="title" value="${Suggestion.title }" readonly="readonly"></td>
						</tr>
						<tr>
							<td class="tdContent">내용</td>
							<td class="tdContent"><textarea class="content"
									name="content" cols="110" readonly="readonly">
								${Suggestion.content }
								</textarea></td>
						</tr>
						<tr>
							<td id="tdSecret">비밀</td>
							<td id="secretContent"><input type="radio" id="public"
								class="secret" name="secret" value="0" checked> <label
								for="public">공개글</label>&nbsp; <input type="radio" id="secret"
								class="secret" name="secret" value="1"> <label
								for="secret">비밀글</label></td>
						</tr>
					</table>
				</form>
			</div>

			<div class="col-md-10 col-md-offset-1">
				<c:if
					test="${login.userId eq Suggestion.userId || login.auth eq 99}">
					<button type="button" class="btn btn-default btn-sm" id="modifyBtn">수정</button>
					<button type="button" class="btn btn-default btn-sm" id="deleteBtn">삭제</button>
					<button type="button" class="btn btn-default btn-sm" id="goListBtn">목록</button>
					<c:if test="${login.auth eq 99 }">
						<button type="button" class="btn btn-default btn-sm"
							id="answerBtn">답변</button>
					</c:if>
				</c:if>
			</div>
		</div>

		<div class="col-md-10 col-md-offset-1 reply_area">
			<table class="table table-bordered reg_reply_tbl">
				<c:if test="${login eq null }">
					<tr>
						<td>회원에게만 댓글 작성 권한이 있습니다.</td>
					</tr>
				</c:if>
				<c:if test="${login ne null }">
					<tr>
						<td id="replyTitle">댓글 달기</td>
					</tr>
					<tr>
						<td id="replyContent"><textarea id="reply_content"
								class="form-control" name="reply_content" autocomplete="off"></textarea>
						
						<button type="button" id="writeReply"
							class="btn btn-default btn-sm reg_reply">등록</button>
						</td>
					</tr>
				</c:if>
			</table>

			<div id="getReply"></div>
		</div>

	</div>

	<form id="contentForm" action="/suggestion/post-edit" method="get">
		<input type="hidden" name="bno"
			value='<c:out value = "${Suggestion.bno}"/>'> <input
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
<script src="/js/suggestion/content.js"></script>
</html>
