<%-- <%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>모서리</title>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="/css/bootstrap.css">
<link rel="stylesheet" href="/css/qnaList.css">
</head>
<body>
	<%@ include file="../includes/sidebar.jsp"%>
	
	<script type="text/javascript">
		$(document).ready(function() {
			// 접속중인 사용자 아이디
			var member_id = '${user.id}';
			// 작성자의 아이디 블라인드 처리
			var name = new String();
			
			// 페이지 클릭 이벤트 처리
			var actionForm = $('#actionForm');
			
			$('.paginate_button a').on('click', function(e) {
				e.preventDefault();
				
				actionForm.find('input[name=pageNum]').val($(this).attr('href'));
				actionForm.submit();
			});
			
			// 검색 처리
			var searchForm = $('#searchForm');
			
			$('#searchForm button').on('click', function(e) {
				
				if(!searchForm.find('input[name=keyword]').val()) {
					alert("키워드를 입력하세요.");
					return false;
				}
				
				// 페이지번호 1로 초기화
				searchForm.find('input[name=pageNum]').val("1");
				e.preventDefault();
				
				searchForm.submit();
			});
			
			// 글쓰기 클릭
			$('.qna_reg_btn').on('click', function() {
				var actionForm = $('#actionForm');
				
				actionForm.attr('action', '/qna/qnaRegist');
				actionForm.submit();
			});
			
			// 글 제목 클릭
			$('.title').on('click', function(e) {
				// a 태그 막기
				e.preventDefault();
				
				// 글 조회를 위한 번호
				var no = $(this).find('input[name=no]').val();
				
				// cri가 담겨있는 form
				var actionForm = $('#actionForm');
				actionForm.attr('action', '/qna/qnaGet');
				actionForm.append('<input type="hidden" name="no" value="' + no + '">');
				
				// 비밀글 여부
				var secret = $(this).attr('href');
				
				// 작성자
				var writer = $(this).find('input[name=writer]').val();
				
				if(secret == '1') { // 비밀글
					if(member_id == writer) { // 작성자와 접속중인 사용자가 동일한 경우 
						actionForm.submit(); // 문의글 조회 이동
					} else { // 작성자가 아닌 경우
						alert("비밀글은 작성자만 조회할 수 있습니다.");
						return false;
					}
				} else { // 공개글
					actionForm.submit(); // 문의글 조회 이동
				 }
				
			});
			
			var result = '<c:out value="${result}" />';
			
			checkAlert(result);
			
			history.replaceState({}, null, null);
			
			function checkAlert(result) {
				
				if(result === '' || history.state) {
					return;
				}
				
				if(result == 'success_delete') {
					alert('해당 글이 삭제되었습니다.');
				} else if(result == 'success_modify') {
					alert('해당 글 수정이 완료되었습니다.');
				} else if(result == 'success_answerRegist') {
					alert('답변이 등록되었습니다.');
				}
				
			}
			
		}); // end document
	</script>
	
	<div class="container" style="margin-left: 22%;">
		<!-- QnA Start -->
		<div class="row">
			<div class="col-md-10 col-md-offset-1 qnaLabel-row">
				<p>Q&A</p>
			</div>
		</div>

		<div class="row qna_body">
			<div class="col-md-10 col-md-offset-1">
				<table class="table qna-board">
					<colgroup>
						<col width= 50px;> <!-- NO -->
						<col width= 250px;> <!-- TITLE -->
						<col width= 80px;> <!-- NAME -->
						<col width= 80px;> <!-- DATE -->
						<col width= 50px;> <!-- HIT -->
					</colgroup>
					<thead>
						<tr>
							<td>NO</td>
							<td>TITLE</td>
							<td>WRITER</td>
							<td>DATE</td>
							<td>HIT</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="qna" items="${qnaList }">
						<tr>
							<!-- NO -->
							<td><c:out value="${qna.no }" /></td>
							<!-- TITLE -->
							<td class="qna_title">
								<a class="title" href="${qna.secret }">
								<c:forEach begin="1" end="${qna.depth }" >
									<c:if test="${qna.depth > 0 }">
										&nbsp;&nbsp;
									</c:if>
								</c:forEach>
								<c:if test="${qna.depth > 0 }">
									<img src="/images/re.gif">
								</c:if>
								<c:if test="${qna.secret ne 1 }">
									<c:out value="${qna.title }" />&nbsp;
								</c:if>
								<c:if test="${qna.secret eq 1 }">
									비밀글입니다.
									<img class="secret_img" src="/images/secret.jpg">
								</c:if>
									<input type="hidden" name="writer" value="${qna.member_id }">
									<input type="hidden" name="no" value="${qna.no }">
								</a>
							</td>
							<!-- WRITER -->
							<td class="qna_writer"><c:out value="${fn:substring(qna.member_id, 0, 2).concat('*****') }" /></td>
							<!-- DATE -->
							<td><c:out value="${qna.reg_date }" /></td>
							<!-- HIT -->
							<td><c:out value="${qna.hit }" /></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
			<!-- 검색 처리 -->
			<div class="col-md-5 col-md-offset-1 search_area">
				<form id="searchForm" action="/qna/qnaList" method="get">
					<select class="form-control search_type" name="type">
						<option value="T" <c:out value="${pageMaker.cri.type eq 'T' ? 'selected' : '' }" />>제목</option>
						<option value="C" <c:out value="${pageMaker.cri.type eq 'C' ? 'selected' : '' }" />>내용</option>
						<option value="TC" <c:out value="${pageMaker.cri.type eq 'TC' ? 'selected' : '' }" />>제목+내용</option>
						<option value="W" <c:out value="${pageMaker.cri.type eq 'W' ? 'selected' : '' }" />>작성자</option>
					</select> 
					<input type="text" class="form-control search_keyword" name="keyword" 
						value='<c:out value="${pageMaker.cri.keyword }" />' autocomplete="off" placeholder="검색어">
					<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }">
					<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
					<button class="btn btn-default btn-sm search_btn">검색</button>
				</form>
			</div>
			<div class="col-md-5 reg_btn_area">
				<c:if test="${user ne null }">
					<button type="button" class="btn btn-default btn-sm qna_reg_btn">글쓰기</button>
				</c:if>
			</div>
			
			<!-- 페이징 처리 -->
			<div class="col-md-10 col-md-offset-1 pagination-div">
				<nav>
					<ul class="pagination">
						<c:if test="${pageMaker.prev}">
							<li class="paginate_button previous"><a href="${pageMaker.startPage-1 }">&laquo;</a></li>
						</c:if>

						<c:forEach var="num" begin="${pageMaker.startPage }" end="${pageMaker.endPage }">
							<li class="paginate_button ${pageMaker.cri.pageNum == num ? 'active' : '' }">
								<a href="${num }">${num }</a>
							</li>
						</c:forEach>

						<c:if test="${pageMaker.next }">
							<li class="paginate_button next"><a href="${pageMaker.endPage+1 }">&raquo;</a></li>
						</c:if>
					</ul>
				</nav>
			</div>
			
			<form id="actionForm" action="/qna/qnaList" method="get">
				<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }">
				<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
				<input type="hidden" name="type" value="${pageMaker.cri.type }">
				<input type="hidden" name="keyword" value="${pageMaker.cri.keyword }">
			</form>
			
		</div>
		<!-- QnA End -->
		
		<%@ include file="../includes/footer.jsp" %>
	
	</div>

</body>
</html> --%>