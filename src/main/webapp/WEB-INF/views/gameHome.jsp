<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<body>

	<%@ include file="/WEB-INF/views/includes/header.jsp"%>
	<%@ include file="/WEB-INF/views/includes/sidebar.jsp"%>
	<div class="col-md-8 col-md-offset-2" style="margin-left: 32%">
		<h1>Room List</h1>
	</div>


	<div class="col-md-8 col-md-offset-2"
		style="margin-left: 32%; display: inline-block:">
		<button type="button" class="btn btn-default" id="btnCreate"
			style="display: inline-block:">방만들기</button>
		<button type="button" class="btn btn-default" id="btnCreate"
			style="display: inline-block:">튜토리얼</button>
	</div>

	<div class="container" style="margin-left: 32%">
		<table class="table">
			<colgroup>
				<col width="20%">
				<col width="80%">
			</colgroup>
			<c:forEach var="room" items="${rooms }">
				<tr>
					<td>${login.userEmail }</td>
				</tr>
				<tr>
					<td><a href="gameHome/<c:out value="${room.roomId}"/>"><c:out
								value="${room.name}" /></a></td>
				</tr>
			</c:forEach>
		</table>
	</div>

	<div class="text-center">
		<ul class="pagination">
			<c:if test="${pageMaker.prev }">
				<li><a
					href='<c:url value="/gameHome?page=${pageMaker.startPage-1 }"/>'><i
						class="fa fa-chevron-left"></i></a></li>
			</c:if>
			<c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage }"
				var="idx">
				<li
					<c:out value ="${pageMaker.cri.page == idx?'class =active':''}"/>>
					<a href="gameHome?page=${idx }">${idx }</a>
				</li>
			</c:forEach>
			<c:if test="${pageMaker.next && pageMaker.endPage >0 }">
				<li><a
					href='<c:url value="/gameHome?page=${pageMaker.endPage+1 }"/>'><i
						class="fa fa-chevron-right"></i></a></li>
			</c:if>
		</ul>
	</div>

	<%@ include file="/WEB-INF/views/includes/roomModal.jsp"%>
	<script src="/genious/js/gameHome.js?ver=1"></script>

</body>