<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<body>

	<%@ include file="/WEB-INF/views/includes/header.jsp"%>

	<div class="col-md-10 col-md-offset-2">
		<h1>Room List</h1>
	</div>

	<div class="col-md-10 col-md-offset-2">
		<table class="table">
			<colgroup>
				<col width="20%">
				<col width="80%">
			</colgroup>

			<!-- 한개의 방 만들어짐 -->
			<c:forEach var="room" items="${rooms }">
				<tr>
					<td>현재 User :${login.userEmail }</td>
				</tr>

				<tr>
					<%-- <td>${userList[1].userEmail}</td> --%>
					<td>${userList[0]}</td>
				</tr>

				<c:if test="${userList[1] != null}">
					<tr>
						<%-- <td>${userList[2].userEmail}</td> --%>
						<td>${userList[1] }</td>
					</tr>
				</c:if>

				<tr>
					<td><a href="gameHome/<c:out value="${room.roomId}"/>"><c:out
								value="${room.name}" /></a></td>
				</tr>
			</c:forEach>
		</table>
	</div>



	<div class="col-md-10 col-md-offset-2">
		<button type="button" class="btn btn-default" id="btnCreate">
			방만들기(모달)</button>
	</div>



	<%@ include file="/WEB-INF/views/includes/roomModal.jsp"%>
	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
</body>

<script type="text/javascript">
	$(document).ready(function() {

		$("#btnCreate").click(function() {
			$("#roomModal").modal();
		});

		$("#createRoom").click(function() {3
			var roomName = $('#RoomName').val();

			if (!roomName) {
				alert("방 이름을 입력해주세요");
				$('#RoomName').focus();
				return false;
			}

			var roomCheck = confirm('방을 만드시겠습니까?');

			if (roomCheck) {
				createRoom(roomName);
				$('#roomModal').modal('hide');
			}

		});

		function createRoom(roomName) {

			$.ajax({
				type : 'post',
				url : '/genious/gameHome',
				data : JSON.stringify(roomName),
				contentType : 'application/json; charset=utf-8',
				success : function(result) {
					console.log(result)
					location.reload();
				}
			})
		}
		;

	});
</script>