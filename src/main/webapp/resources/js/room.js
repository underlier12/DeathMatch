$(function () {
	
	// variables
	
	var chatStatus = $('#chatStatus');
	var chatMsgArea = $('textarea[name="chatMsg"]');
    var messageInput = $('input[name="message"]');
    var sendBtn = $('.send');
    var roomId = $('.content').data('room-id');
    var member = $('.content').data('member');
    
    var answerList = $('.answer');
    var selectedInput = $('#selected');
   
    
    var url = window.location.host;//웹브라우저의 주소창의 포트까지 가져옴
    var pathname = window.location.pathname; /* '/'부터 오른쪽에 있는 모든 경로*/
    var appCtx = pathname.substring(0, pathname.indexOf("/",2));
    var root = url+appCtx;

    var selectBtn = $('.select');
    var uniBtn = $('.uni');
    var onBtn = $('.on');
    var readyBtn = $('.ready');
    
    var playerAInput = $('#playerA');
    var playerBInput = $('#playerB');
    var scoreAInput = $('.scoreA');
    var scoreBInput = $('.scoreB');
    
    var roundP = $('#round');

    var sock = new SockJS("http://"+root+"/ws/chat");
    
    
    // websocket actions
    
    sock.onopen = function () {
        sock.send(JSON.stringify({type: 'JOIN', roomId: roomId, sender: member}));
        chatStatus.text('Info: connection opened.');
    }
    
    sock.onmessage = function (event){
    	var content = JSON.parse(event.data);
    	
    	console.log("content.sender : " + content.sender);
    	
    	switch(content.sender){
    	case "Setting":
    	case "Dealer":
    		fromServer(content);
    		break;
    	default:
    		fromUser(content);
    	}
    }
    
 	sock.onclose = function(event){
 		console.log("sock.onclose");
 		chatStatus.text('Info: connection closed.');
 	}

 	// websocket functions
 	
 	function fromServer(content){
 		switch(content.type){
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
 		case "END":
 			notifyEnd(content);
 			break;
 		default:
 			console.log("fromServer default");
 		}
 	}
 	
 	function fromUser(content){
 		switch(content.type){
 		case "UNI":
 			submitUni(content);
 			break;
 		case "ON":
 			submitOn(content);
 			break;
 		default:
 			console.log("fromUser default");
 		}
 	}
 	
 	function notifyJoin(content){
		chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 	}
 	
 	function notifyReady(content){
		chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
		
		if(content.user1){
			playerAInput.val(content.user1);
			playerBInput.val(content.user2);
			
			$('.scoreA').val(0);
			$('.scoreB').val(0);
		}
 	}

 	function notifyRound(content){
 		chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');	 			
        roundP.text(content.round + ' ROUND');
 	}
 	
 	function notifyProblem(content){
		var defaultPath = "/genious/resources/images/";
		var defaultExtension = ".jpg";
		
		for(var i=0; i < content.cards.length; i++){
			$(".card:eq("+i+")").attr("src", defaultPath + content.cards[i] + defaultExtension);
		}
 	}
 	
 	function notifyUni(content){
 		chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');	
			
		addUp(content);
 	}
 	
 	function notifyOn(content){
 		chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 		
 		addUp(content);
 		
 		console.log(content.answer);
		
		if(parseInt(content.score) > 0){
        	answerList.append('<li>' + content.answer + '</li>');
		}
 	}
 	
 	function notifyEnd(content){
 		chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 		
 		switch(content.message.substring(0, 4)){
		case "데스매치":
 			announceWinner(content);
 			break;
 		default:
 			resetAnswerList();
 			break;
 		}
 	}
 	
 	function addUp(content){
 		var existingScore;
		var score = parseInt(content.score);
 		
 		if(content.user1 == playerAInput.val()){
			existingScore = parseInt(scoreAInput.val()); 
			scoreAInput.val(existingScore + score);
		}else{
			existingScore = parseInt(scoreBInput.val()); 
			scoreBInput.val(existingScore + score);
		}
 	}
 	
 	function announceWinner(content){
 		if(scoreAInput.val() > scoreBInput.val()){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' 
 					+ '승자는 ' + playerAInput.val() + '입니다. 축하합니다.\n');
 		} else if(scoreAInput.val() < scoreBInput.val()){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' 
 					+ '승자는 ' + playerBInput.val() + '입니다. 축하합니다.\n');
 		} else{
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' 
 					+ '결과는 무승부입니다.\n');
 		}
 	}
 	
 	function resetAnswerList(){
 		answerList.empty();
 	}
 	
 	function submitUni(content){
 		chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 	}
 	
 	function submitOn(content){
 		chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 	}
 	
 	
 	// button actions
	
    sendBtn.click(function () {
        var message = messageInput.val();
        sock.send(JSON.stringify(
        		{type: 'TALK', roomId: roomId, sender: member, message: message}));
        messageInput.val('');
    });
    
    selectBtn.click(function(){
    	var selectedCard = $(this).attr('name');
    	var selectedBefore = selectedInput.val();
    	selectedInput.val(selectedBefore + selectedCard);
    });
    
    uniBtn.click(function(){
    	sock.send(JSON.stringify(
    			{type: 'UNI', roomId: roomId, sender: member, message: "결!"}));
    });
    
    onBtn.click(function(){
    	
    	var answerString = selectedInput.val();
    	var answerArray = answerString.split("");
    	var sortedAnswerArray = answerArray.sort();
    	var message = sortedAnswerArray.join('');
    	
    	sock.send(JSON.stringify(
    			{type: 'ON', roomId: roomId, sender: member, message: message}));
    	
    	selectedInput.val('');
    });
    
    readyBtn.click(function(){
    	sock.send(JSON.stringify(
    			{type: 'READY', roomId: roomId, sender: member}));
    });
    
});