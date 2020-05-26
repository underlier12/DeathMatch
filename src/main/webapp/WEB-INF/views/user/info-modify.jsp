<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<body>
	<div>
		<div class="col-md-10 col-md-offset-1">
			<h2>연락처 수정</h2>
			<p>
				<c:out value="${login.userId }" />
				님의 정보입니다
			</p>
			<p>회원 정보는 개인정보 처리 방침에 따라 안전하게 보호되며, 회원님의 명백한 동의 없이 공개 또는 제 3자에게
				제공되지 않습니다</p>
		</div>
	</div>

	<div class="row">
		<div class="col-md-3 col-md-offset-1"
			style="margin-top: 50px; padding-left: 30px;">
			<h4>사용자 이름</h4>
		</div>
		<div class="col-md-8" style="margin-top: 50px; padding-left: 60px;">
			<h4>
				<c:out value="${login.userId }" />
			</h4>
			<p>본인 확인을 통해 정보를 수정하실 수 있습니다</p>
			<button type="button" class="btn btn-default" id="changePwBtn">수정</button>
		</div>

		<div class="col-md-3 col-md-offset-1"
			style="margin-top: 50px; padding-left: 30px;">
			<h4>휴대전화</h4>
		</div>
		
		<div class="col-md-8" style="margin-top: 50px; padding-left: 60px;">
			<p>휴대전화 :<c:out value ="${fn:substring(login.phone,0,5).concat('***-****')}" /></p>
			<p>사용자 휴대번호입니다.</p>
			<button type="button" class="btn btn-default" id="changePwBtn">수정</button>
		</div>

	</div>

	<%@ include file="../includes/footer.jsp"%>
</body>
</html>