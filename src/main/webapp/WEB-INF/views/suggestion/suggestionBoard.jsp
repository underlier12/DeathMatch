<%-- <%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
response.setHeader("Cache-Control","no-store");  
response.setHeader("Pragma","no-cache");  
response.setDateHeader("Expires",0);  
if (request.getProtocol().equals("HTTP/1.1"))
        response.setHeader("Cache-Control", "no-cache");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모서리</title>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="/css/bootstrap.css">
<link rel="stylesheet" href="/css/qnaRegist.css">
</head>
<body>
	<%@ include file="../includes/sidebar.jsp"%>

	<script src="/ckeditor/ckeditor.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			console.log('product_code : ${product_code}');
			console.log('pageNum : ' + '${cri.pageNum}');
			
			// 등록 버튼
			$('.reg_btn').on('click', function() {
				
				if(!'${user.id}') {
					alert("로그인 후 이용해주세요.");
					return false;
				}
				
				var registForm = $('#registForm');
				
				var title = $('.title').val();
				var content = CKEDITOR.instances['content'].getData();
				
				if(!title) {
					alert("제목을 입력하세요.");
					$('.title').focus();
					return false;
				} else if(!content) {
					alert("내용을 입력하세요.");
					$('.content').focus();
					return false;
				}
				
				var c = confirm("글을 등록하시겠습니까?");
				if(c) {
					registForm.submit();
				} else{
					return false;
				}
				
			});
		});
	</script>

	<div class="container" style="margin-left: 22%;">
		<!-- Qna Regist Start -->
		<div class="row">
			<div class="col-md-10 col-md-offset-1 reg_qna_header">
				<p>
					Q&A<small> | 상품 Q&A입니다.</small>
				<hr>
			</div>

			<div class="col-md-10 col-md-offset-1">
				<form id="registForm" action="/qna/qnaRegist" method="post">
					<input type="hidden" name="member_id"
						value='<c:out value="${user.id }" />'> <input
						type="hidden" name="product_code"
						value='<c:out value="${product_code }" />'> <input
						type="hidden" name="pageNum" value="${cri.pageNum }"> <input
						type="hidden" name="amount" value="${cri.amount }"> <input
						type="hidden" name="type" value="${cri.type }"> <input
						type="hidden" name="keyword" value="${cri.keyword }">
					<table class="table table-bordered reg_qna_table">
						<colgroup>
							<col width="15%">
							<col width="85%">
						</colgroup>
						<tr>
							<td>제목</td>
							<td><input type="text" class="title" name="title"
								autocomplete="off"></td>
						</tr>
						<tr>
							<td>내용</td>
							<td><textarea class="content" name="content" cols="110"></textarea>
							</td>
						</tr>
						<tr>
							<td>비밀글설정</td>
							<td><input type="radio" id="public" name="secret" value="0"
								checked> <label for="public">공개글</label>&nbsp; <input
								type="radio" id="secret" name="secret" value="1"> <label
								for="secret">비밀글</label></td>
						</tr>
					</table>
				</form>
			</div>

			<div class="col-md-10 col-md-offset-1 reg_qna_footer">
				<button type="button" class="btn btn-default btn-sm reg_btn">등록</button>
				<button type="button" class="btn btn-default btn-sm cancel_btn"
					onclick="history.back()">취소</button>
			</div>
		</div>
		<!-- Qna Regist End -->

		<%@ include file="../includes/footer.jsp"%>

	</div>

	<script type="text/javascript">
		var ckeditor_config = {
			resize_enableb : false,
			enterMode : CKEDITOR.ENTER_BR,
			shiftEnterMode : CKEDITOR.ENTER_P,
			filebrowserUploadUrl : "/admin/goods/ckUpload"
		};
	
		CKEDITOR.replace("content", ckeditor_config);
	</script>

</body>
</html> --%>