$(function () {
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

 	// handshake
    var sock = new SockJS("http://"+root+"/ws/chat");
//    var sock = new SockJS("/ws/chat");
    
    // onopen : connection이 맺어졌을 때의 callback
    sock.onopen = function () {
        // send : connection으로 message를 전달
        // connection이 맺어진 후 가입(JOIN) 메시지를 전달
        sock.send(JSON.stringify({type: 'JOIN', roomId: roomId, sender: member}));
        chatStatus.text('Info: connection opened.');
        
        console.log(playerAInput.val());
        
    }
    
 	// onmessage : message를 받았을 때의 callback
    sock.onmessage = function (event) {
        var content = JSON.parse(event.data);
        
 		if(content.type == 'PROBLEM'){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' 
 					+ content.card1 + content.card2 + content.card3
 					+ content.card4 + content.card5 + content.card6
 					+ content.card7 + content.card8 + content.card9 + '\n');
 			
 			var cardList = 
 				[content.card1, content.card2, content.card3,
 				content.card4, content.card5, content.card6,
 				content.card7, content.card8, content.card9];
 			
 			var defaultUrl = "/genious/resources/images/";
 			var defaultExtension = ".jpg";
 			
 			$("#card1").attr("src", defaultUrl + content.card1 + defaultExtension);
 			$("#card2").attr("src", defaultUrl + content.card2 + defaultExtension);
 			$("#card3").attr("src", defaultUrl + content.card3 + defaultExtension);
 			$("#card4").attr("src", defaultUrl + content.card4 + defaultExtension);
 			$("#card5").attr("src", defaultUrl + content.card5 + defaultExtension);
 			$("#card6").attr("src", defaultUrl + content.card6 + defaultExtension);
 			$("#card7").attr("src", defaultUrl + content.card7 + defaultExtension);
 			$("#card8").attr("src", defaultUrl + content.card8 + defaultExtension);
 			$("#card9").attr("src", defaultUrl + content.card9 + defaultExtension);

 			
 			/* cardList.forEach(updateProblemBoard); */
 			
 		}else if(content.type == 'UNI'){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');	
 			
 			console.log("content.sender : " + content.sender);
 			console.log("playerAInput.val()" + playerAInput.val());
 			
 			var score = parseInt(content.score);
 			
 			if(content.sender == playerAInput.val()){
// 	 			console.log("scoreAInput.val()" + scoreAInput.val());
 				var existingScore = parseInt(scoreAInput.val()); 
 				scoreAInput.val(existingScore + score);
 			}else{
// 				console.log("scoreBInput.val()" + scoreBInput.val());
 				var existingScore = parseInt(scoreBInput.val()); 
 				scoreBInput.val(existingScore + score);
 			}
 			
// 			console.log("sender : " + content.sender);
// 			console.log("score " + score);
// 			console.log("existingScore " + existingScore);
// 			console.log("type score " + typeof(score));
// 			console.log("type existingScore " + typeof(existingScore));
 			 			
 		}else if(content.type == 'READY'){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 			$('.scoreA').val(0);
 			$('.scoreB').val(0);
 		}else if(content.type == 'ON'){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 			var score = parseInt(content.score);
 			
 			console.log("content.sender : " + content.sender);
 			console.log("playerAInput.val()" + playerAInput.val());
 			console.log("playerBInput.val()" + playerBInput.val());

 			if(content.sender == playerAInput.val()){
// 	 			console.log("scoreAInput.val()" + scoreAInput.val());
 				var existingScore = parseInt(scoreAInput.val()); 
 				scoreAInput.val(existingScore + score);
 			}else{
// 				console.log("scoreBInput.val()" + scoreBInput.val());
 				var existingScore = parseInt(scoreBInput.val()); 
 				scoreBInput.val(existingScore + score);
 			}
 			
 			if(score > 0){
 	        	answerList.append('<li>' + content.message.substring(0, 3) + '</li>');
 			}
 			
 		}else if(content.type == 'JOIN'){
 	    	
 			chatMsgArea.eq(0).prepend(content.message + '\n');
 			
 	        if(!playerAInput.val()){
 	        	playerAInput.val(content.sender);
 	        }else{
 	        	playerBInput.val(content.sender);
 	        }
 	        
 		}else if(content.type == 'ROUND'){
 			
            chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');	 			
//            console.log(content.round);
            roundP.text(content.round + ' ROUND');
 			
 		}else{
            chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');	 			
 		}
        
        /* if(content.type == 'UNION'){
        	answerList.append('<li>' + content.message + '</li>');
        } */
    };
    
    /* function updateProblemBoard(item){

    } */
    
 	// onclose
 	sock.onclose = function(event){
 		chatStatus.text('Info: connection closed.');
 	}
	
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
    			{type: 'UNI', roomId: roomId, sender: member}));
    });
    
    onBtn.click(function(){
    	var message = selectedInput.val();
    	sock.send(JSON.stringify(
    			{type: 'ON', roomId: roomId, sender: member, message: message}));
    	selectedInput.val('');
    });
    
    readyBtn.click(function(){
    	sock.send(JSON.stringify(
    			{type: 'READY', roomId: roomId, sender: member}));
    });
    
});