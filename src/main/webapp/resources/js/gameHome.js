$(function(){
	
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
		}
		
	});
	
	function createRoom(roomName){
		$.ajax({
			type : 'post',
			url :'/genious/gameHome',
			data : JSON.stringify(roomName),
			contentType : 'application/json; charset=utf-8',
			success : function(result){
				console.log(result);
				location.href =result;
			}
		})
	};
	
	$('#btnTuto').click(function(){
		$('#tutoModal').modal();
		document.getElementById("clip").play(); 
	});
});