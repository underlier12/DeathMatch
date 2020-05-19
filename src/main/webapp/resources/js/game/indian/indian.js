$(function(){
	
	// WebSocket 연결하기
	var url = window.location.host; // 웹 브라우저의 주소창의 포트까지 가져옴;
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
	
	var player1BetChip;
	var player2BetChip;
	var betChip;
   	var betCheck = true;
	var betCheck2 = true;
	var betCheck3 = true;
	
	var drawCheck = 0;
   	
	
	/** Prev hide * */
	/*
	 * betBtn.hide(); betGiveUpBtn.hide();
	 * 
	 * chip1.hide(); chip2.hide(); players.hide(); chipBetting.hide();
	 * upBtn.hide(); downBtn.hide(); allInBtn.hide(); betSendBtn.hide();
	 */
	
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
				: bettingAct(content);
				break;
			case "BETRESULT" :
				resultRound(content);
				break;
			case "GIVEUP" 
				:giveUpRound(content);
				break;		
			case "NEXT"
				:nextRound(content);
				break;
			case "DRAW"
				:draw(content);
				break;
			case "NEXTDRAW"
				: nextDrawRound(content);
				break;
			case "END"
				: endGame(content);
				break;
			case "LEAVE"
				: leavePlayer(content);
				break;
			case "STOP"
				:stopGame(content);
			default:
				console.log("Default!!");
		}
	}
	
	function leavePlayer(content){
		if(content.player == playerId1.val()){
			playerId1.val('');
		}else{
			playerId2.val('');
		}
		infoArea.eq(0).prepend(content.message + "\n");
	}
	
	function stopGame(content){
		infoArea.eq(0).prepend(content.message + "\n");
		readyBtn.show();
		resetChip();
	}
	
	function resetChip(){
		chipScore1.text("X"+0);
		chipScore2.text("X"+0);
		betchip1Score.text("X"+0);
		betchip2Score.text("X"+0);
	}
	
	$("#leave").click(function(){
		location.href='../../rooms'
	});
	
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
		/*
		 * chip1.hide(); chip2.hide();
		 */
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
	
	function endGame(content){
		infoArea.eq(0).prepend(content.message + "\n");
		infoArea.eq(0).prepend(content.winner + "\n");
		endGameChipText(content);
	}
	
	// 처음 게임 시작 disableBtn
	function disableBtn(content){
		console.log(content.player);
		console.log(member);
		if(content.player!= member){
			disableAll();
		}else if(content.player == member){
			enableAll();
		}
	};
	
	function disableAll(){
		betSendBtn.prop("disabled",true);
		betBtn.prop("disabled",true);
		allInBtn.prop("disabled",true);
		betGiveUpBtn.prop("disabled",true);
		upBtn.prop("disabled",true);
		downBtn.prop("disabled",true);
		console.log("btn disabled!!");
	}
	
	function enableAll(){
		betBtn.prop("disabled",false);
		allInBtn.prop("disabled",false);
		betGiveUpBtn.prop("disabled",false);
		betSendBtn.prop("disabled",false);
		upBtn.prop("disabled",false);
		downBtn.prop("disabled",false);
		console.log("btn abled!!");
	}
	
	function inGame(){
		readyBtn.hide();
		showBettingBtn();
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
		player2MaxChip(content);
		cardSelect(content);
	}
	
	function nextRound(content){
		var message = content.message;
		var turnPlayer = message.substring(0,message.indexOf("님"));
		console.log(content.player);
		console.log("TurnPlayer " + turnPlayer);
		infoArea.eq(0).prepend(message + "\n");
		player2MaxChip(content);
		cardSelect(content);
		if(turnPlayer != member){
			disableAll();
		}else if(turnPlayer == member){
			enableAll();
		}
	}
	
	function nextDrawRound(content){
		var message = content.message;
		var turnPlayer = message.substring(0,message.indexOf("님"));
		console.log(content.player);
		console.log("TurnPlayer " + turnPlayer);
		infoArea.eq(0).prepend(message + "\n");
		drawMaxChip(content);
		cardSelect(content);
		if(turnPlayer != member){
			disableAll();
		}else if(turnPlayer == member){
			enableAll();
		}
	}
	
	function draw(content){
		console.log("Draw");
		drawCheck++;
		infoArea.eq(0).prepend(content.message + "\n");
		betChipScore(content)
		openCard(content);
		setTimer();
		startTimer();
		timer.show();
	}
	
	// betting Chip javascript
	
	var chipCount =1;
	var checkMaxChip = 0;
	var player1Chip = 0;
	var player2Chip = 0;
	var p1MaxChipCheck;
	var p2MaxChipCheck;
	var getBetChip1;
	var getBetChip2;
	var currentPlayer;
	
	function player2MaxChip(content){
		p1MaxChipCheck = content.player1Chip;
		p2MaxChipCheck = content.player2Chip;
		player1Chip = content.player1Chip-1;
		player2Chip = content.player2Chip-1;
		currentPlayer = content.player;
		showChipText(content);
		chipCount = chipBetting.val();
		console.log("default chipCount: " + chipCount);
		if(content.player == member){
			checkMaxChip = player1Chip;
		}else{
			checkMaxChip = player2Chip;
		}
	}
	
	function drawMaxChip(content){
		p1MaxChipCheck = content.player1Chip;
		p2MaxChipCheck = content.player2Chip;
		player1Chip = content.player1Chip;
		player2Chip = content.player2Chip;
		currentPlayer = content.player;
		drawShowChipText(content);
		chipCount = chipBetting.val();
		console.log("default chipCount: " + chipCount);
		if(content.player == member){
			checkMaxChip = player1Chip;
		}else{
			checkMaxChip = player2Chip;
		}
	}
	
	// 기본 세팅 showChipText
	function showChipText(content){
		chipScore1.text("X"+player1Chip);
		chipScore2.text("X"+player2Chip);
		betchip1Score.text("X"+1);
		betchip2Score.text("X"+1);
	}
	
	//draw 세팅 showChipText
	function drawShowChipText(content){
		chipScore1.text("X"+player1Chip);
		chipScore2.text("X"+player2Chip);
		betchip1Score.text("X"+content.betChip1);
		betchip2Score.text("X"+content.betChip2);
	}
	
	//end game showChipText
	function endGameChipText(content){
		chipScore1.text("X"+player1Chip);
		chipScore2.text("X"+player2Chip);
		betchip1Score.text("X"+0);
		betchip2Score.text("X"+0);
	}
	
	upBtn.click(function(){
		console.log(currentPlayer);
		chipCount = chipBetting.val();
		chipCount++;
		console.log("upBtn chipCount: " + chipCount);
		console.log("CheckMaxChip :" + checkMaxChip);
		if(chipCount>checkMaxChip){
			maxChipValidation();
		}else if(currentPlayer == member){
			player1ChipUp();
			player1BetChipUp();
		}else if(currentPlayer !=member){
			player2ChipUp();
			player2BetChipUp();
		}
		console.log("Result Chip count: " + chipCount);
	});
	
	function maxChipValidation(){
		alert("칩을 " + checkMaxChip +" 초과해서 걸 수 없습니다");
		// player1Chip = player1Chip-chipCount-1;
		chipCount--;
	}
	
	function player1ChipUp(){
		var chip1Max = checkMaxChip; 
		chipBetting.val(chipCount);
		console.log("player1Chip :" + chip1Max);
		chip1Max-=chipCount;
		console.log(chip1Max);
		chipScore1.text("X"+ chip1Max);
	}
	
	function player1BetChipUp(){
		var getBetChip1 = betchip1Score.text().substr(1,2);
		getBetChip1 = Number(getBetChip1)+1;
		console.log("getBetChip1Up" + getBetChip1);
		betchip1Score.text("X"+getBetChip1);
	}
	
	
	function player2ChipUp(){
		var chip2Max = checkMaxChip;
		chipBetting.val(chipCount);
		console.log("player1Chip :" + chip2Max);
		chip2Max -=chipCount;
		console.log(chip2Max);
		chipScore2.text("X"+chip2Max);
	}
	
	function player2BetChipUp(){
		var getBetChip2 = betchip2Score.text().substr(1,2);
		getBetChip2 = Number(getBetChip2)+1;
		console.log("getBetChip1Up" + getBetChip2);
		betchip2Score.text("X"+getBetChip2);
	}
	
	
	downBtn.click(function(){
		chipCount = chipBetting.val();
		if(chipCount <= 0 ){
			minChipValidation();
			return;
		}
		chipCount--;
		console.log("Chip count : " + chipCount);
		if(currentPlayer == member){
			player1DownChip();
			player1BetChipDown();
		}else if(currentPlayer !=member){
			player2DownChip();
			player2BetChipDown();
		}
		console.log("Chip count: " + chipCount);
	});
	
	function minChipValidation(){
		alert("칩을 1개이상 걸어주세요!")
		chipCount = 0;
	}
	
	function player1DownChip(){
		var chip1 = chipScore1.text().substr(1, 2);
		chip1 = Number(chip1)+1;
		chipBetting.val(chipCount);
		chipScore1.text("X"+ chip1);
		console.log("chip1Score: " + chip1);
	}
	
	function player1BetChipDown(){
		var getBetChip1 = betchip1Score.text().substr(1,2);
		getBetChip1 = Number(getBetChip1)-1;
		console.log("getBetChip1Up" + getBetChip1);
		betchip1Score.text("X"+getBetChip1);
	}
	
	function player2DownChip(){
		var chip2 = chipScore2.text().substr(1,2);
		chip2 = Number(chip2)+1;
		chipBetting.val(chipCount);
		chipScore2.text("X"+chip2);
		console.log("chip2Score: " + chip2);	
	}
	
	function player2BetChipDown(){
		var getBetChip2 = betchip2Score.text().substr(1,2);
		getBetChip2 = Number(getBetChip2)-1;
		console.log("getBetChip1Up" + getBetChip2);
		betchip2Score.text("X"+getBetChip2);
	}
	
	allInBtn.click(function(){
		alert("칩을 모두 다 거시겠습니까?");
		if(currentPlayer == member){
			var chip1 = chipScore1.text().substr(1, 2);
			var getBetChip1 = betchip1Score.text().substr(1,2);
			getBetChip1 = Number(getBetChip1)+Number(chip1);
			chipScore1.text("X"+ 0);
			betchip1Score.text("X"+getBetChip1);
		}else if(currentPlayer !=member){
			var chip2 = chipScore2.text().substr(1,2);
			var getBetChip2 = betchip2Score.text().substr(1,2);
			getBetChip2 = Number(getBetChip2)+Number(chip2);
			chipScore2.text("X"+ 0);
			betchip2Score.text("X"+getBetChip2);
		}
		chipBetting.val(checkMaxChip);
		chipCount = checkMaxChip;
	})
	
	function betChipScore(content){
		player1Chip = content.player1Chip;
		player2Chip = content.player2Chip;
		getBetChip1 = content.betChip1;
		getBetChip2 = content.betChip2;
		console.log("player1Chip: " + player1Chip);
		console.log("player2Chip: " + player2Chip);
		console.log("Content Betchip1: " + content.betChip1);
		console.log("Content Betchip2: " + content.betChip2);
		betChipText();
	}
	
	function betChipText(){
		betchip1Score.text("X"+getBetChip1);
		betchip2Score.text("X"+getBetChip2);
		chipScore1.text("X"+player1Chip);
		chipScore2.text("X"+player2Chip);
	}
	
	function cardSelect(content){
		if(content.player == member){
			cardSelect2(content);
		}else{
			cardSelect1(content);
		}
	}
	
	function openCard(content){
		if(currentPlayer == member){
			cardSelect1(content);
		}else{
			cardSelect2(content);
		}
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
		player1Chip = content.player1Chip;
		player2Chip = content.player2Chip;
		chipScore1.text("X"+player1Chip);
		chipScore2.text("X"+player2Chip);
		openCard(content);
		setTimer();
		startTimer();
		timer.show();
		console.log("give up ROUND...?");
	}
	
	function resultRound(content){
		console.log("Resultround!!");
		console.log(content.betChip1);
		console.log(content.betChip2);
		infoArea.eq(0).prepend(content.message + "\n");
		player1Chip = content.player1Chip;
		player2Chip = content.player2Chip;
		chipScore1.text("X"+player1Chip);
		chipScore2.text("X"+player2Chip);
		openCard(content);
		setTimer();
		startTimer();
		timer.show();
		console.log("timer...?");
	}
		
	function bettingAct(content){
		console.log(content.message);
		console.log(content.player);
		console.log("bettingAct betChip1: " + content.betChip1);
		console.log("bettingAct betChip2: " + content.betChip2);
		chipBetting.val(0);
		infoArea.eq(0).prepend(content.message + "\n");
		betChipScore(content);
		maxChipModify(content);
		disableBtn(content);
	}
		
	function maxChipModify(content){
		console.log(content.checkPlayer);
		if(content.checkPlayer == member){
			checkMaxChip = content.player1Chip;
		}else if(content.checkPlayer != member){
			checkMaxChip = content.player2Chip;
		}
		console.log("Modify MaxChip: " + checkMaxChip);
	}
	
	betBtn.click(function(){
		chipCalculatorShow();
	});
	
	// timer
	// timer variables
   
    var timer = $("#timer");
	
    function setTimer(){
    	document.getElementById("timer").innerHTML = `
    	<div class="base-timer">
    	<svg class="base-timer__svg" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
    	<g class="base-timer__circle">
    	<circle class="base-timer__path-elapsed" cx="50" cy="50" r="45"></circle>
    	<path
        id="base-timer-path-remaining"
        stroke-dasharray="283"
        class="base-timer__path-remaining"
        d="
          M 50, 50
          m -45, 0
          a 45,45 0 1,0 90,0
          a 45,45 0 1,0 -90,0
        "
    	></path>
    	</g>
    	</svg>
    	<span id="base-timer-label" class="base-timer__label">
    	 ${formatTimeLeft(timeLeft)}</span>
    	</div>
    	`;
    }
    
    function formatTimeLeft(time) {
 
    	  const minutes = Math.floor(time / 60);
    	  let seconds = time % 60;
    	  if (seconds < 10) {
    	    seconds = `${seconds}`;
    	  }
    	  return `${seconds}`;
    }
    let timeLeft =5;
	
    function startTimer() {
    	let timePassed = 0;
    	let timeLimit = 5;
    	let timeLeft = timeLimit;
    	
    	timerInterval = setInterval(() => {
        timePassed = timePassed += 1;
        timeLeft = timeLimit - timePassed;
    	document.getElementById("base-timer-label").innerHTML = formatTimeLeft(timeLeft);	
    	if(timeLeft == 0){
    		onTimesUp();
    	}
    	if(timeLeft == 0 && drawCheck>0)
    		onDrawTimesUP();
    	}, 1000);
    }
    
    function onTimesUp(){
    	clearInterval(timerInterval);
		timerInterval = null;
		timer.hide();
		endRound();
    }
    
    function onDrawTimesUP(){
    	clearInterval(timerInterval);
		timerInterval = null;
		timer.hide();
		endDrawRound();
    }
   	// timer 종료후 서버로 요청 -> draw는 따로 또 구현해야함
   	function endRound(){
   		console.log(" endRound " );
   		roundSetting();
   		if(currentPlayer == member){
   			console.log("....");
   			var roundData = {type : "ROUND", sender:member, roomId:roomId,
					betChip:betChip,player1Chip:player1Chip,player2Chip:player2Chip,player1BetChip:player1BetChip
					,player2BetChip:player2BetChip};
			sock.send(JSON.stringify(roundData));
   		}
   	}
   	
   	function endDrawRound(){
   		console.log("drawRound" );
   		player1BetChip = parseInt(betchip1Score.text().substr(1));
		player2BetChip = parseInt(betchip2Score.text().substr(1));
		console.log("Draw p2chip " + player2BetChip);
		roundSetting();
   		if(currentPlayer == member){
   			console.log("....");
   			var roundData = {type : "NEXTDRAW", sender:member, roomId:roomId,
					betChip:betChip,player1Chip:player1Chip,player2Chip:player2Chip,player1BetChip:player1BetChip
					,player2BetChip:player2BetChip};
			sock.send(JSON.stringify(roundData));
   		}
   	}
   	
   	function roundSetting(){
		console.log("Next Round");
		cardImg1.attr("src",defaultCardPath+"card"+defaultPng);
		cardImg2.attr("src",defaultCardPath+"card"+defaultPng);
	}
   	
	function betChipMinLimit(){
		console.log("betChip current Player:" + currentPlayer);
		if(currentPlayer == member){
			if(player1BetChip < player2BetChip || player1BetChip == 1){
				return betCheck = false;
			}
		}else if(currentPlayer!= member){
			if(player1BetChip > player2BetChip || player2BetChip == 1){
				return betCheck = false;
			}
		}
	}
	
	function betChipMaxLimit(){
		console.log("betChip current Player:" + currentPlayer);
		var p1MaxChip = player1BetChip+player1Chip;
		var p2MaxChip = player2BetChip+player2Chip;
		if(currentPlayer == member){
			if(player1BetChip > p2MaxChip){
				return betCheck2 = false;
			}
		}else if(currentPlayer != member){
			if(player2BetChip > p1MaxChip){
				return betCheck2 = false;
			}
		}
	}
	
	function oneChipStock(){
		console.log("oneChipStock : "  + player1BetChip + " " + player1Chip);
		// 첫번째 플레이어 일때
		if(currentPlayer == member){	
			if(player2BetChip == 1 && player2Chip == 0){	// 첫번째 플레이어의 칩이 더 많을때
				betCheck = true;
				betCheck2 = true;
				if(player1BetChip>1){
					betCheck3 = false;
				}
			}else if(player1BetChip == 1 && player1Chip ==0){	//	두번째 플레이어의 칩이 더 많을때
				betCheck = true;
				betCheck2 = true;
				if(player2BetChip>1){
					betCheck3 = false;
				}	
			}
		}else if(currentPlayer != member){		//두번째 플레이어 일때
			if(player1BetChip == 1 && player1Chip == 0){	//	두번째 플레이어의 칩이 더 많을때
				betCheck = true;
				betCheck2 = true;
				if(player2BetChip!=1){
					console.log("player2BetChip != 1");
					betCheck3 = false;
				}
			}else if(player2BetChip == 1 && player2Chip == 0){	//첫번째 플레이어의 칩이 더 많을때
				betCheck = true;
				betCheck2 = true;
				if(player1BetChip>1){
					betCheck3 = false;
				}	
			}
		}
	}
	
	function betChipValidation(){
		betChipMinLimit();
		betChipMaxLimit();
		oneChipStock();
		if(betCheck == false){
			alert("상대보다 같거나 많은 칩을 걸어주세요");
		}else if(betCheck2 == false){
			alert("상대보다 많은 칩을 배팅할 수 없습니다");
		}else if(betCheck3 == false){
			alert("상대칩이 1개일떄는 배팅은 한개입니다");
		}
	}

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
		player1BetChip = parseInt(betchip1Score.text().substr(1));
		player2BetChip = parseInt(betchip2Score.text().substr(1));
		betChip = chipBetting.val();
		betCheck = true;
		betCheck2 = true;
		betCheck3 = true;
		console.log("player1BetChip send " +  player1BetChip);
		console.log("player2BetChip send " + player2BetChip);
		console.log("BetChip " + betChip);
		betChipValidation();
		if(betCheck == false || betCheck2 == false || betCheck3 == false){
			return;
		}
		var bettingData = {type : "BETTING", sender:member, roomId:roomId,
				betChip:betChip,player1Chip:player1Chip,player2Chip:player2Chip,player1BetChip:player1BetChip
				,player2BetChip:player2BetChip};
		sock.send(JSON.stringify(bettingData));
		betchip1Score.text("X"+1);
		betchip2Score.text("X"+1);
	});
	
	betGiveUpBtn.click(function(){
		player1BetChip = parseInt(betchip1Score.text().substr(1));
		player2BetChip = parseInt(betchip2Score.text().substr(1));
		console.log("betGiveUp Function ");
		console.log("player1BetChip send " + player1BetChip);
		console.log("player2BetChip send " + player2BetChip);
		var giveUpData = {type:"GIVEUP",sender:member,roomId:roomId,player1Chip:player1Chip,player2Chip:player2Chip,
				player1BetChip:player1BetChip,player2BetChip:player2BetChip};
		sock.send(JSON.stringify(giveUpData));
		console.log("Success Submit betting resultData");
	});
	
});