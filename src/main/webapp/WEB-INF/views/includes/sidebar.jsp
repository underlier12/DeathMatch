<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
</head>
<body>
	<%@ include file="/WEB-INF/views/includes/header.jsp"%>

	<!-- w3-sidebar w3-bar-block container -->
	<div class="sidebar">
		<div class="row">
			<div class="col-md-12">
				<h1 style="font-size: 45px;">
					<b>DeathMatch</b>
				</h1>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<img src="${pageContext.request.contextPath}/images/user.png" width="260">
			</div>
		</div>

		<div class="row">
			<div class="col-md-12 font-12" style="margin-left: 15px">
				<h3>
					<b>ID: ${login.userEmail }</b>
				</h3>
			</div>
			<div class="col-md-12 font-12" style="margin-left: 15px">
				<h3>
					<b>NAME:</b>
				</h3>
			</div>
			<div class="col-md-12 font-12" style="margin-left: 15px">
				<h3>
					<b>LEVEL:</b>
				</h3>
			</div>
		</div>

		<div class="row mypage">
			<form action ="/genious/user/myPage" method ="get">
				<div class="col-md-12">
					<button class="btn btn-default-sm"
						style="width: 260px; height: 60px; margin-top: 15px;">MyPage</button>
				</div>
			</form>

			<form action="/genious/user/logout" method="get">
				<div class="col-md-12">
					<c:if test="${!empty login}">
						<button type="submit" class="btn btn-default btn-block login-btn" id="logout"
							style="width: 260px; height: 60px; margin-top: 15px;">LogOut</button>
					</c:if>
				</div>
			</form>
		</div>
	</div>
</body>
</html>