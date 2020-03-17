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


	<%@ include file="/WEB-INF/views/includes/roomModal.jsp"%>
	<script src ="/genious/js/gameHome.js?ver=1"></script>
	<%-- <%@ include file="/WEB-INF/views/includes/footer.jsp"%> --%>
</body>