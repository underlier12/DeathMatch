<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>

<body>

	<div>
		<div class="col-md-10 col-md-offset-1" style="text-align: center">
			<h2>회원 정보</h2>
		</div>
	</div>

	<div class ="row">
		<div class ="col-md-4 col-md-offset-2" style ="margin-top :80px; text-align:center" >
			<h3>사용자</h3>
			<h4><c:out value ="${login.userId }"/></h4>
			<button type="button" class="btn btn-default" id="changePwBtn">수정</button>
		</div>

		<div class ="col-md-6" style ="margin-top :80px">
			<h3>비밀번호 수정</h3>
			<button type="button" class="btn btn-default" id="changePwBtn" onclick ="location.href='/genious/user/changePw'">수정</button>
		</div>
	</div>
	
	
	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
</body>
</html>