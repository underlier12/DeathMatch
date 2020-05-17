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
    <div id="index">
		<div class="board">
			<div class="title">
				<p>전적 기록</p>
			</div>
			
			<div class="summary">
				<p>요약</p>
			</div>
			
			<div class="body">
				<table>
					<thead>
						<tr>
							<td>No</td>
							<td>Game Type</td>
							<td>User Id</td>
							<td>Opponent Id</td>
							<td>User Score</td>
							<td>Opponent Score</td>
							<td>Win or Lose</td>
						</tr>
						
					</thead>
					<tbody>
						<c:forEach items="${list}" var="record">
							<tr>
								<td><c:out value="${record.no }"></c:out></td>
								<td><c:out value="${record.gameType }"></c:out></td>
								<td><c:out value="${record.userId }"></c:out></td>
								<td><c:out value="${record.opponentId }"></c:out></td>
								<td><c:out value="${record.userScore }"></c:out></td>
								<td><c:out value="${record.opponentScore }"></c:out></td>
								<td><c:out value="${record.winLose }"></c:out></td>
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
			
		</div>
	    <%@ include file="../includes/footer.jsp" %>
    </div>
    
</body>
<script src="/js/record/record.js"></script>
</html>