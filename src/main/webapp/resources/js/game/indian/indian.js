$(function(){
	
	var url = window.location.host; // 웹 브라우저의 주소창의 포트까지 가져옴;
	var sock = new SockJS("http://"+url+"/ws/indian");
	
	var roomId = $('.content').data('room-id');
	var member = $('.content').data('member');
	var defaultCardPath = "/resources/images/indiancards/";
	var defaultPng = ".png";
	
	var player1BetChip;	// 플레이어 1 : 베팅칩
	var player2BetChip;	// 플레이어 2 : 베팅칩
	var player1Chip = 0;	// 플레이어 1 : 보유칩
	var player2Chip = 0;	// 플레이어 2 : 보유칩
	var betChip;	// 현재 건 배팅칩
	var currentPlayer;	// 현재 플레이어
	var drawCheck = 0;	// 무승부 체크
	var p1MaxChipCheck; // 플레이어 1 : Max 칩
	var p2MaxChipCheck;	// 플레이어 2 : Max 칩
	
	var getBetChip1;
	var getBetChip2;
	var checkMaxChip = 0;
	var betMinCheck = true;
	var betMaxCheck = true;
	var betOneChipCheck = true;
	var chipCount =1;
	var reset = 0;
	
	var timer = $("#timer");  // 타이머  
    let timeLeft =5;	//	default 타이머 시간 : 5초
    
	sock.onopen = function(){
		defaultHide();
		var join = {type :'JOIN', sender: member , roomId:roomId};
		sock.send(JSON.stringify(join));
		$("#connectionArea").text("Connecting Server")
	}
	sock.onclose = function(){
		defaultHide();
		$("#connectionArea").text("Server closed")
	}
	
	function defaultHide(){
		$("#betBtn").hide();
		$("#chipAllInBtn").hide();
		$("#betGiveUpBtn").hide();
		$("#chipBetting").hide();
		$("#betSendBtn").hide();
		$("#chipUpBtn").hide();
		$("#chipDownBtn").hide();
	}
	// 서버에서 온 메세지를 받는다
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
			case "GIVEUP" :
				giveUpRound(content);
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
				break;
			default:
				console.log("Default!!");
		}
	}
	
	function inGame(){
		$("#readyBtn").hide();
		$("#betBtn").show();
		$("#betGiveUpBtn").show();
		$("#chipAllInBtn").show();
		$("#chipBetting").show();
		$("#betSendBtn").show();
		$("#chipUpBtn").show();
		$("#chipDownBtn").show();
	}
	
	/* chat */
	function chat(content){
		$("#chatArea").eq(0).prepend(content.sender + " : " + content.message+ " " + "\n");
	}
	
	/* Player */
	function ready(content){
		reset = 0;
		console.log(content.message);
		console.log(content.player);
		if(content.message.substring(0,4) == '플레이어'){
			$("#infoArea").eq(0).prepend(content.message + "\n");
			$("#infoArea").eq(0).prepend(content.firstTurn + "\n");
			inGame();
			disableBtn(content);
		}else{
			$("#infoArea").eq(0).prepend(content.message + "\n");
		}
	}
	
	function leavePlayer(content){
		if(content.player == $("#playerId1").val()){
			$("#playerId1").val('');
		}else{
			$("#playerId2").val('');
		}
		$("#infoArea").eq(0).prepend(content.message + "\n");
	}
	
	function loadPlayer(content){
		switch (content.message) {
		case "PLAYER":
			joinPlayer(content);
			break;
		}
	}
	
	function joinPlayer(content){
		if(!$("#playerId1").val()){
			$("#playerId1").val(content.player);
		}else if(!$("#playerId2").val()){
			$("#playerId2").val(content.player);
		}
	}	
	
	// 플레이어가 방을 나갈때 게임을 종료
	function stopGame(content){
		$("#infoArea").eq(0).prepend(content.message + "\n");
		$("#infoArea").eq(0).prepend(content.winner + "\n");
		closeCard();
		disableAll();
		defaultHide();
		alert("방을 나가 주세요!");
	}
	
	function endGame(content){
		openCard(content);
		$("#infoArea").eq(0).prepend(content.message + "\n");
		$("#infoArea").eq(0).prepend(content.winner + "\n");
		$("#chipScore1").text("X"+content.player1Chip);
		$("#chipScore2").text("X"+content.player2Chip);
		$("#betchip1Score").text("X"+0);
		$("#betchip2Score").text("X"+0);
		alert("게임이 종료 되었습니다. 5초후 게임을 다시 시작할 수 있습니다.");
		reset++;
		defaultTimer();
		$("#readyBtn").show();
	}	
	/* btn disable */
	function disableBtn(content){
		 var turnPlayer = content.firstTurn.substring(0,content.firstTurn.indexOf("님"));
		 if(turnPlayer != member){
			disableAll();
		}else if(turnPlayer == member){
			enableAll();
		}
	};
	
	function disableAll(){
		$("#betSendBtn").prop("disabled",true);
		$("#betBtn").prop("disabled",true);
		$("#chipAllInBtn").prop("disabled",true);
		$("#betGiveUpBtn").prop("disabled",true);
		$("#chipUpBtn").prop("disabled",true);
		$("#chipDownBtn").prop("disabled",true);
		console.log("Button Disable All");
	}
	
	function enableAll(){
		$("#betBtn").prop("disabled",false);
		$("#chipAllInBtn").prop("disabled",false);
		$("#betGiveUpBtn").prop("disabled",false);
		$("#betSendBtn").prop("disabled",false);
		$("#chipUpBtn").prop("disabled",false);
		$("#chipDownBtn").prop("disabled",false);
		console.log("Button Able All");
	}
	
	/* Round */
	function startRound(content){
		console.log("startRound");
		$("#chip1").show();
		$("#chip2").show();
		playerMaxChip(content);
		cardSelect(content);
	}
	
	function nextRound(content){
		closeCard();
		var message = content.message;
		var turnPlayer = message.substring(0,message.indexOf("님"));
		$("#infoArea").eq(0).prepend(message + "\n");
		playerMaxChip(content);
		cardSelect(content);
		playerAbleBtn(content);
	}
	
	function nextDrawRound(content){
		drawCheck = 0;
		closeCard();
		var message = content.message;
		$("#infoArea").eq(0).prepend(message + "\n");
		drawMaxChip(content);
		cardSelect(content);
		playerAbleBtn(content);
	}
	
	function playerAbleBtn(content){
		var message = content.message;
		var turnPlayer = message.substring(0,message.indexOf("님"));
		if(turnPlayer != member){
			disableAll();
		}else if(turnPlayer == member){
			enableAll();
		}
	}
	
	function giveUpRound(content){
		$("#infoArea").eq(0).prepend(content.chipMessage + "\n");
		$("#infoArea").eq(0).prepend(content.winner + "\n");
		chipAndPlayer(content)
		openCard(content);
		defaultTimer();
		console.log("Give Up Round");
	}
	
	function resultRound(content){
		$("#infoArea").eq(0).prepend(content.message + "\n");
		chipAndPlayer(content)
		openCard(content);
		defaultTimer();
		console.log("Result Round");
	}	
	function chipAndPlayer(content){
		player1Chip = content.player1Chip;
		player2Chip = content.player2Chip;
		$("#chipScore1").text("X"+player1Chip);
		$("#chipScore2").text("X"+player2Chip);
	}
	
	function drawMaxChip(content){
		playerAndMaxChipModify(content);
		player1Chip = content.player1Chip;
		player2Chip = content.player2Chip;
		drawShowChipText(content);
		chipCount = $("#chipBetting").val();
		if(content.player == member){
			checkMaxChip = player1Chip;
		}else{
			checkMaxChip = player2Chip;
		}
	}
	
	function playerMaxChip(content){
		playerAndMaxChipModify(content);
		player1Chip = content.player1Chip-1;
		player2Chip = content.player2Chip-1;
		showChipText(content);
		chipCount = $("#chipBetting").val();
		if(content.player == member){
			checkMaxChip = player1Chip;
		}else{
			checkMaxChip = player2Chip;
		}
	}
	
	function playerAndMaxChipModify(content){
		p1MaxChipCheck = content.player1Chip;
		p2MaxChipCheck = content.player2Chip;
		currentPlayer = content.player;
	}
	
	function showChipText(content){
		console.log(content.type);
		$("#chipScore1").text("X"+player1Chip);
		$("#chipScore2").text("X"+player2Chip);
		$("#betchip1Score").text("X"+1);
		$("#betchip2Score").text("X"+1);
	}
	
	function drawShowChipText(content){
		console.log(content.type);
		$("#chipScore1").text("X"+player1Chip);
		$("#chipScore2").text("X"+player2Chip);
		$("#betchip1Score").text("X"+content.betChip1);
		$("#betchip2Score").text("X"+content.betChip2);
	}

	function draw(content){
		console.log("Draw");
		drawCheck++;
		$("#infoArea").eq(0).prepend(content.message + "\n");
		betChipScore(content)
		openCard(content);
		setTimer();
		startTimer();
		timer.show();
	}
	
	// 프론트에서 플레이어의 칩을 조절해 주기 위해 사용한다
 	$("#chipUpBtn").click(function(){	//ChipUp
		chipCount = $("#chipBetting").val();
		chipCount++;
		if(chipCount>checkMaxChip){
			alert("칩을 " + checkMaxChip +" 초과해서 걸 수 없습니다");
			chipCount--;
		}else if(currentPlayer == member){
			player1ChipUp();
			player1BetChipUp();
		}else if(currentPlayer !=member){
			player2ChipUp();
			player2BetChipUp();
		}
		console.log("Result Chip count: " + chipCount);
	});
	
	$("#chipAllInBtn").click(function(){	// All In
		alert("칩을 모두 다 거시겠습니까?");
		if(currentPlayer == member){
			var chip1 = $("#chipScore1").text().substr(1, 2);
			var getBetChip1 = $("#betchip1Score").text().substr(1,2);
			getBetChip1 = Number(getBetChip1)+Number(chip1);
			$("#chipScore1").text("X"+ 0);
			$("#betchip1Score").text("X"+getBetChip1);
		}else if(currentPlayer !=member){
			var chip2 = $("#chipScore2").text().substr(1,2);
			var getBetChip2 = $("#betchip2Score").text().substr(1,2);
			getBetChip2 = Number(getBetChip2)+Number(chip2);
			$("#chipScore2").text("X"+ 0);
			$("#betchip2Score").text("X"+getBetChip2);
		}
		$("#chipBetting").val(checkMaxChip);
		chipCount = checkMaxChip;
	})
	
	$("#chipDownBtn").click(function(){	// ChipDown
		chipCount = $("#chipBetting").val();
		if(chipCount <= 0 ){
			alert("칩을 1개이상 걸어주세요!")
			chipCount = 0;
			return;
		}
		chipCount--;
		if(currentPlayer == member){
			player1DownChip();
			player1BetChipDown();
		}else if(currentPlayer !=member){
			player2DownChip();
			player2BetChipDown();
		}
		console.log("Chip count: " + chipCount);
	});
	
	/* Up Chip */
	function player1ChipUp(){	// 플레이어 1: 보유칩 증가
		var chip1Max = checkMaxChip; 
		$("#chipBetting").val(chipCount);
		chip1Max-=chipCount;
		$("#chipScore1").text("X"+ chip1Max);
	}
	
	function player1BetChipUp(){	// 플레이어 1: 배팅칩 증가
		var getBetChip1 = $("#betchip1Score").text().substr(1,2);
		getBetChip1 = Number(getBetChip1)+1;
		$("#betchip1Score").text("X"+getBetChip1);
	}
	
	function player2ChipUp(){	// 플레이어 2: 보유칩 증가
		var chip2Max = checkMaxChip;
		$("#chipBetting").val(chipCount);
		chip2Max -=chipCount;
		$("#chipScore2").text("X"+chip2Max);
	}
	
	function player2BetChipUp(){	// 플레이어 2: 배팅칩 증가
		var getBetChip2 = $("#betchip2Score").text().substr(1,2);
		getBetChip2 = Number(getBetChip2)+1;
		$("#betchip2Score").text("X"+getBetChip2);
	}
	
	/* Down Chip */
	function player1DownChip(){	// 플레이어 1 : 보유칩 감소
		var chip1 = $("#chipScore1").text().substr(1, 2);
		chip1 = Number(chip1)+1;
		$("#chipBetting").val(chipCount);
		$("#chipScore1").text("X"+ chip1);
	}
	
	function player2DownChip(){	// 플레이어 2: 보유칩 감소
		var chip2 = $("#chipScore2").text().substr(1,2);
		chip2 = Number(chip2)+1;
		$("#chipBetting").val(chipCount);
		$("#chipScore2").text("X"+chip2);
	}
	
	function player1BetChipDown(){	//	플레이어 1 : 배팅칩 감소
		var getBetChip1 = $("#betchip1Score").text().substr(1,2);
		getBetChip1 = Number(getBetChip1)-1;
		$("#betchip1Score").text("X"+getBetChip1);
	}
	
	function player2BetChipDown(){	// 플레이어 2: 배팅칩 감소
		var getBetChip2 = $("#betchip2Score").text().substr(1,2);
		getBetChip2 = Number(getBetChip2)-1;
		$("#betchip2Score").text("X"+getBetChip2);
	}
	function betChipScore(content){
		player1Chip = content.player1Chip;
		player2Chip = content.player2Chip;
		getBetChip1 = content.betChip1;
		getBetChip2 = content.betChip2;
		betChipText();
	}
	
	function betChipText(){
		$("#betchip1Score").text("X"+getBetChip1);
		$("#betchip2Score").text("X"+getBetChip2);
		$("#chipScore1").text("X"+player1Chip);
		$("#chipScore2").text("X"+player2Chip);
	}
	
	// 칩 배팅시 최소,최대 조건으로 배팅칩에 제한을 건다
    function betChipMinLimit(){
		if(currentPlayer == member){	// 첫번째 플레이어 차례
			if(player1BetChip < player2BetChip || player1BetChip == 1){
				if(player2BetChip == 1 && player2Chip == 0){
					if(player1BetChip>1){	// 남은 칩이 1개라면 배팅 칩 1개를 허용한다.
						return betOneChipCheck = false;
					}
				}else if(player1BetChip == 1 && player1Chip ==0){
					if(player2BetChip>1){
						return betOneChipCheck = false;
					}	
				}else{
					return betMinCheck = false;
				}
			}
		}else if(currentPlayer!= member){	// 두번째 플레이어 차례
			if(player1BetChip > player2BetChip || player2BetChip == 1){
				if(player2BetChip == 1 && player2Chip == 0){
					if(player1BetChip>1){	// 남은 칩이 1개라면 배팅 칩 1개를 허용한다.
						return betOneChipCheck = false;
					}
				}else if(player1BetChip == 1 && player1Chip ==0){
					if(player2BetChip>1){
						return betOneChipCheck = false;
					}	
				}else{
					return betMinCheck = false;
				}
			}
		}
	}
    function betChipMaxLimit(){	// 배팅칩의 최대를 검사한다
		var p1MaxChip = player1BetChip+player1Chip;
		var p2MaxChip = player2BetChip+player2Chip;
		if(currentPlayer == member){
			if(player1BetChip > p2MaxChip){
				return betMaxCheck = false;
			}
		}else if(currentPlayer != member){
			if(player2BetChip > p1MaxChip){
				return betMaxCheck = false;
			}
		}
	}
    
	function maxChipModify(content){	// 배팅칩의 최대수를 변경한다
		if(content.checkPlayer == member){
			checkMaxChip = content.player1Chip;
		}else if(content.checkPlayer != member){
			checkMaxChip = content.player2Chip;
		}
	}
	
    function betChipValidation(){	// 배팅칩에 제한을 걸기위한 유효성 검사
    	betMinCheck = true; betMaxCheck = true; betOneChipCheck = true;
		betChipMinLimit();
		betChipMaxLimit();
		if(betMinCheck == false){
			alert("상대보다 같거나 많은 칩을 걸어주세요");
		}else if(betMaxCheck == false){
			alert("상대보다 많은 칩을 배팅할 수 없습니다");
		}else if(betOneChipCheck == false){
			alert("상대칩이 1개일떄는 배팅은 한개입니다");
		}
	}
	
	// 카드 선택,오픈,닫기
	function cardSelect(content){
		if(member == $("#playerId1").val()){
			$("#card2").attr("src",defaultCardPath+"card"+content.card2+defaultPng);
		}else if(member == $("#playerId2").val()){
			$("#card1").attr("src",defaultCardPath+"card"+content.card1+defaultPng);
		}
	}
		
	function openCard(content){
		if(currentPlayer == member){
			$("#card1").attr("src",defaultCardPath+"card"+content.card1+defaultPng);
		}else{
			$("#card2").attr("src",defaultCardPath+"card"+content.card2+defaultPng);
		}
	}
	
 	function closeCard(){
 		$("#card1").attr("src",defaultCardPath+"card"+defaultPng);
 		$("#card2").attr("src",defaultCardPath+"card"+defaultPng);
 	}
		
	function bettingAct(content){
		$("#chipBetting").val(0);
		$("#infoArea").eq(0).prepend(content.message + "\n");
		betChipScore(content);
		maxChipModify(content);
		disableBtn(content);
	}
	
	// 타이머 설정
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
    
	function defaultTimer(){
		setTimer();
		startTimer();
		disableAll();
		timer.show();
	}
 
    function onTimesUp(){
    	clearInterval(timerInterval);
		timerInterval = null;
		timer.hide();
    }  
    
    function startTimer() {
    	let timePassed = 0;
    	let timeLimit = 5;
    	let timeLeft = timeLimit;
    	
    	timerInterval = setInterval(() => {
        timePassed = timePassed += 1;
        timeLeft = timeLimit - timePassed;
    	document.getElementById("base-timer-label").innerHTML = formatTimeLeft(timeLeft);	
    	if(timeLeft == 0 && reset == 0){
    		onTimesUp();
    		endRound();
    		}
    	else if(timeLeft == 0 && reset > 0 ){
    		onTimesUp();
    		closeCard();
    	}
    	}, 1000);
    }
    
    function endRound(){	
   		if(currentPlayer == member && drawCheck > 0){
   			playerGetBetChip();
   			var roundData = {type : "NEXTDRAW", sender:member, roomId:roomId,
					betChip:betChip,player1Chip:player1Chip,player2Chip:player2Chip,player1BetChip:player1BetChip
					,player2BetChip:player2BetChip};
   			sock.send(JSON.stringify(roundData));
   		}else if(currentPlayer == member){
   			var roundData = {type : "ROUND", sender:member, roomId:roomId,
   					betChip:betChip,player1Chip:player1Chip,player2Chip:player2Chip,player1BetChip:player1BetChip
   					,player2BetChip:player2BetChip};
   			sock.send(JSON.stringify(roundData));
   		}
   	}
    
	function playerGetBetChip(){
		player1BetChip = parseInt($("#betchip1Score").text().substr(1));
		player2BetChip = parseInt($("#betchip2Score").text().substr(1));
	}
	
	$("#leave").click(function(){
		location.href='../../rooms'
	});
	
	$("#message").keydown(function(key) {
		var message = $("#message").val();
        if (key.keyCode == 13) {
        	var chatData = {type :'TALK',sender:member,roomId:roomId,message:message};
    		sock.send(JSON.stringify(chatData));
            $("#message").val('');
        }
    });

	$("#sendBtn").click(function(){
		var message = $("#message").val();
		var chatData = {type :'TALK',sender:member,roomId:roomId,message:message};
		sock.send(JSON.stringify(chatData));
	});
	
	$("#readyBtn").click(function(){
		var readyData = {type : "READY", sender : member, roomId : roomId};
		sock.send(JSON.stringify(readyData));
	});
	
	$("#betSendBtn").click(function(){
		playerGetBetChip();
		betChipValidation();
		betChip = $("#chipBetting").val();
		if(betMinCheck == false || betMaxCheck == false || betOneChipCheck == false){return;}
		var bettingData = {type : "BETTING", sender:member, roomId:roomId,
				betChip:betChip,player1Chip:player1Chip,player2Chip:player2Chip,player1BetChip:player1BetChip
				,player2BetChip:player2BetChip};
		sock.send(JSON.stringify(bettingData));
	});
	
	$("#betGiveUpBtn").click(function(){
		$("#chipBetting").val(0);
		playerGetBetChip();
		var giveUpData = {type:"GIVEUP",sender:member,roomId:roomId,player1Chip:player1Chip,player2Chip:player2Chip,
				player1BetChip:player1BetChip,player2BetChip:player2BetChip};
		sock.send(JSON.stringify(giveUpData));
	});
	
});