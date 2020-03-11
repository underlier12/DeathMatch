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

    var sock = new SockJS("http://"+root+"/ws/chat");
    
    sock.onopen = function () {
        sock.send(JSON.stringify({type: 'JOIN', roomId: roomId, sender: member}));
        chatStatus.text('Info: connection opened.');
    }
    
    sock.onmessage = function (event) {
        var content = JSON.parse(event.data);
        
 		if(content.type == 'PROBLEM'){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' 
 					+ content.cards + '\n');
 
 			var defaultUrl = "/genious/resources/images/";
 			var defaultExtension = ".jpg";
 			
 			for(var i=0; i < content.cards.length; i++){
 				console.log(content.cards[i]);
 				$(".card:eq("+i+")").attr("src", defaultUrl + content.cards[i] + defaultExtension);
 			}
 			
 		}else if(content.type == 'UNI'){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');	
 			
 			console.log("content.sender : " + content.sender);
 			console.log("playerAInput.val()" + playerAInput.val());
 			
 			var score = parseInt(content.score);
 			
 			if(content.user1 == playerAInput.val()){
 				var existingScore = parseInt(scoreAInput.val()); 
 				scoreAInput.val(existingScore + score);
 			}else{
 				var existingScore = parseInt(scoreBInput.val()); 
 				scoreBInput.val(existingScore + score);
 			}
 			
 		}else if(content.type == 'READY'){
 			
 			if(content.message){
 				chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 			}
 			
 			if(content.user1){
 				
 				playerAInput.val(content.user1);
 				playerBInput.val(content.user2);
 				
 				$('.scoreA').val(0);
 				$('.scoreB').val(0);
 			}
 			
 		}else if(content.type == 'ON'){
 			chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
 			var score = parseInt(content.score);
 			
 			console.log("content.sender : " + content.sender);
 			console.log("playerAInput.val()" + playerAInput.val());
 			console.log("playerBInput.val()" + playerBInput.val());

 			if(content.user1 == playerAInput.val()){
 				var existingScore = parseInt(scoreAInput.val()); 
 				scoreAInput.val(existingScore + score);
 			}else{
 				var existingScore = parseInt(scoreBInput.val()); 
 				scoreBInput.val(existingScore + score);
 			}
 			
 			if(score > 0){
 	        	answerList.append('<li>' + content.message.substring(0, 3) + '</li>');
 			}
 			
 		}else if(content.type == 'JOIN'){
 	    	
 			chatMsgArea.eq(0).prepend(content.message + '\n');
 	        
 		}else if(content.type == 'ROUND'){
 			
 			console.log("round : " + content.round);
            chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');	 			
            roundP.text(content.round + ' ROUND');
 			
 		}else{
            chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');	 			
 		}
    };
    
 	sock.onclose = function(event){
 		console.log("sock.onclose");
        sock.send(JSON.stringify({type: 'OUT', roomId: roomId, sender: member}));
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