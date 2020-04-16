$(function(){
	$("#createChatBtn").click(function(){
		var roomName = $('#RoomName').val();
		
		var data = {
			roomName : roomName
		}
		
		console.log("room Name : " + roomName );
		
		if(!roomName){
			alert("방 이름을 입력해주세요");
			$("#RoomName").focus();
			return false;
		}
		
		var roomCheck = confirm("방을 만드시겠습니까?");
		
		if(roomCheck){
			createRoom(data);
			$("#chatModal").modal('hide');
		}
		
	});
	
	function createRoom(data){
		$.ajax({
			type : 'post',
			url :'/indianHome',
			data : JSON.stringify(data),
			contentType : 'application/json; charset=utf-8',
			success : function(result){
				console.log(result);
				location.href = result;
			}
		})
	};
	
})
