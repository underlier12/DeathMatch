$(function(){
	
	//WebSocket 연결하기
	var url = window.location.host; //웹 브라우저의 주소창의 포트까지 가져옴
	var sock = new SockJS("http://"+url+"/ws/chats");
	
	console.log(url);
	var roomId = $('.content').data('room-id');
	var member = $('.content').data('member');
	
	var readyBtn = $("#readyBtn");
	var messageArea = $("#messageArea");

	// WebSocket actions
	
	// webSocket이 연결되었을때
	sock.onopen = function(){
		var join = {type :'JOIN', sender: member , roomId:roomId};
		sock.send(JSON.stringify(join));
		messageArea.text("connection opened");
		console.log("connection opened");
	}
	
	// 서버로부터 메세지를 받았을때
	sock.onmessage = function(e){
		var content = JSON.parse(e.data);
		console.log(content.message);
		console.log(content.sender);
		messageArea.append(content.message + " " +"<br/>");
	}
	
	//서버와 연결을 끊었을때
	sock.onclose = function(){
		console.log("sock.onclose");
		messageArea.text("connection closed");
	}
	
	function sendMessage(){
		//sock.send -> 연결된 소켓에 메세지를 보냅니다
		var message = $("#message").val();
		var chat = {type :'TALK', sender : member, roomId : roomId,message : message};
		sock.send(JSON.stringify(chat));
		console.log("success submit");
	}
	
	function cardDeck(){
		
	}
	
	$("#sendBtn").click(function(){
		sendMessage();
	});
	
});