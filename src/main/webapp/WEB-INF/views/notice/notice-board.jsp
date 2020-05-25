<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../includes/meta.jsp"%>
<title>Notice-Board</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/notice/notice-board.css" rel="stylesheet">
</head>
<script>
</script>
<body>
	<input type ="hidden" id ="currentUser" value ="${login.userId }">
	<input type ="hidden" id ="auth" value ="${login.auth }">

	<div id="notice-home">

		<%@ include file="../main/sidebar.jsp"%>

		<div>
			<div class="button-flex">
				<div>
					<p id="notice-title">공지 게시판</p>
				</div>
				<div id="btnDiv">
					<button type="button" class="btn btn-default btn-sm" id="playBtn">게임하기</button>
					<button type="button" class="btn btn-default btn-sm" id="sugBtn">건의</button>
				</div>
			</div>
			
			<div>
				<table class="table" id="notice-body">
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
						<c:forEach items="${list}" var="noticeBoard">
							<tr>
								<td><c:out value="${noticeBoard.bno }"></c:out></td>
								<td>
									<a id="aTitle" class="title" href="${noticeBoard.pw }">
										<%-- <c:forEach begin="1" end="${noticeBoard.depth }">
											<c:if test="${noticeBoard.depth > 0 }">
											&nbsp;&nbsp;
										</c:if>
										</c:forEach> <c:if test="${noticeBoard.depth >0 }">
										[RE]
										</c:if> --%> 
										<c:if test="${noticeBoard.pw ne 1 }">
											<c:out value="${noticeBoard.title }" />
										</c:if> 
										<c:if test="${noticeBoard.pw eq 1 }">
											비밀글 입니다.
											<img class="secret_img" src="/images/secret.jpg">
										</c:if>
										<%-- <c:when test="${noticeBoard.pw eq 1 }">
											<img class="secret_img" src="/images/secret.jpg">
											비밀글 입니다.
										</c:when>
										<c:otherwise>
											<c:out value="${noticeBoard.title }" />
										</c:otherwise> --%>
										<input type="hidden" name="userId" value ="${noticeBoard.userId }">
										<input type="hidden" name="bno" value ="${noticeBoard.bno }">
									</a>
								</td>
								<td><c:out value="${noticeBoard.userId }"></c:out></td>
								<td><c:out value="${noticeBoard.regdate }"></c:out></td>
								<td><c:out value="${noticeBoard.hit }"></c:out></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<div class="text-center" id="pageDiv">
					<ul class="pagination">
						<c:if test="${pageMaker.prev }">
							<li class="paginate_button previous">
								<a id="prevBtn" href="${pageMaker.startPage-1 }">◀</a>
							</li>
						</c:if>
						<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
							<li class="paginate_button ${pageMaker.cri.page == num ? 'active' : '' }">
								<a id="pageNum" href="${num }">${num }</a>
							</li>
						</c:forEach>
						<c:if test="${pageMaker.next}">
							<li class="paginate_button next">
								<a id="nextBtn" href="${pageMaker.endPage+1 }">▶</a>
								</li>
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
								<input type="text" class="form-control" id="searchKeyWord" name="keyword" 
									value='<c:out value="${pageMaker.cri.keyword }" />' autocomplete="off" placeholder="검색어"> 
								<input type="hidden" name="page" value="${pageMaker.cri.page }">
								<input type="hidden" name="perPageNum" value="${pageMaker.cri.perPageNum }">
							</div>
							<div>
								<button class="btn btn-default" id="searchBtn">검색</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>

		<form id="actionForm" action="/notice" method="get">
			<input type="hidden" name="page" value="${pageMaker.cri.page }">
			<input type="hidden" name="perPageNum"
				value="${pageMaker.cri.perPageNum }"> <input type="hidden"
				name="type" value='<c:out value = "${pageMaker.cri.type }"/>'>
			<input type="hidden" name="keyword"
				value='<c:out value = "${pageMaker.cri.keyword }"/>'>
		</form>
	</div>
</body>
<script src="/js/notice/notice-board.js"></script>
<%@ include file="../includes/footer.jsp"%>
</html>
