<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${room.name} 채팅방</title>
    <script type="text/javascript" src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"></script>
</head>
<body>
<h1>${room.name}(${room.roomId})</h1>

<h3 id="round"></h3>

<div id='chatStatus'></div>
<div class="content" data-room-id="${room.roomId}" data-member="${member}">
    <textarea name="chatMsg" rows="5" cols="40" readonly></textarea><p>
    <input name="message">
    <button class="send">보내기</button>
</div>

<ul class="answer">
</ul>

<table border="1">
    <tr>
		<td><button class="select" name="1">1<img id="card1" src=""></button></td>
		<td><button class="select" name="2">2<img id="card2" src=""></button></td>
		<td><button class="select" name="3">3<img id="card3" src=""></button></td>
    </tr>
    <tr>
		<td><button class="select" name="4">4<img id="card4" src=""></button></td>
		<td><button class="select" name="5">5<img id="card5" src=""></button></td>
		<td><button class="select" name="6">6<img id="card6" src=""></button></td>
    </tr>
    <tr>
		<td><button class="select" name="7">7<img id="card7" src=""></button></td>
		<td><button class="select" name="8">8<img id="card8" src=""></button></td>
		<td><button class="select" name="9">9<img id="card9" src=""></button></td>
    </tr>
</table>

<input type="text" id="selected"><br>

<button class="uni">결</button>
<button class="on">합</button>
<button class="ready">Ready</button> <br>

<h3> Score </h3>
<input value="a"> : <input value="b"><br>
<input id="a" class="score1"> : <input id="b" class="score2">

<script>
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

	 	// handshake
        var sock = new SockJS("http://"+root+"/ws/chat"); 
	    
        // onopen : connection이 맺어졌을 때의 callback
        sock.onopen = function () {
            // send : connection으로 message를 전달
            // connection이 맺어진 후 가입(JOIN) 메시지를 전달
            sock.send(JSON.stringify({type: 'JOIN', roomId: roomId, sender: member}));
            chatStatus.text('Info: connection opened.');
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
	 			
	 			var score = parseInt(content.score);
	 			var existingScore = parseInt($('.score1').val());
	 			
	 			console.log("sender : " + content.sender);
	 			console.log("score " + score);
	 			console.log("existingScore " + existingScore);
	 			console.log("type score " + typeof(score));
	 			console.log("type existingScore " + typeof(existingScore));
	 			
	 			$('.score1').val(existingScore + score);
	 			
	 		}else if(content.type == 'READY'){
	 			chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
	 			$('.score1').val(0);
	 			$('.score2').val(0);
	 		}else if(content.type == 'ON'){
	 			chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
	 			var score = parseInt(content.score);
	 			var existingScore = parseInt($('.score1').val());
	 			
	 			$('.score1').val(existingScore + score);
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
</script>
</body>
</html>