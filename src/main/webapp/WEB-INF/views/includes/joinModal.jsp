<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<!-- 회원가입 Modal 추가 -->
	<div class="modal fade" id="registerModal" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 id="modal-title" class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<table class="table table-bordered noticeView-table">
						<tr>
							<td class="emai">회원 이메일 :</td>
							<td><input type ="email" name ="userEmail" id="userEmail"></td>
						</tr>

						<tr>
							<td class="view_title">비밀 번호:</td>
							<td><input type ="password" name ="pw" id ="pw"></td>
						</tr>
						
						<tr>
							<td class="view_title">회원 이름:</td>
							<td><input type ="text" name ="name" id="userName"></td>
						</tr>
						
						<tr>
							<td>휴대폰 번호:</td>
							<td><input type="text" name ="phone" id="phone"></td>
						</tr>

					</table>
				</div>
				<div class="modal-footer">
					<button type="button" id ="join" class ="btn btn-default btn-sm">가입</button>
					<button type="button" class="btn btn-default btn-sm"
						data-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
	

</body>
</html>