<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<link rel="stylesheet" href="/css/roomModal.css">
<body>

	<!-- 회원가입 Modal 추가 -->
	<div class="modal fade" id="roomModal" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content -->
			<div class="modal-content" id ="roomModalContent">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 id="modal-title" class="modal-title">방 생성</h2>
				</div>
				<div class="modal-body">
					<table class="table no-margin">
						<tr>
							<td><p id ="roomIdTitle">방 이름 :</p></td>
							<td><input type ="email" name ="name" id="RoomName"></td>
						</tr>
						<tr>
							<td>
								<input type="radio" id="union" value="union" class="radioBtn" name="type" checked>
								<label for="union" class="label">
									<img class="typeImg boundary" src="/resources/images/UnionLogo.png">
								</label>
							</td>
							<!-- <td>
								<input type="radio" id="indian" value="indian" class="radioBtn" name="type">
								<label for="indian" class="label">
									<img class="typeImg" src="">
								</label>
							</td> -->
						</tr>

					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default btn-lg" data-dismiss="modal">닫기</button>
					<button type="button" id ="createRoom" class ="btn btn-default btn-lg">방 만들기</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>