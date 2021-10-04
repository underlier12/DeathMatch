<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="../../includes/meta.jsp"%>
<title>Genius Death Match</title>
<%@ include file="../../includes/header.jsp"%>
<link href="/css/game/indian/indian.css" rel="stylesheet">
</head>
<body>
	<sec:authentication property="principal" var="user"/>
	<c:set var="userText" value="${user.username}"/>

	<div id="game">
		<div class="indian-flex">
			<div>
				<img id="logo"
					src="${pageContext.request.contextPath}/images/indianLogo.png">
			</div>
		</div>

		<div class="indian-flex">
			<div class="cards">
				<img id="card1"
					src="${pageContext.request.contextPath}/images/indiancards/card.png">
				<input id="playerId1" readonly>
			</div>
			<div class="timer-wrapper">
				<div class="timer" id="timer"></div>
			</div>
			<div class="cards">
				<img id="card2"
					src="${pageContext.request.contextPath}/images/indiancards/card.png">
				<input id="playerId2" readonly>
			</div>
		</div>

		<div class="indian-flex">
			<div class="chipDiv">
				<div id="chip1">보유칩</div>
				<div>
					<img id="chip1Img"
						src="${pageContext.request.contextPath}/images/indiancards/chip.png">
					<div id="chipScore1"></div>
				</div>
			</div>
			<div class="chipDiv">
				<div id="betChip1">배팅칩</div>
				<div>
					<img id="betchip1Img"
						src="${pageContext.request.contextPath}/images/indiancards/chipImg.png">
					<div id="betchip1Score"></div>
				</div>
			</div>
			<div class="chipDiv">
				<div id="betChip2">배팅칩</div>
				<div id="betDiv2">
					<img id="betchip2Img"
						src="${pageContext.request.contextPath}/images/indiancards/chipImg.png">
					<div id="betchip2Score"></div>
				</div>
			</div>
			<div class="chipDiv">
				<div id="chip2">보유칩</div>
				<div id="chipDiv2">
					<img id="chip2Img"
						src="${pageContext.request.contextPath}/images/indiancards/chip.png">
					<div id="chipScore2"></div>
				</div>
			</div>
		</div>

		<div class="indian-flex">
			<div>
				<button type="button" id="readyBtn">Ready</button>
				<div>
					<button type="button" id="betBtn">BETTING</button>
				</div>
			</div>
		</div>

		<div class="indian-flex">
			<div>
				<button type="button" id="chipAllInBtn">All in</button>
				<div>
					<button type="button" id="betGiveUpBtn">포기</button>
				</div>
			</div>
			<div id="chipBettingArea">
				<input type="text" id="chipBetting" readonly="readonly" value="0">
			</div>
			<div>
				<button type="button" id="chipUpBtn">▲</button>
				<br>
				<button type="button" id="chipDownBtn">▼</button>
			</div>
			<div>
				<button type="button" id="betSendBtn">배팅</button>
			</div>
		</div>

		<div class="indian-flex">
			<div>
				<div id="connectionArea"></div>
				<textarea id="infoArea" rows="4" cols="30" readonly></textarea>
				<textarea id="chatArea" rows="4" cols="70" readonly></textarea>
				<input type="text" id="message" /> <input type="button"
					id="sendBtn" value="submit" />
				<button id="leave">나가기</button>
			</div>
		</div>

		<div class="indian-flex">
			<div class="content" data-room-id="${room.roomId}"
				data-member="${fn:substringBefore(userText,'@')}"></div>
			<%@ include file="../../includes/footer.jsp"%>
		</div>

	</div>

	<script src="/js/game/indian/indian.js"></script>

</body>
</html>