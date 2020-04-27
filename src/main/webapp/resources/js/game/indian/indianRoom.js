$(function(){
	
	//WebSocket 연결하기
	var url = window.location.host; //웹 브라우저의 주소창의 포트까지 가져옴
	console.log("host url : " + url);
	var sock = new SockJS("http://"+url+"/ws/indian");
	
	var roomId = $('.content').data('room-id');
	var member = $('.content').data('member');
	
	var playerId1 = $("#playerId1");
	var playerId2 = $("#playerId2");
	
	var infocard1 = $("#infocard1");
	var infocard2 = $("#infocard2");
	
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
	var betchip1Score = $("#betchip1Score");
	var betchip2Score = $("#betchip2Score");
	
	
	var betBtn = $("#betBtn");
	var betGiveUpBtn = $("#betGiveUpBtn");
	var betSendBtn = $("#betSendBtn");
	
	var card1;
	var card2;
	
	// chip calculator
	var chipBetting = $("#chipBetting");
	var upBtn = $("#chipUpBtn");
	var downBtn = $("#chipDownBtn");
	var allInBtn = $("#chipAllInBtn");
	var count = chipBetting.val();
	
	/** Prev hide **/
	/*betBtn.hide();
	betGiveUpBtn.hide();
	
	chip1.hide();
	chip2.hide();
	players.hide();
	chipBetting.hide();
	upBtn.hide();
	downBtn.hide();
	allInBtn.hide();
	betSendBtn.hide();*/
	
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
			case "START"
				: startRound(content);
				break;
			case "TURN"
				: whoseTurn(content);
				break;
			//case "RESULT" :
			//	resultRound(content);
			case "GIVEUP" 
				:giveUpRound(content);
				break;		
			default:
				console.log("Default!!");
		}
	}
	
	// end
	
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
		/*chip1.hide();
		chip2.hide();*/
	}
	
	function infoCard(content){
		console.log(content.checkPlayer);
		if(content.checkPlayer == member){
			infocard1.val("내 카드 입니다");
		}
	}
	
	function chat(content){
		chatArea.eq(0).prepend(content.sender + " : " + content.message+ " " + "\n");
	}
	
	// 초기 firstTurn은 먼저 입장한 플레이어 이다.
	function ready(content){
		console.log(content.message);
		console.log(content.player);
		if(content.message.substring(0,4) == '플레이어'){
			infoArea.eq(0).prepend(content.message + "\n");
			infoArea.eq(0).prepend(content.firstTurn + "\n");
			inGame();
			disableBtn(content);
		}else{
			infoArea.eq(0).prepend(content.message + "\n");
		}
	}
	
	function disableBtn(content){
		console.log(content.player);
		console.log(member);
		if(content.player!= member){
			betBtn.prop("disabled",true);
			betGiveUpBtn.prop("disabled",true);
			console.log("btn disabled!!");
		}else if(content.player == member){
			betBtn.prop("disabled",false);
			betGiveUpBtn.prop("disabled",false);
			console.log("btn abled!!");
		}
	};
	
	function inGame(){
		readyBtn.hide();
		showBettingBtn();
		//chipCalculatorShow();
	}
	
	function showBettingBtn(){
		betBtn.show();
		betGiveUpBtn.show();
	}
	
	function chipCalculatorShow(){
		chipBetting.show();
		upBtn.show();
		downBtn.show();
		allInBtn.show();
		betSendBtn.show();
	}
	
	function startRound(content){
		console.log(content.sender);
		console.log(content.player);
		chip1.show();
		chip2.show();
		players.show();
		bettingChip(content);
		if(content.player == member){
			cardSelect2(content);
			chipText(content);
		}else{
			cardSelect1(content);
			chipText(content);
		}
	}
	
	// betting Chip
	function bettingChip(content){
		console.log(content.player);
		console.log(content.chip1);
		console.log(content.chip2);
		var maxCnt = 0;
		if(content.player == member){
			maxCnt = content.chip1;
		}else{
			maxCnt = content.chip2;
		}
		upBtn.click(function(){
			if(content.player == member){
				maxCnt = content.chip1;
			}else{
				maxCnt = content.chip2;
			}
			count++;
			if(count>maxCnt){
				alert("칩을 " + maxCnt +" 이상 걸 수 없습니다");
			}else{
				chipBetting.val(count);
				console.log("Chip count: " + count);
			}
		});
		
		allInBtn.click(function(){
			alert("칩을 모두 다 거시겠습니까?");
			if(content.player == member){
				maxCnt = content.chip1;
			}else{
				maxCnt = content.chip2;
			}
			chipBetting.val(maxCnt);
			count = maxCnt;
		})
	}
	// end
	
	downBtn.bind("click",function downChip(){
		count--;
		console.log("Chip count : " + count);
		if(count < 0 ){
			alert("칩을 1개이상 걸어주세요!")
			count = 0;
		}else{
			chipBetting.val(count);
			console.log("Chip count: " + count);
			//count = 0;
		}
	});
	
	
	function chipText(content){
		chipScore1.text(content.chip1);
		chipScore2.text(content.chip2);
	}
	
	function cardSelect1(content){
		console.log(content.card1);
		card1 = content.card1;
		cardImg1.attr("src",defaultCardPath+"card"+card1+defaultPng);
	}
	
	function cardSelect2(content){
		console.log(content.card2);
		card2 = content.card2;
		cardImg2.attr("src",defaultCardPath+"card"+card2+defaultPng);
	}
	
	function giveUpRound(content){
		infoArea.eq(0).prepend(content.chipMessage + "\n");
		infoArea.eq(0).prepend(content.winner + "\n");
		console.log(content.chip1);
		console.log(content.chip2);
		console.log(content.chipMessage)
		console.log(content.winner);
		
		if(content.player == member){
			cardSelect1(content);
		}else{
			cardSelect2(content);
		}
		chipText(content);
	}
	
	
	function whoseTurn(content){
		console.log(content.message);
		console.log(content.player);
		infoArea.eq(0).prepend(content.message + "\n");
		disableBtn(content);
	}
	
	betBtn.click(function(){
		chipCalculatorShow();
	});
	
	/** Message **/
	
	sendBtn.click(function(){
		var message = $("#message").val();
		console.log(message);
		var chatData = {type :'TALK',sender:member,roomId:roomId,message:message};
		sock.send(JSON.stringify(chatData));
	});
	
	readyBtn.click(function(){
		var readyData = {type : "READY", sender : member, roomId : roomId};
		sock.send(JSON.stringify(readyData));
		console.log("Success Submit readyData");
	});
	
	betSendBtn.click(function(){
		var bettingData = {type : "BETTING",sender:member, roomId:roomId};
		sock.send(JSON.stringify(bettingData));
		console.log("Success submit bettingData");
	});
	
	betGiveUpBtn.click(function(){
		var resultData = {type:"GIVEUP",sender:member,roomId:roomId};
		sock.send(JSON.stringify(resultData));
		console.log("Success Submit betting resultData");
	});
	
	
});