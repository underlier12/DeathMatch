<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/header.jsp"%>
<script type="text/javascript">
	var message = "${msg}";
	if (message != "") {
		alert(message);
	}
</script>
<link rel="stylesheet" href="/css/changePw.css">
<body>

	<div style="margin-top: 80px">
		<div class="col-md-10 col-md-offset-1">
			<h2 id="title">비밀번호 변경</h2>
		</div>
	</div>

	<form action="/user/changePw" method="post" id="changePw">
		<div class="row changePw-row" style="margin-top: 80px">
			<div class="col-md-4 col-md-offset-4" style="margin-top: 80px;">
				<p id="safeTitle">안전한 비밀번호로 내 정보를 보호하세요</p>
				<p class ="text-warning">다른 아이디/사이트에서 사용한 적 없는 비밀번호</p>
				<p class ="text-warning">이전에 사용한 적 없는 비밀번호가 안전합니다</p>
			</div>

			<div class="col-md-4 col-md-offset-4" style= "margin-top: 20px;">
				<div>
					<input type="password" class="form-control" name="currentPw" id="currentPw"
						placeholder="현재 비밀번호" maxlength="15">
				</div>
				<div>
					<input type="password" class="form-control" name="changePw" id="changePw1"
						placeholder="새 비밀번호" maxlength="15">
				</div>
				<div>
					<input type="password" class="form-control" id="changePw2"
						placeholder="새 비밀번호 확인" maxlength="15">
				</div>
			</div>

		</div>
		<div class="col-md-4 col-md-offset-4"
			style="text-align: center; margin-top: 40px;">
			<span>
				<button type="button" class="btn btn-default" id="changePwBtn">변경</button>
				<button type="button" class="btn btn-default" id="cancleBtn" onclick ="history.back()">취소</button>
			</span>
		</div>
	</form>

	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>


</body>
<script type="text/javascript">
	//changePw 유효성검사 -> Button을 누를때 마다 값이 갱신되어야함
	$("#changePwBtn").click(function(){
		var currentPw = $("#currentPw").val();
		var changePw1 = $("#changePw1").val();
		var changePw2 = $("#changePw2").val();
		
		if(currentPw == '' || currentPw == null ){
			alert("현재 비밀번호를 입력해주세요");
			return false;
		}
		if (changePw1 == "" || changePw1 == null ||
				changePw2 == "" || changePw2 == null) {
			alert("새 비밀번호를 입력해 주세요");
			return false;
		}
		if(changePw1 != changePw2){
			alert("새 비밀번호와 비밀번호 확인이 다릅니다");
			return false;
		}
		if(currentPw.length<4){
			alert("현재 비밀번호를 4자리이상 입력해 주세요");
			return false;
		}
		if(changePw1.length<4 || changePw2.length<4){
			alert("새 비밀번호를 4자리이상 입력해 주세요");
			return false;
		}
		changePw.submit();
	})
</script>
</html>