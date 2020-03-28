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

	<div id="game">
	
		<!-- first row -->
		<div class="flex-box">
			<div>
				<img id="logo" src="/genious/resources/images/UnionLogo.png">
			</div>
		</div>
		
		<!-- second row -->
		<div class="flex-box">
			<div>
				<p id="round"></p>
			</div>
		</div>
		
		<!-- third row -->
		<div class="flex-box">
			<div>
				<ul id="answer" class="no-padding"></ul>
			</div>
			<div>
				<table>
				    <tr>
						<td>
							<input type="checkbox"  id="card1" class="selectbox" name="1">
							<label for="card1" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card2" class="selectbox" name="2">
							<label for="card2" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card3" class="selectbox" name="3">
							<label for="card3" class="label"><img class="card"></label>
						</td>
				    </tr>
				    <tr>
						<td>
							<input type="checkbox"  id="card4" class="selectbox" name="4">
							<label for="card4" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card5" class="selectbox" name="5">
							<label for="card5" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card6" class="selectbox" name="6">
							<label for="card6" class="label"><img class="card"></label>
						</td>
				    </tr>
				    <tr>
						<td>
							<input type="checkbox"  id="card7" class="selectbox" name="7">
							<label for="card7" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card8" class="selectbox" name="8">
							<label for="card8" class="label"><img class="card"></label>
						</td>
						<td>
							<input type="checkbox"  id="card9" class="selectbox" name="9">
							<label for="card9" class="label"><img class="card"></label>
						</td>
				    </tr>
				</table>
			</div>
			<div>
				<button id="uni" class="union">결</button>
				<button id="on" class="union">합</button>
				<button id="ready">준비</button>
			</div>
		</div>
		
		<!-- forth row -->
		<div class="flex-box">
			<div class="exclaim-wrapper">
				<div id="exclaimA" class="exclaim">				
					<p id="statementA" class="statement"></p>
				</div>
			</div>
			<div class="timer-wrapper">
				<div id="timerA" class="timer"></div>
			</div>
			<div>
				<div id='pass'></div>
			</div>
			<div class="timer-wrapper">
				<div id="timerB" class="timer"></div>
			</div>
			<div class="exclaim-wrapper">
				<div id="exclaimB" class="exclaim">
					<p id="statementB" class="statement"></p>				
				</div>
			</div>
		</div>
		
		<!-- fifth row -->
		<div class="flex-box">
			<div>
				<input id="playerA" class="player" readonly>
				<input id="scoreA" class="score" readonly>				
			</div>
			<div>
				<div id='connectionStatus'></div>
				<div class="content" data-room-id="${room.roomId}" data-member="${member}">
			    	<textarea id="broadcast" rows="4" cols="70" readonly></textarea><p>
			    </div>							
			</div>
			<div>
				<input id="playerB" class="player" readonly>
				<input id="scoreB" class="score" readonly>				
			</div>
		</div>
		
		<!-- sixth row -->
		<div class="flex-box">
			<button id="leave">나가기</button>
		</div>
		
		<%@ include file="includes/footer.jsp" %>
	</div>

	<script src="/genious/js/room.js"></script>
</body>
</html>