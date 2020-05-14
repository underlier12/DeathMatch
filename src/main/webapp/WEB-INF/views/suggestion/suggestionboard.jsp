<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../includes/meta.jsp"%>
<title>Register</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/suggestion/suggestionboard.css" rel="stylesheet">
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
						<c:forEach items="${list}" var="sugBoard">
							<tr>
								<td><c:out value="${sugBoard.bno }"></c:out></td>
								<td><a href="<c:out value ="${sugBoard.bno}"/>"
									class="title"> <c:out value="${sugBoard.title }"></c:out>
								</a></td>
								<td><c:out value="${sugBoard.userId }"></c:out></td>
								<td><c:out value="${sugBoard.regdate }"></c:out></td>
								<td><c:out value="${sugBoard.hit }"></c:out></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<div class="text-center" id="pageDiv">
					<ul class="pagination">
						<c:if test="${pageMaker.prev }">
							<li class="paginate_button previous"><a id="prevBtn"
								href="${pageMaker.startPage-1 }">◀</a></li>
						</c:if>
						<c:forEach var="num" begin="${pageMaker.startPage}"
							end="${pageMaker.endPage}">
							<li
								class="paginate_button ${pageMaker.cri.page == num ? 'active' : '' }">
								<a id="pageNum" href="${num }">${num }</a>
							</li>
						</c:forEach>
						<c:if test="${pageMaker.next}">
							<li class="paginate_button next"><a id="nextBtn"
								href="${pageMaker.endPage+1 }">▶</a></li>
						</c:if>
					</ul>
					<c:if test="${login ne null }">
						<button type="button" class="btn btn-default btn-sm" id="writeBtn">글쓰기</button>
					</c:if>
				</div>

				<div class="col-md-5 col-md-offset-4 search_area">
					<form id="searchForm" action="/suggestion/suggestionboard"
						method="get">
						<div id="search_div">
							<div>
								<select class="form-control search_type" name="type">
									<option value="T"
										<c:out value="${pageMaker.cri.type eq 'T' ? 'selected' : '' }" />>제목</option>
									<option value="C"
										<c:out value="${pageMaker.cri.type eq 'C' ? 'selected' : '' }" />>내용</option>
									<option value="W"
										<c:out value="${pageMaker.cri.type eq 'W' ? 'selected' : '' }" />>작성자</option>
								</select>
							</div>
							<div>
								<input type="text" class="form-control search_keyword"
									name="keyword"
									value='<c:out value="${pageMaker.cri.keyword }" />'
									autocomplete="off" placeholder="검색어"> <input
									type="hidden" name="page" value="${pageMaker.cri.page }">
								<input type="hidden" name="perPageNum"
									value="${pageMaker.cri.perPageNum }">
							</div>
							<div>
								<button class="btn btn-default btn-sm search_btn">검색</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>

		<form id="actionForm" action="/suggestion/suggestionboard"
			method="get">
			<input type="hidden" name="page" value="${pageMaker.cri.page }">
			<input type="hidden" name="perPageNum"
				value="${pageMaker.cri.perPageNum }"> <input type="hidden"
				name="type" value='<c:out value = "${pageMaker.cri.type }"/>'>
			<input type="hidden" name="keyword"
				value='<c:out value = "${pageMaker.cri.keyword }"/>'>
		</form>
	</div>
</body>
<script src="/js/suggestion/suggestionboard.js"></script>
<%@ include file="../includes/footer.jsp"%>
</html>
