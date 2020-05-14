$(function(){
	
	var actionForm = $("#actionForm");
	
	$("#writeBtn").click(function(){
		$(location).attr("href","/suggestion/registration");
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
		actionForm.append("<input type='hidden' name='bno' value='"+$(this).attr("href")+"'>");
		actionForm.submit();
		
	});
})