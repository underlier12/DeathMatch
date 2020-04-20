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

	var defaultCardPath = "/resources/images/indiancards/";
	var defaultPng = ".png";
	
	var cardImg1 = $("#card1");
	var cardImg2 = $("#card2");
	
	var chip1 = $("#chip1");
	var chip2 = $("#chip2");
	
	var players = $(".players");
	
	var chipScore1 = $("#chipScore1");
	var chipScore2 = $("#chipScore2");
	
	/** Prev hide **/
	chip1.hide();
	chip2.hide();
	players.hide();
	
	
	// WebSocket actions
	
	// websocket Connect
	sock.onopen = function(){
		
		var join = {type :'JOIN', sender: member , roomId:roomId};
		sock.send(JSON.stringify(join));
		connectionArea.text("Connecting Server");
		console.log("sock.opened");
	}
	
	// websocket close
	sock.onclose = function(){
		console.log("sock.onclose");
		connectionArea.text("Server closed");
	}
	
	// message from server
	sock.onmessage = function(e){
		var content = JSON.parse(e.data);
		console.log(content.type);
		switch(content.type){
			case "LOAD":
				loadPlayer(content);
				break;
			case "TALK" 
				: chat(content);
				break;
			case "JOIN" 
				: joinPlayer(content);
				  break;
			case "READY"
				: ready(content);
				break;
			case "DRAW"
				: draw(content);
				break;
			default:
				console.log("Default!!");
		}
	}
	
	function loadPlayer(content){
		switch (content.message) {
		case "PLAYER":
			if(!playerId1.val()){
				playerId1.val(content.player);
			}else if(!playerId2.val()){
				playerId2.val(content.player);
			}
			break;
		}
	}
	
	function joinPlayer(content){
		infoArea.eq(0).prepend(content.message +"\n");
		if(!playerId1.val()){
			playerId1.val(content.player);
		}else if(!playerId2.val()){
			playerId2.val(content.player);
		}
		chip1.hide();
		chip2.hide();
	}
	
	function chat(content){
		chatArea.eq(0).prepend(content.sender + " : " + content.message+ " " + "\n");
	}
	
	function ready(content){
		console.log(content.message);
		if(content.message.substring(0,4) == '플레이어'){
			infoArea.eq(0).prepend(content.message + "\n");
			inGame();
		}else{
			infoArea.eq(0).prepend(content.message + "\n");
		}
	}

	function draw(content){
		console.log(content.sender);
		console.log(content.player);
		chip1.show();
		chip2.show();
		players.show();
		if(content.player == member){
			cardSelect2(content);
			chipText(content);
		}else{
			cardSelect1(content);
			chipText(content);
		}
		
	}
	
	function chipText(content){
		chipScore1.text(content.chip1);
		chipScore2.text(content.chip2);
	}
	
	function cardSelect1(content){
		console.log(content.card1);
		cardImg1.attr("src",defaultCardPath+"card"+content.card1+defaultPng);
	}
	
	function cardSelect2(content){
		console.log(content.card2);
		cardImg2.attr("src",defaultCardPath+"card"+content.card2+defaultPng);
	}
	
	function inGame(){
		readyBtn.hide();
	}
	
	/** Message **/

	
	sendBtn.click(function(){
		var message = $("#message").val();
		console.log(message);
		var chatData = {type :'TALK',sender:member,roomId:roomId,message:message};
		sock.send(JSON.stringify(chatData));
		console.log("Success submit chatData");
	});
	
	readyBtn.click(function(){
		var readyData = {type : "READY", sender : member, roomId : roomId};
		sock.send(JSON.stringify(readyData));
		console.log("Success submit readyData");
	});
	
	
});