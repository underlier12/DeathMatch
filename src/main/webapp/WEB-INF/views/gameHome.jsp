<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<body>

	<%@ include file="/WEB-INF/views/includes/header.jsp"%>
	
	
	<div class="col-md-10 col-md-offset-2">
		<h1>Room List</h1>
	</div>

	<div class="col-md-10 col-md-offset-2">
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
	
	<!-- <table class ="table get_roomList">
		<colgroup>
			<col width="20%">
			<col width="80%">
		</colgroup>
		<tbody>
		</tbody>
	</table> -->


	<!-- <div class="col-md-10 col-md-offset-2">
		<h1>CREATE ROOM</h1>
		<form method="post" action="">
			<div class="content">
				<input type="text" name="name">
				<p>
					<input type="submit" value="방 만들기" />
			</div>
		</form>
	</div> -->


	<div class="col-md-10 col-md-offset-2">
		<button type="button" class="btn btn-default" id="btnCreate">
		방만들기(모달)</button>
	</div>



	<%@ include file="/WEB-INF/views/includes/roomModal.jsp"%>
	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
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
				//addRoomHtml(roomName)
				console.log(result)
				location.reload();
			}
		})
	};
	
	
 	/* function reload(){
		$("#roomList").load(window.location.href + "#roomList");
	}; */
	
	/* function addRoomHtml(roomName){
		var roomList = '${rooms}';
		var replyHtml = '';
		$('.get_roomList').html(remplyHtml);
		
		for(var i =0; i<roomList.length; i++){
			replyHtml += '<tr>'
				   + '	<td>' +'</td>'
				   + '	<td>' + roomList[i].name + '</td>'
				   + '</tr>'
				   + '<tr>'
				   + '	<td colspan="3">' + 테스트 + '</td>'
				   + '</tr>';
		}
				  
		$('.get_roomList').append(replyHtml);
			
			
		} */

	
	});
	
	
</script>