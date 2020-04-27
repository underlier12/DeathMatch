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
			betSendBtn.prop("disabled",true);
			betBtn.prop("disabled",true);
			allInBtn.prop("disabled",true);
			betGiveUpBtn.prop("disabled",true);
			upBtn.prop("disabled",true);
			downBtn.prop("disabled",true);
			console.log("btn disabled!!");
		}else if(content.player == member){
			betBtn.prop("disabled",false);
			allInBtn.prop("disabled",false);
			betGiveUpBtn.prop("disabled",false);
			betSendBtn.prop("disabled",false);
			upBtn.prop("disabled",false);
			downBtn.prop("disabled",false);
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
			defaultBetting(content);
		}else{
			cardSelect1(content);
			chipText(content);
			defaultBetting(content);
		}
	}
	
	function defaultBetting(content){
		var maxChip1 = content.chip1-1;
		var maxChip2 = content.chip2-1;
		console.log("chip1: " + maxChip1);
		console.log("chip2: " + maxChip2);
		chipScore1.text("X"+maxChip1);
		chipScore2.text("X"+maxChip2);
		betchip1Score.text("X"+1);
		betchip2Score.text("X"+1);
	}

	
	// betting Chip javascript
	
	var chipCount =1;
	var checkMaxChip = 0;
	var player1Chip = 0;
	var player2Chip = 0;
	var currentPlayer;
	
	function bettingChip(content){
		currentPlayer = content.player;
		player1Chip = content.chip1;
		player2Chip = content.chip2;
		
		console.log(content.player);
		console.log(content.chip1);
		console.log(content.chip2);
		
		chipCount = chipBetting.val();
		console.log("default chipCount: " + chipCount);
		// 유저별로 maxChip을 다르게 설정
		if(content.player == member){
			checkMaxChip = content.chip1-1;
		}else{
			checkMaxChip = content.chip2-1;
		}
	};
	
	upBtn.click(function(){
		console.log(currentPlayer);
		chipCount = chipBetting.val();
		chipCount++;
		console.log("upBtn chipCount: " + chipCount);
		
		if(chipCount>checkMaxChip){
			alert("칩을 " + checkMaxChip +" 초과해서 걸 수 없습니다");
			player1Chip = player1Chip-chipCount-1;
			chipCount--;
			return;
		}else if(currentPlayer == member){
			var chip1Max = checkMaxChip; 
			chipBetting.val(chipCount);
			console.log("player1Chip :" + chip1Max);
			chip1Max-=chipCount;
			console.log(chip1Max);
			chipScore1.text("X"+ chip1Max);
		}else if(currnetPlayer !=member){
			var chip2Max = checkMaxChip;
			chipBetting.val(chipCount);
			console.log("player1Chip :" + chip2Max);
			chip2Max -=chipCount;
			console.log(chip2Max);
			chipScore2.text("X"+chip2Max);
		}
		console.log("Result Chip count: " + chipCount);
	});
	
	downBtn.click(function(){
		chipCount--;
		console.log("Chip count : " + chipCount);
		if(chipCount < 0 ){
			alert("칩을 1개이상 걸어주세요!")
			chipCount = 0;
			return;
		}
		if(currentPlayer == member){
			var chip1 = chipScore1.text().substr(1, 2);
			chip1 = Number(chip1)+1;
			console.log(jQuery.type(chip1));
			console.log("chip1Score: " + chip1);	
			chipBetting.val(chipCount);
			chipScore1.text("X"+ chip1);
			console.log("Chip count: " + chipCount);
		}
	});
	
	allInBtn.click(function(){
		alert("칩을 모두 다 거시겠습니까?");
		if(currentPlayer == member){
			chipScore1.text("X"+ 0);
		}else if(currentPlayer !=member){
			chipScore2.text("X"+ 0);
		}
		chipBetting.val(checkMaxChip);
		chipCount = checkMaxChip;
	})
	
	function chipText(content){
		chipScore1.text("X" +content.chip1);
		chipScore2.text("X" +content.chip2);
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