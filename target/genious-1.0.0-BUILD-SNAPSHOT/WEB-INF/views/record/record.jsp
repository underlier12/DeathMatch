<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../includes/meta.jsp" %>
    <title>Genius Death Match</title>
    <%@ include file="../includes/header.jsp" %>
    <link href="/css/record/record.css" rel="stylesheet">
</head>

<body>	
    <div id="recordHistory">
    	<%@ include file="../main/sidebar.jsp"%>
    	
		<div class="board">
			<div class="record-title">
				<p>전적 기록</p>
			</div>
			
			<div class="summary">
				<p>요약</p>
				<div class="pull-right">
					<button type="button" class="btn btn-default btn-lg" id="btnBack">돌아가기</button>
				</div>
			</div>
			
			<div class="body">
				<table class="table">
					<thead>
						<tr class="attribute">
							<td>게임 종류</td>
							<td>본인 아이디</td>
							<td></td>
							<td>상대 아이디</td>
							<td>본인 점수</td>
							<td></td>
							<td>상대 점수</td>
							<td>승패</td>
						</tr>
						
					</thead>
					<tbody>
						<c:forEach items="${record}" var="record">
							<tr class="battleInfo">
								<c:choose>
									<c:when test="${record.gameType eq 'indian' }">
										<td>
											<img class="game-type" src="${pageContext.request.contextPath}/images/indianLogo.png">
										</td>
									</c:when>
									<c:otherwise>
										<td>
											<img class="game-type" src="${pageContext.request.contextPath}/images/UnionLogo.png">
										</td>
									</c:otherwise>
								</c:choose>
								<%-- <td><c:out value="${record.gameType }"></c:out></td> --%>
								<td><c:out value="${record.userId }"></c:out></td>
								<td>vs</td>
								<td><c:out value="${record.opponentId }"></c:out></td>
								<td><c:out value="${record.userScore }"></c:out></td>
								<td>vs</td>
								<td><c:out value="${record.opponentScore }"></c:out></td>
								<td class="wl"><c:out value="${record.winLose }"></c:out></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<div class="text-center" id="pageDiv">
					<ul class="pagination">
						<c:if test="${pageMaker.prev }">
							<li class="paginate_button previous">
								<a id="prevBtn"href="${pageMaker.startPage-1 }">◀</a>
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
					
				</div>
			</div>
			
			<form id="actionForm" action="/record" method="get">
				<input type="hidden" name="page" value="${pageMaker.cri.page }">
				<%-- <input type="hidden" name="perPageNum" value="${pageMaker.cri.perPageNum }"> 
				<input type="hidden" name="type" value='<c:out value = "${pageMaker.cri.type }"/>'>
				<input type="hidden" name="keyword" value='<c:out value = "${pageMaker.cri.keyword }"/>'> --%>
			</form>
			
		</div>
    </div>
    <%@ include file="../includes/footer.jsp" %>
    
</body>
<script src="/js/record/record.js"></script>
</html>