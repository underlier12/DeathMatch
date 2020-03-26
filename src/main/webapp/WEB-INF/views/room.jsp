<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="includes/meta.jsp" %>
    <title>${room.name} 채팅방</title>
    <%@ include file="includes/header.jsp" %>
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
						<td>
							<input type="checkbox"  id="card1" class="selectbox checkbox" name="1">
							<label for="card1" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card2" class="selectbox checkbox" name="2">
							<label for="card2" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card3" class="selectbox checkbox" name="3">
							<label for="card3" class="label"><img class="card"></label>
						</td>
				    </tr>
				    <tr>
						<td>
							<input type="checkbox"  id="card4" class="selectbox checkbox" name="4">
							<label for="card4" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card5" class="selectbox checkbox" name="5">
							<label for="card5" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card6" class="selectbox checkbox" name="6">
							<label for="card6" class="label"><img class="card"></label>
						</td>
				    </tr>
				    <tr>
						<td>
							<input type="checkbox"  id="card7" class="selectbox checkbox" name="7">
							<label for="card7" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card8" class="selectbox checkbox" name="8">
							<label for="card8" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card9" class="selectbox checkbox" name="9">
							<label for="card9" class="label"><img class="card"></label>
						</td>
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
			<div class="timer">
				<div id="timerA"></div>
			</div>
			<div class="connection">
				<div id='connectionStatus'></div>
			</div>
			<div class="timer">
				<div id="timerB"></div>
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
				
		
		<%@ include file="includes/footer.jsp" %>
	</div>

	<script src="/genious/js/room.js"></script>
</body>
</html>