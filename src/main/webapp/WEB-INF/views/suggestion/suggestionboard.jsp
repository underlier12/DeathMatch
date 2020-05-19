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
</script>
<body>
	<input type ="hidden" id ="currentUser" value ="${login.userId }">
	<input type ="hidden" id ="auth" value ="${login.auth }">

	<div id="suggestionHome">

		<div id="sidebar">
			<%@ include file="../main/sidebar.jsp"%>
		</div>

		<div>
			<div class="button-flex">
				<div>
					<p id="suggestTitle">건의 게시판</p>
				</div>
				<div id="btnDiv">
					<button type="button" class="btn btn-default btn-sm" id="playBtn">게임하기</button>
					<button type="button" class="btn btn-default btn-sm" id="noticeBtn">공지</button>
				</div>
			</div>
			
			<div class="suggest_body">
				<table class="table" id="suggest_body">
					<colgroup>
						<col width=50px;>
						<col width=200px;>
						<col width=80px;>
						<col width=80px;>
						<col width=60px;>
					</colgroup>
					<thead>
						<tr id="title">
							<td>번호</td>
							<td>제목</td>
							<td>작성자</td>
							<td>작성일</td>
							<td>조회수</td>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${list}" var="sugBoard">
							<tr>
								<td><c:out value="${sugBoard.bno }"></c:out></td>
								<td><a id="aTitle" class="title" href="${sugBoard.pw }">
										<c:forEach begin="1" end="${sugBoard.depth }">
											<c:if test="${sugBoard.depth > 0 }">
											&nbsp;&nbsp;
										</c:if>
										</c:forEach> <c:if test="${sugBoard.depth >0 }">
										[RE]
										</c:if> <c:if test="${sugBoard.pw ne 1 }">
											<c:out value="${sugBoard.title }" />
										</c:if> 
										<c:if test="${sugBoard.pw eq 1 }">
											비밀글 입니다.
											<img class="secret_img" src="/images/secret.jpg">
										</c:if>
										<input type="hidden" name="userId" value ="${sugBoard.userId }">
										<input type="hidden" name="bno" value ="${sugBoard.bno }">
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
					<form id="searchForm" action="/suggestion" method="get">
						<div id="search_div">
							<div>
								<select class="form-control" id="searchType" name="type">
									<option value="T"
										<c:out value="${pageMaker.cri.type eq 'T' ? 'selected' : '' }" />>제목</option>
									<option value="C"
										<c:out value="${pageMaker.cri.type eq 'C' ? 'selected' : '' }" />>내용</option>
									<option value="W"
										<c:out value="${pageMaker.cri.type eq 'W' ? 'selected' : '' }" />>작성자</option>
								</select>
							</div>
							<div>
								<input type="text" class="form-control" id="searchKeyWord"
									name="keyword"
									value='<c:out value="${pageMaker.cri.keyword }" />'
									autocomplete="off" placeholder="검색어"> <input
									type="hidden" name="page" value="${pageMaker.cri.page }">
								<input type="hidden" name="perPageNum"
									value="${pageMaker.cri.perPageNum }">
							</div>
							<div>
								<button class="btn btn-default" id="searchBtn">검색</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>

		<form id="actionForm" action="/suggestion" method="get">
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
