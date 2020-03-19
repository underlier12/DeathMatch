<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="includes/meta.jsp" %>
    <title>${room.name} 채팅방</title>
    <%@ include file="includes/header2.jsp" %>
    <link href="/genious/css/room.css" rel="stylesheet">
</head>

<body>

	<div class="game">
	
		<!-- first row -->
		<div class="title-box">
			<div class="title">
				<img class="logo" src="/genious/resources/images/UnionLogo.png">
			</div>
		</div>
		
		<!-- second row -->
		<div class="round-box">
			<div class="round">
				<p id="round"></p>
			</div>
		</div>
		
		<!-- third row -->
		<div class="problem-box">
			<div class="answerSheet">
				<ul class="answer no-padding"></ul>
			</div>
			<div class="problem">
				<table>
				    <tr>
						<td><button class="select" name="1"><img class="card"></button></td>
						<td><button class="select" name="2"><img class="card"></button></td>
						<td><button class="select" name="3"><img class="card"></button></td>
				    </tr>
				    <tr>
						<td><button class="select" name="4"><img class="card"></button></td>
						<td><button class="select" name="5"><img class="card"></button></td>
						<td><button class="select" name="6"><img class="card"></button></td>
				    </tr>
				    <tr>
						<td><button class="select" name="7"><img class="card"></button></td>
						<td><button class="select" name="8"><img class="card"></button></td>
						<td><button class="select" name="9"><img class="card"></button></td>
				    </tr>
				</table>
			</div>
			<div class="buttons">
				<button class="uni">결</button>
				<button class="on">합</button>
				<button class="ready">준비</button>
			</div>
		</div>
		
		<!-- forth row -->
		<div class="event-box">
			<div class="exclaimA">
			
			</div>
			<div class="timerA">
			
			</div>
			<div class="connection">
				<div id='connectionStatus'></div>
			</div>
			<div class="timerB">
			
			</div>
			<div class="exclaimB">
			
			</div>
		</div>
		
		<!-- fifth row -->
		<div class="battle-box">
			<div class="playerA">
				<input id="playerA" readonly>
				<input id="scoreA" readonly>				
			</div>
			<div class="broadcast">
				<input type="text" id="selected">
				<div class="content" data-room-id="${room.roomId}" data-member="${member}">
			    	<textarea id="broadcast" rows="4" cols="70" readonly></textarea><p>
			    </div>
			    <!-- <input name="message">
			    <button class="send">보내기</button> -->									
			</div>
			<div class="playerB">
				<input id="playerB" readonly>
				<input id="scoreB" readonly>				
			</div>
		</div>
		
		<!-- test row -->
		<div>
			<h1>${room.name}</h1>
			<h4>${room.roomId}</h4>
		</div>
		
		<%@ include file="includes/footer2.jsp" %>
	</div>

	<script src="/genious/js/room.js"></script>
</body>
</html>