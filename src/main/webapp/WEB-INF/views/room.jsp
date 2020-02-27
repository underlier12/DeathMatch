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
<div id='chatStatus'></div>
<div class="content" data-room-id="${room.roomId}" data-member="${member}">
    <textarea name="chatMsg" rows="5" cols="40" readonly></textarea><p>
    <input name="message">
    <button class="send">보내기</button>
</div>

<ul class="answer">
</ul>

<table border="1">
    <tr> <!-- align = "center"> -->
		<td><button class="select" name="1"><img src="/genious/resources/images/MGB.jpg"></button></td>
		<td><button class="select" name="2"><img src="/genious/resources/images/SOW.jpg"></button></td>
		<td><button class="select" name="3"><img src="/genious/resources/images/AGB.jpg"></button></td>
    </tr>
    <tr>
		<td><button class="select" name="4"><img src="/genious/resources/images/MGG.jpg"></button></td>
		<td><button class="select" name="5"><img src="/genious/resources/images/SGG.jpg"></button></td>
		<td><button class="select" name="6"><img src="/genious/resources/images/AOB.jpg"></button></td>
    </tr>
    <tr>
		<td><button class="select" name="7"><img src="/genious/resources/images/APG.jpg"></button></td>
		<td><button class="select" name="8"><img src="/genious/resources/images/APB.jpg"></button></td>
		<td><button class="select" name="9"><img src="/genious/resources/images/SPB.jpg"></button></td>
    </tr>
</table>

<input type="text" id="selected"><br>

<button class="uni">결</button>
<button class="on">합</button>
<button class="ready">Ready</button> <br>

Score <input class="score1"> : <input class="score2">

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
            chatMsgArea.eq(0).prepend(content.sender + ' : ' + content.message + '\n');
            
            if(content.type == 'UNION'){
            	answerList.append('<li>' + content.message + '</li>');
            }
        };
        
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