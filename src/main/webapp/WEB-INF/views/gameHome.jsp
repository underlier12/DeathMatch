<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<body>

	<%@ include file="/WEB-INF/views/includes/header.jsp"%>
	
	<%@ include file="/WEB-INF/views/includes/sidebar.jsp"%>
	<div class="col-md-8 col-md-offset-2" style ="margin-left:32%">
		<h1>Room List</h1>
	</div>
	
	
	<div class="col-md-8 col-md-offset-2" style ="margin-left:32%; display:inline-block:">
		<button type="button" class="btn btn-default" id="btnCreate" style="display:inline-block:">
		방만들기</button>
		<button type="button" class="btn btn-default" id="btnCreate" style="display:inline-block:">
		튜토리얼</button>
	</div>

	<div class="container" style ="margin-left:32%">
		<table class ="table">
			<colgroup>
				<col width="20%">
				<col width="80%">
			</colgroup>
			<c:forEach var="room" items="${rooms }">
				<tr>
					<td>${login.userEmail }</td>
				</tr>
				<tr>
				<td>
				<a href="gameHome/<c:out value="${room.roomId}"/>"><c:out
						value="${room.name}" /></a></td>
				</tr>
			</c:forEach>
		</table>
	</div>


	<%@ include file="/WEB-INF/views/includes/roomModal.jsp"%>
	<%-- <%@ include file="/WEB-INF/views/includes/footer.jsp"%> --%>
</body>

<script type="text/javascript">

	$(document).ready(function(){
		
	$("#btnCreate").click(function() {
		$("#roomModal").modal();
	}); 
	
	$("#createRoom").click(function(){
		var roomName = $('#RoomName').val();
		
		if(!roomName){
			alert("방 이름을 입력해주세요");
			$('#RoomName').focus();
			return false;
		}
		
		var roomCheck = confirm('방을 만드시겠습니까?');
		
		if(roomCheck){
			createRoom(roomName);
			$('#roomModal').modal('hide');
			//reload();
			
		}
		
	});
	
	function createRoom(roomName){
		
		$.ajax({
			type : 'post',
			url :'/genious/gameHome',
			data : JSON.stringify(roomName),
			contentType : 'application/json; charset=utf-8',
			success : function(result){
				console.log(result)
				location.reload();
			}
		})
		};
		
	});
	
	
</script>