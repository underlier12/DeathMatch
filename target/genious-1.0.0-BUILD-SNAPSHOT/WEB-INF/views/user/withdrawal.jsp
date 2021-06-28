<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<script type="text/javascript">
	var message = "${msg}";
	if (message != "") {
		alert(message);
	}
</script>
<link rel="stylesheet" href="/css/user/withdrawal.css">
<body>

	<div style="margin-top: 80px">
		<div class="col-md-10 col-md-offset-1">
			<h2 id="title">회원 탈퇴</h2>
		</div>
	</div>

	<form action="/auth/user/delete" method="post" id="userDelete">
		<div class="row changePw-row" style="margin-top: 80px">
			<div class="col-md-4 col-md-offset-4" style="margin-top: 80px;">
				<p id="safeTitle">그동안 데스매치를 이용해 주셔서 감사합니다</p>
				<p class ="text-warning">회원탈퇴를 위해서 비밀번호를 입력해주세요</p>
			</div>

			<div class="col-md-4 col-md-offset-4" style= "margin-top: 20px;">
				<div>
					<input type="password" class="form-control" name="pw" id="checkPw1"
						placeholder="비밀번호" maxlength="15">
				</div>
				<div>
					<input type="password" class="form-control" id="checkPw2"
						placeholder="비밀번호 확인" maxlength="15">
				</div>
				<c:if test = "${msg ne null}">
					alert(msg);
				</c:if>
			</div>

		</div>
		<div class="col-md-4 col-md-offset-4"
			style="text-align: center; margin-top: 40px;">
			<span>
				<button type="button" class="btn btn-default" id="deleteBtn">탈퇴</button>
				<button type="button" class="btn btn-default" id="cancleBtn" onclick ="history.back()">취소</button>
			</span>
		</div>
		
	</form>

	<%@ include file="../includes/footer.jsp"%>


</body>
<script type="text/javascript">
	$("#deleteBtn").click(function(){
		var checkPw1 = $("#checkPw1").val();
		var checkPw2 = $("#checkPw2").val();
		
		if (checkPw1 == "" || checkPw2 == null ||
				checkPw1 == "" || checkPw2 == null) {
			alert("새 비밀번호를 입력해 주세요");
			return false;
		}
		if(checkPw1 != checkPw2){
			alert("비밀번호와 비밀번호 확인이 다릅니다");
			return false;
		}
		if(checkPw1.length<4){
			alert("비밀번호를 4자리이상 입력해 주세요");
			return false;
		}
		if(checkPw2.length<4 || checkPw2.length<4){
			alert("비밀번호를 4자리이상 입력해 주세요");
			return false;
		}
		userDelete.submit();
	})
</script>
</html>