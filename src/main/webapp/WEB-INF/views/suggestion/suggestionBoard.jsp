<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../includes/meta.jsp"%>
<title>Register</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/suggestion/suggestionBoard.css" rel="stylesheet">
</head>
<script>
	/* if(${msg} != null){
		alert(${msg});
	} */
</script>
<body>

	<div class="container">
		
		<div class="row">
			<div class="col-md-10 col-md-offset-1 qnaLabel-row">
				<p>건의 게시판</p>
			</div>
		</div>

		<div class="row suggest_body">
			<div class="col-md-10 col-md-offset-1">
				<table class="table" id="suggest_body">
					<colgroup>
						<col width=50px;>
						<!-- NO -->
						<col width=250px;>
						<!-- TITLE -->
						<col width=80px;>
						<!-- NAME -->
						<col width=80px;>
						<!-- DATE -->
						<col width=50px;>
						<!-- HIT -->
					</colgroup>
					<thead>
						<tr id="title">
							<td>NO</td>
							<td>TITLE</td>
							<td>WRITER</td>
							<td>DATE</td>
							<td>HIT</td>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${list}" var="Suggestion">
							<tr>
								<td><c:out value="${Suggestion.bno }"></c:out></td>
								<td><a href="/suggestion/content?bno=${Suggestion.bno}">
										<c:out value="${Suggestion.title }"></c:out>
								</a></td>
								<td><c:out value="${Suggestion.userId }"></c:out></td>
								<td><c:out value="${Suggestion.regdate }"></c:out></td>
								<td><c:out value="${Suggestion.hit }"></c:out></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<div class="text-center" id="pageBtn">
					<ul class="pagination">
						<c:if test="${pageMaker.prev }">
							<li><a
								href='<c:url value="/suggestionBoard?page=${pageMaker.startPage-1 }"/>'><i
									class="fa fa-chevron-left"></i></a></li>
						</c:if>
						<c:forEach begin="${pageMaker.startPage}"
							end="${pageMaker.endPage }" var="idx">
							<li
								<c:out value ="${pageMaker.cri.page == idx?'class =active':''}"/>>
								<a id="pageNum" href="suggestionBoard?page=${idx }">${idx }</a>
							</li>
						</c:forEach>
						<c:if test="${pageMaker.next && pageMaker.endPage >0 }">
							<li><a
								href='<c:url value="/suggesionBoard?page=${pageMaker.endPage+1 }"/>'><i
									class="fa fa-chevron-right"></i></a></li>
						</c:if>
					</ul>
				</div>
			</div>
		</div>

		<div class="col-md-10 col-md-offset-1">
			<c:if test="${login ne null }">
				<button type="button" class="btn btn-default btn-sm" id="writeBtn">글쓰기</button>
			</c:if>
		</div>
	</div>
</body>
<script src="/js/suggestion/suggestionBoard.js"></script>
<%@ include file="../includes/footer.jsp"%>
</html>
