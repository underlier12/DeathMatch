<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<link rel="stylesheet" href="/genious/css/roomModal.css">
<body>

	<!-- 회원가입 Modal 추가 -->
	<div class="modal fade" id="roomModal" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content" id ="roomModalContent">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 id="modal-title" class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<table class="table">
						<tr>
							<td><p id ="roomIdTitle">방 만들기 :</p></td>
							<td><input type ="email" name ="name" id="roomName"></td>
						</tr>

					</table>
				</div>
				<div class="modal-footer">
					<button type="button" id ="createRoom" class ="btn btn-default btn-sm">방만들기</button>
					<button type="button" class="btn btn-default btn-sm"
						data-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>