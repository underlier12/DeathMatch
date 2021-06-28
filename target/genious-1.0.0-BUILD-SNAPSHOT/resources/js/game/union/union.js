$(function () {
// websocket variables
	
	var url = window.location.host;//웹브라우저의 주소창의 포트까지 가져옴
	var sock = new SockJS("http://"+url+"/ws/chat");	
	
// variables
	
	// request variables
	var roomId = $('.content').data('room-id');
	var member = $('.content').data('member');
	
	// resources variables
	var defaultCardPath = "/resources/images/cards/";
	var defaultJpg = ".jpg";
	var defaultPng = ".png";
	
// timer variables
    
    const FULL_DASH_ARRAY = 283; // 2*pi*r
    let timerInterval = null;
    var timer;
    
    
// websocket actions
    
    sock.onopen = function () {
        sock.send(JSON.stringify({type: 'JOIN', roomId: roomId, sender: member}));
        $('#connectionStatus').text('connection opened');
        notInGame();
        disableAll();
    }
    
    sock.onmessage = function (event){
    	var content = JSON.parse(event.data);
    	    	
    	switch(content.sender){
    	case "Setting":
    	case "Dealer":
    	case "Loader":
    		fromServer(content);
    		break;
    	default:
    		fromUser(content);
    	}
    }
    
 	sock.onclose = function(event){
 		console.log("sock.onclose");
 		$('#connectionStatus').text('connection closed');
 	}

// websocket functions
 	
 	function fromServer(content){ 	
 		
 		if(content.message){
 			$('#broadcast').eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 		}
 		
 		switch(content.type){
 		case "LOAD":
 			notifyLoad(content);
 			break;
 		case "JOIN":
 			notifyJoin(content);
 			break;
 		case "READY":
 			notifyReady(content);
 			break;
 		case "ROUND":
 			notifyRound(content);
 			break;
 		case "PROBLEM":
 			notifyProblem(content);
 			break;
 		case "UNI":
 			notifyUni(content);
 			break;
 		case "ON":
 			notifyOn(content);
 			break;
 		case "TURN":
 			notifyTurn(content);
 			break;
 		case "END":
 			notifyEnd(content);
 			break;
 		case "LEAVE":
 			notifyLeave(content);
 			break;
 		default:
 			console.log("fromServer default");
 		}
 	}
 	
 	function fromUser(content){
 		 		
 		switch(content.type){
 		case "UNI":
 		case "ON":
 			receiveExclaim(content);
 			break;
 		case "TALK":
 			receiveTalk(content);
 			break;
 		default:
 			console.log("fromUser default");
 		}
 	}
 	
 	function notifyLoad(content){
 		switch (content.message) {
		case "PLAYER":
			if(!$('#playerA').val()){
				$('#playerA').val(content.user);
			}else if(!$('#playerB').val()){
				$('#playerB').val(content.user);
			}
			break;
			
		case "SCORE":
			if($('#playerA').val() == content.user){
				$('#scoreA').val(content.score);
			} else {
				$('#scoreB').val(content.score);
			}
			break;
			
		case "PROBLEM":
			console.log("problem loading");
			console.log("length : " + content.set.length);
			for(var i=0; i < content.set.length; i++){
				$(".card:eq("+i+")").attr("src", defaultCardPath + content.set[i] + defaultJpg);
			}
			break;
			
		case "ANSWER":
			console.log("answer loading");
			console.log("length : " + content.set.length);
			for(var i=0; i < content.set.length; i++){
	        	$('#answer').append('<li>' + content.set[i] + '</li>');
			}
		}
 	}
 	
 	function notifyJoin(content){		
		if(!$('#playerA').val()){
			$('#playerA').val(content.user1);
		}else if(!$('#playerB').val()){
			$('#playerB').val(content.user1);
		}else{
			inGame();
			disableAll();
		}
		toastMessage("info", content.message);
 	}
 	
 	function notifyReady(content){
		if(content.message.substring(0, 3) == '참가자'){
			$('#scoreA').val(0);
			$('#scoreB').val(0);
			inGame();
		}
		toastMessage("info", content.message);
 	}

 	function notifyRound(content){
        $('#round').text(content.round + ' ROUND');
		countDown(content);
		$('#pass').html('');
 	}
 	
 	function notifyProblem(content){
		for(var i=0; i < content.cards.length; i++){
			$(".card:eq("+i+")").attr("src", defaultCardPath + content.cards[i] + defaultJpg);
		}
 	}
 	
 	function notifyUni(content){
		addUp(content);
 	}
 	
 	function notifyOn(content){
 		if(!(content.message == "합!")){
 			addUp(content);
 		}
		
		if(parseInt(content.score) > 0){
        	$('#answer').append('<li>' + content.answer + '</li>');
		}
 	}
 	
 	function notifyTurn(content){
		countDown(content);		
		isMyTurn(content);
 	}
 	
 	function notifyEnd(content){
 		onTimesUp();
 		
 		switch(content.message.substring(0, 4)){
		case "데스매치":
 			announceWinner(content);
 		default:
 			resetRound();
 			break;
 		}
 		toastMessage("info", content.message);
 	}
 	
 	function notifyLeave(content){
 		if(content.user1 == $('#playerA').val()){
 			$('#playerA').val('');
 		} else {
 			$('#playerB').val('');
 		}
 		toastMessage("info", content.message);
 	}
 	
 	function addUp(content){
 		var existingScore;
		var score = parseInt(content.score);
 		
 		if(content.user1 == $('#playerA').val()){
			existingScore = parseInt($('#scoreA').val()); 
			$('#scoreA').val(existingScore + score);
		}else{
			existingScore = parseInt($('#scoreB').val()); 
			$('#scoreB').val(existingScore + score);
		}
 		
 		if(content.score > 0){
 			toastMessage("success", content.message);
 		} else {
 			toastMessage("warning", content.message);
 		}
 	}
 	
 	function announceWinner(content){
 		var message;
 		if(content.user1 == "무승부"){
 			message = '결과는 무승부입니다.\n';
 		} else {
 			message = '승자는 ' + content.user1 + '입니다. 축하합니다.\n';
 		}
 		$('#broadcast').eq(0).prepend(content.sender + ' : ' + message);
 		toastMessage("info", message);
 		
 		notInGame();
 		disableAll();
 		resetRound();
 		resetScore();
 		$('#pass').html('');
 	}
 	
 	function countDown(content){
 		if(timerInterval){
 			onTimesUp();
 		}
 		switch (content.user1) {
		case $('#playerA').val():
			timer = "timerA";
			setTimer(content.countDown);
			break;
		case $('#playerB').val():
			timer = "timerB";
			setTimer(content.countDown);
			break;
		}
 	 	startTimer(content);
 	}
 	
 	function resetRound(){
 		$('#round').text('');
 		$('#answer').empty();
 	}
 	
 	function resetScore(){
 		setTimeout(function(){
 			$('#scoreA').val('');
 			$('#scoreB').val(''); 			
 		}, 5000);
 	}
 	
 	function receiveExclaim(content){
 		$('#broadcast').eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 		exclaim(content);
 	}
 	
 	function receiveTalk(content){
 		$('#talk').eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 	}
 	
 	function exclaim(content){
 		if(content.sender == $('#playerA').val()){
 			$('#exclaimA').show();
 	    	$('#exclaimB').hide();
 			$('#statementA').text(content.message); 			
 		}else{
 			$('#exclaimA').hide();
 	    	$('#exclaimB').show();
 			$('#statementB').text(content.message);
 		} 		
 	}
 	
// timer

 	function setTimer(time){
 		document.getElementById(timer).innerHTML = `
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
 			
 			<span id="base-timer-label" class="base-timer__label">${time}</span>
 			</div>
 			`;
 	}

 	function onTimesUp() {
 	  clearInterval(timerInterval);
 	  timerInterval = null;
 	  document.getElementById(timer).innerHTML = "";
 	  
 	  disableAll()
 	}

 	function startTimer(content) {
 	 	let timePassed = 0;
 	 	let timeLimit = content.countDown;
 	 	let timeLeft = timeLimit;
 	 	
 	 	timerInterval = setInterval(() => {
 	    timePassed = timePassed += 1;
 	    timeLeft = timeLimit - timePassed;
 	    document.getElementById("base-timer-label").innerHTML = timeLeft;
 	    setCircleDasharray(timeLimit, timeLeft);

 	    if(timeLeft <= 0){
 	    	timeUp(timeLeft, content); 	    	
 	    }
 	    
 	 	}, 1000);
 	}

 	function calculateTimeFraction(timeLimit, timeLeft) {
 	  const rawTimeFraction = timeLeft / timeLimit;
 	  return rawTimeFraction - (1 / timeLimit) * (1 - rawTimeFraction);
 	}

 	function setCircleDasharray(timeLimit, timeLeft) {
 	  const circleDasharray = `${(
 	    calculateTimeFraction(timeLimit, timeLeft) * FULL_DASH_ARRAY
 	  ).toFixed(0)} 283`;
 	  document
 	    .getElementById("base-timer-path-remaining")
 	    .setAttribute("stroke-dasharray", circleDasharray);
 	}
 	
 	function timeUp(timeLeft, content){
		onTimesUp();
		
		if(content.user1 == member){
			console.log("content.message, timeup : " + content.message);
			var pass = 1;
			
			if(content.type == 'ROUND' || content.message.substring(0, 1) == "결"){
				pass = 0;
			}
			
			sock.send(JSON.stringify(
					{type: 'TIMEUP', roomId: roomId, sender: member, message: "TIMEUP",
						pass: pass}));
		}
 	}
 	
// button/checkbox actions
 	
    $('#uni').click(function(){
    	sock.send(JSON.stringify(
    			{type: 'UNI', roomId: roomId, sender: member, message: "결!"}));
    });
    
    $('#on').click(function(){
    	sock.send(JSON.stringify(
    			{type: 'ON', roomId: roomId, sender: member, message: "합!"}));
    });
    
    $('#ready').click(function(){
    	sock.send(JSON.stringify(
    			{type: 'READY', roomId: roomId, sender: member, message: "READY"}));
    });
    
    $("#sendBtn").click(function(){
		sock.send(JSON.stringify(
				{type : 'TALK', roomId: roomId, sender: member, message: $("#message").val()}));
		$("#message").val('');
	});
    
    $("#message").keydown(function(key) {
    	if(key.keyCode == 13){
    		sock.send(JSON.stringify(
    				{type : 'TALK', roomId: roomId, sender: member, message: $("#message").val()}));
    		$("#message").val('');
    	}
    });
    
    $('#leave').click(function(){
    	location.href='../../rooms';
    });
    
    $('.selectbox').change(function(){	
    	$(this).next().children().toggleClass("boundary");
    	var $checkedBox = $('input[type="checkbox"]:checked');
    	
    	if($checkedBox.length == 3){
    		var message = "";
    		
    		$checkedBox.each(function(){
    			var selected = $(this).attr("name");
    			message += selected;
    		})
    		    		
    		sock.send(JSON.stringify(
        			{type: 'ON', roomId: roomId, sender: member, message: message}));
    		
    		$checkedBox.each(function(){
    			$(this).prop("checked", false);
    			$(this).next().children().toggleClass("boundary");
    		})
    	}
    });
    
// setting functions
    
    function inGame(){
    	$('#ready').hide();
    	$('#uni').show();
    	$('#on').show();
    }
    
    function notInGame(){
    	$('#uni').hide();
    	$('#on').hide();
    	$('#exclaimA').hide();
    	$('#exclaimB').hide();
    	$('#ready').show();
    	for(var i=0; i < 9; i++){
    		$(".card:eq("+i+")").attr("src", defaultCardPath + (i+1) + defaultPng);
		}
    }
    
    function isMyTurn(content){
    	console.log(" ");
    	console.log("isMyTurn : " + content.message + " " + content.user1);

    	var passText = "PASS<br>";
    	var passTotal = passText.repeat(content.pass);
    	$('#pass').html(passTotal);
    	
    	if(content.user1 == member){
    		switch (content.message.substring(0, 1)) {
			case "결":
				enableUni();
				break;
				
			case "합":
				enableProblem();
				break;

			default:
				enableUni();
				enableOn();
				break;
			}
    	}
    }
    
    function enableProblem(){
    	console.log("enProb");
    	for(var i=0; i < 9; i++){
    		$(".selectbox:eq("+i+")").prop("disabled", false);
    	}
    }
    
    function enableUni(){
    	console.log("enUni");
    	$('#uni').prop("disabled", false);
    }
    
    function enableOn(){
    	console.log("enOn");
    	$('#on').prop("disabled", false);
    }
    
    function disableAll(){
    	console.log("disAll");
    	for(var i=0; i < 9; i++){
    		$(".selectbox:eq("+i+")").prop("disabled", true);
    	}
    	$('#uni').prop('disabled', true);
    	$('#on').prop('disabled', true);
    }
    
    function toastMessage(type, message){
    	toastr.options = {
    			  "closeButton": true,
    			  "progressBar": true,
    			  "positionClass": "toast-top-center",
    			  "showDuration": "300",
    			  "hideDuration": "1000",
    			  "timeOut": "3000",
    			  "showMethod": "fadeIn",
    			  "hideMethod": "fadeOut"
    			}
    	toastr[type](message);
    }
    
});