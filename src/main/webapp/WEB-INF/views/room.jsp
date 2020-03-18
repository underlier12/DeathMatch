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

	<div class="container-fluid">
		<!-- first row -->
		<div class="row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">
				<h1>${room.name}</h1>
				<h4>${room.roomId}</h4>			
			</div>
			<div class="col-sm-4"></div>
		</div>
		<!-- second row -->
		<div class="row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">
				<p id="round"></p>
			</div>
			<div class="col-sm-4"></div>
		</div>
		
		<!-- third row -->
		<div class="row">
			<div class="col-sm-2"></div>
			<div class="col-sm-1"></div>
			<div class="col-sm-1 no-padding">
				<ul class="answer no-padding">
				</ul>
			</div>
			<div class="col-sm-4 no-padding no-margin">
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
			<div class="col-sm-1">
				<div class="row">
					<button class="uni">결</button>
				</div>
				<div class="row">
					<button class="on">합</button>
				</div>
				<div class="row">
					<button class="ready">준비</button>
				</div>
			</div>
			<div class="col-sm-1"></div>
			<div class="col-sm-2"></div>
		</div>
		
		<!-- forth row -->
		<div class="row">
			<div class="col-sm-2"></div>
			<div class="col-sm-1"></div>
			<div class="col-sm-1"></div>
			<div class="col-sm-4">
				<div id='chatStatus'></div>
			</div>
			<div class="col-sm-1"></div>
			<div class="col-sm-1"></div>
			<div class="col-sm-2"></div>
		</div>
		
		
		<!-- fifth row -->
		<div class="row">
			<div class="col-sm-2"></div>
			<div class="col-sm-1"></div>
			<div class="col-sm-1">
				<div class="row">
					<input id="playerA">
					<input class="scoreA">				
				</div>
			</div>
			<div class="col-sm-4">
				<div class="row">
					<div class="col-sm-12 hidden-xs">
						<div class="row">
							<input type="text" id="selected">
						</div>
						<div class="row">
							<div class="content" data-room-id="${room.roomId}" data-member="${member}">
						    	<textarea name="chatMsg" rows="4" cols="70" readonly></textarea><p>
						    </div>
						</div>
					    <!-- <input name="message">
					    <button class="send">보내기</button> -->									
					</div>
				</div>
			</div>
			<div class="col-sm-1">
				<div class="row">
					<input id="playerB">
					<input class="scoreB">
				</div>
			</div>
			<div class="col-sm-1"></div>
			<div class="col-sm-2"></div>
		</div>
	</div>
	
	<%-- <script src="<c:url value="/resources/js/room.js" />"></script> --%>
	<script src="/genious/js/room.js"></script>
	<%@ include file="includes/footer2.jsp" %>
</body>
</html>