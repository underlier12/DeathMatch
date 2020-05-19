$(function(){
	
	var actionForm = $("#actionForm");
	var searchForm = $("#searchForm");
	var currentUser = $("#currentUser").val();
	var auth = $("#auth").val();
	
	console.log("CurrentUser: " + currentUser);
	
	$("#writeBtn").click(function(){
		$(location).attr("href","/suggestion/registration");
	});
	
	$("#playBtn").click(function(){
		$(location).attr("href","/rooms");
	});
	
	$(".listBtn").click(function(){
		$(location).attr("href","/suggestion");
	});
	
	$('.paginate_button a').on("click",function(e){
		e.preventDefault();
		
		var targetPage = $(this).attr("href");
		
		var actionForm = $("#actionForm");
		
		e.preventDefault();
		actionForm.find('input[name=page]').val($(this).attr('href'));
		actionForm.submit();
	});
	
	$('.title').on("click",function(e){
		// 제목 클릭시 페이지 번호와  게시글 갯수를 전송한다 
		e.preventDefault();
		
		var actionForm = $("#actionForm");
		var bno = $(this).find('input[name=bno]').val();
		
		console.log("bno " + bno);
		actionForm.attr("action","/suggestion/content");
		actionForm.append("<input type='hidden' name='bno' value='"+ bno +"'>");
		
		var pw = $(this).attr("href");
		console.log("pw : " + pw)
		
		var userId = $(this).find('input[name=userId]').val();
		console.log("userId : " + userId);
		
		if(pw == 1){
			if(currentUser == userId || auth == 99 ){
				actionForm.submit();
			}else{
				alert("비밀글은 작성자만 조회 할 수 있습니다");
				return false;
			}
		}else{
			actionForm.submit();
		}
	});
	
	$('#searchForm button').on('click', function(e) {
		
		if(!searchForm.find('input[name=keyword]').val()) {
			$(location).attr("href","/suggestion");
		}
		
		// 페이지번호 1로 초기화
		searchForm.find('input[name=pageNum]').val("1");
		e.preventDefault();
		
		searchForm.submit();
	});
})