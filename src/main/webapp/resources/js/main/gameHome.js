$(function(){
	var msg = $('.content').data('msg');
	if(msg) alert(msg);
	
	$("#btnCreate").click(function() {
		$("#roomModal").modal();
	}); 
	
	$("#createRoomBtn").click(function(){
		var gameType = $('input[name="type"]:checked').val();
		var roomName = $('#RoomName').val();
		
		var data = {
				gameType: gameType,
				roomName: roomName
		}
		
		console.log("room type : " + gameType);
		
		if(!roomName){
			alert("방 이름을 입력해주세요");
			$('#RoomName').focus();
			return false;
		}
		
		var roomCheck = confirm('방을 만드시겠습니까?');
		
		if(roomCheck){
			createRoom(data);
			$('#roomModal').modal('hide');
		}
		
	});
	
	function createRoom(data){
		$.ajax({
			type : 'post',
			url :'/rooms',
			data : JSON.stringify(data),
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
	
	$('.radioBtn').change(function(){	
    	$('.radioBtn').next().children().toggleClass("boundary");
    });
	
});