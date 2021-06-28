<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="../includes/meta.jsp"%>
<title>DeathMatch GameHome</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/main/rooms.css" rel="stylesheet">
</head>

<body>
	<div id="gameHome">
		<%@ include file="sidebar.jsp"%>

		<div id="gameRooms" class="content" data-msg="${msg}">

			<div class="title">
				<div>
					<h1>Room List</h1>
				</div>
				
				<div id="buttonList">
					<button type="button" class="btn btn-default btn-lg" id="btnCreate">방만들기</button>
					<button type="button" class="btn btn-default btn-lg" id="btnTuto">튜토리얼</button>
					<button type="button" class="btn btn-default btn-lg" id="btnNotice">공지사항</button>
					<button type="button" class="btn btn-default btn-lg"
						id="btnSuggestion">건의사항</button>
				</div>
			</div>

			<div class="row">
				
				<div class="col-md-3" id="leftRooms">
					<c:forEach var="room" items="${rooms }" begin="0" end="4">
						<div class="oneRoom">
							<c:choose>
								<c:when test="${room.gameType eq 'indian' }">
									<div id="imageDiv">
										<img
											src="${pageContext.request.contextPath}/images/indianLogo.png"
											width="130px" height="50px">
									</div>
								</c:when>

								<c:when test="${room.gameType eq 'union' }">
									<div id="imageDiv">
										<img
											src="${pageContext.request.contextPath}/images/UnionLogo.png"
											width="130px" height="50px">
									</div>
								</c:when>
							</c:choose>
							<p class="roomName">${room.name}</p>

							<c:choose>
								<c:when test="${room.playing eq true }">
									<p class="isPlaying">Playing...</p>
								</c:when>
								<c:when test="${room.playing eq false }">
									<p class="isPlaying">Waiting...</p>
								</c:when>
							</c:choose>

							<button type="button" class="gameJoinBtn"
								onclick="location.href='rooms/<c:out value="${room.gameType }/${room.roomId}"/>'">Join</button>

						</div>
					</c:forEach>
				</div>

				<div class="col-md-4" id ="rightRooms">
					<c:forEach var="room" items="${rooms }" begin="5" end="9">
						<div class="oneRoom">
							<c:choose>
								<c:when test="${room.gameType eq null }">
									<div id="imageDiv">
										<img
											src="${pageContext.request.contextPath}/images/UnionLogo.png"
											width="130px" height="50px">
									</div>
								</c:when>
							</c:choose>
							<p class="roomName">${room.name}</p>

							<c:choose>
								<c:when test="${room.playing eq true }">
									<p class="isPlaying">Playing...</p>
								</c:when>
								<c:when test="${room.playing eq false }">
									<p class="isPlaying">Waiting...</p>
								</c:when>
							</c:choose>

							<button type="button" class="gameJoinBtn"
								onclick="location.href='rooms/<c:out value="${room.roomId}"/>'">Join</button>

						</div>
					</c:forEach>
				</div>

			</div>

			<div class="text-center" id="pageBtn">
				<ul class="pagination">
					<c:if test="${pageMaker.prev }">
						<li><a
							href='<c:url value="/rooms?page=${pageMaker.startPage-1 }"/>'><i
								class="fa fa-chevron-left"></i></a></li>
					</c:if>
					<c:forEach begin="${pageMaker.startPage}"
						end="${pageMaker.endPage }" var="idx">
						<li
							<c:out value ="${pageMaker.cri.page == idx?'class =active':''}"/>>
							<a id="pageNum" href="rooms?page=${idx }">${idx }</a>
						</li>
					</c:forEach>
					<c:if test="${pageMaker.next && pageMaker.endPage >0 }">
						<li><a
							href='<c:url value="/rooms?page=${pageMaker.endPage+1 }"/>'><i
								class="fa fa-chevron-right"></i></a></li>
					</c:if>
				</ul>
			</div>
		</div>
	</div>

	<%@ include file="modal-room.jsp"%>
	<%@ include file="modal-tuto.jsp"%>
	<script src="/js/main/rooms.js?ver=1"></script>

</body>