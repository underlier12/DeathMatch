$(function(){
	
	//WebSocket 연결하기
	var url = window.location.host; //웹 브라우저의 주소창의 포트까지 가져옴
	console.log("host url : " + url);
	var sock = new SockJS("http://"+url+"/ws/indian");
	
	var roomId = $('.content').data('room-id');
	var member = $('.content').data('member');
	var playerId1 = $("#playerId1");
	var playerId2 = $("#playerId2");
	
	var readyBtn = $("#readyBtn");
	var sendBtn = $("#sendBtn");
	
	var chatArea = $("#chatArea");
	var infoArea = $("#infoArea");
	var connectionArea = $("#connectionArea");

	// WebSocket actions
	
	// webSocket이 연결되었을때
	sock.onopen = function(){
		var join = {type :'JOIN', sender: member , roomId:roomId};
		sock.send(JSON.stringify(join));
		connectionArea.text("Connecting Server");
		console.log("sock.opened");
	}
	
	// 서버로부터 메세지를 받았을때
	sock.onmessage = function(e){
		var content = JSON.parse(e.data);
		var type = content.type;
		console.log(type);
		switch(type){
			case "TALK" 
				: chatArea.eq(0).prepend(content.sender + " : " + content.message +" " + "\n");
				break;
			case "JOIN" 
				: infoArea.eq(0).prepend(content.sender + " 님이 입장하였습니다 "+"\n");
				  console.log(playerId1.text());
				  if(!playerId1.val()){
					  playerId1.val(content.sender);
				  }else if(!playerId2.val()){
					  playerId2.val(content.sender);
				  }
				  break;
			case "READY"
				: infoArea.eq(0).prepend(content.sender + "님이 준비하셨습니다" +"\n");
			default:
				console.log("Default!!");
		}
	}

	
	//서버와 연결을 끊었을때
	sock.onclose = function(){
		console.log("sock.onclose");
		connectionArea.text("Server closed");
	}
	
	// webSocket finish
	
	/** Message **/
	
	function sendMessage(){
		var message = $("#message").val();
		console.log(message);
		var chat = {type :'TALK', sender : member, roomId : roomId,message : message};
		sock.send(JSON.stringify(chat));
		console.log("success submit");
	}
	
	sendBtn.click(function(){
		sendMessage();
	});
	
	readyBtn.click(function(){
		var readyData = {type : "READY", sender : member, roomId : roomId};
		sock.send(JSON.stringify(readyData));
		console.log("Success submit ReadyData");
	});
	
	
});