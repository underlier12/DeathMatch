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

<!-- <script src="/resources/js/room.js"></script> -->
<script src="<c:url value="/resources/js/room.js" />"></script>
</body>
</html>