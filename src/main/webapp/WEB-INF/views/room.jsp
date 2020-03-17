<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="includes/meta.jsp" %>
    <title>${room.name} 채팅방</title>
    <%@ include file="includes/header2.jsp" %>
</head>

<body>
	<h1>${room.name}</h1>
	
	<h4>${room.roomId}</h4>
	
	<p id="round"></p>
	
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
			<td><button class="select" name="1">1<img id="card1" class="card"></button></td>
			<td><button class="select" name="2">2<img id="card2" class="card"></button></td>
			<td><button class="select" name="3">3<img id="card3" class="card"></button></td>
	    </tr>
	    <tr>
			<td><button class="select" name="4">4<img id="card4" class="card"></button></td>
			<td><button class="select" name="5">5<img id="card5" class="card"></button></td>
			<td><button class="select" name="6">6<img id="card6" class="card"></button></td>
	    </tr>
	    <tr>
			<td><button class="select" name="7">7<img id="card7" class="card"></button></td>
			<td><button class="select" name="8">8<img id="card8" class="card"></button></td>
			<td><button class="select" name="9">9<img id="card9" class="card"></button></td>
	    </tr>
	</table>
	
	<input type="text" id="selected"><br>
	
	<button class="uni">결</button>
	<button class="on">합</button>
	<button class="ready">Ready</button> <br>
	
	<h3> Score </h3>
	<input id="playerA"> : <input id="playerB"><br>
	<input class="scoreA"> : <input class="scoreB">
	
	<%-- <script src="<c:url value="/resources/js/room.js" />"></script> --%>
	<link href="/genious/css/room.css" rel="stylesheet">
	<script src="/genious/js/room.js"></script>
	<%@ include file="includes/footer2.jsp" %>
</body>
</html>