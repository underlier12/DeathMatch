<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="../includes/meta.jsp" %>
    <title>ChatRoom List</title>
    <%@ include file="../includes/header.jsp" %>
</head>
<body>
	<div>
		<button type ="button" id="chatBtn">방만들기</button>
	</div>
	
	<div style = color:#ECA50E;>
		<c:forEach var ="room" items="${indianRooms}" varStatus ="status" >
			<div class="chatRoom">
				<p>${status.count} : <c:out value = "${room.roomName}"/> </p>
			<button type="button" class="gameJoinBtn"
								onclick="location.href='indianHome/<c:out value="${room.roomId}"/>'">Join</button>
			</div>
		</c:forEach>
	</DIV>
	
	<%@ include file="../modal-indian.jsp"%>
	<script src="/js/chatHome.js"></script>
</body>
<script type="text/javascript">
	
	$(function(){
		$("#chatBtn").click(function(){
			$("#chatModal").modal();
		})
	})
</script>
</html>