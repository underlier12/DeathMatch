<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<%@ include file="../includes/meta.jsp"%>
<title>sidebar</title>
<%@ include file="../includes/header.jsp"%>
<link href="/css/main/sidebar.css" rel="stylesheet">
</head>
<body>
	<div id="sidebar">
		<div class="row title">
			<div class="col-md-12">
				<h1 class="title2">
					<b>DeathMatch</b>
				</h1>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<img src="${pageContext.request.contextPath}/images/user.png"
					width="260">
			</div>
		</div>

		<div class="row info">
			<div class="col-md-12 font-12">
				<h3>
					<b>ID: ${login.userId }</b>
				</h3>
			</div>
			<div class="col-md-12 font-12">
				<h3>
					<b>Email :${login.userEmail } </b>
				</h3>
			</div>
		</div>

		<div class="row mypage">
			<form action="/auth/user/mypage" method="get">
				<div class="col-md-12">
					<button class="btn btn-default btn-block sideBtn">MyPage</button>
				</div>
			</form>

			<form action="/auth/user/logout" method="get">
				<div class="col-md-12">
					<c:if test="${!empty login}">
						<button type="submit" class="btn btn-default btn-block sideBtn"
							id="logout">LogOut</button>
					</c:if>
				</div>
			</form>

			<form action="/record" method="get">
				<div class="col-md-12">
					<c:if test="${!empty login}">
						<button type="submit" class="btn btn-default btn-block sideBtn"
							>Record</button>
					</c:if>
				</div>			
			</form>
		</div>
	</div>
</body>
</html>