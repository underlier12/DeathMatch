$(function(){
	
	var modifyForm = $("#modifyForm");
	
	$("#modifyBtn").click(function(){
		
		var title = $('input[name=title]').val();
		var content = $('.content').val();
		
		if(title == "" || title == null){
			alert("제목을 입력해 주세요");
			return false;
		}
		if(content == "" || content == null){
			alert("내용을 입력해 주세요");
			return false;
		}
		
		var c = confirm("건의글을 수정하시겠습니까?");
		if(c){
			modifyForm.submit();
		}else{
			return false;
		}
	})
	
	$("#deleteBtn").click(function(){
		
		var c = confirm("건의글을 삭제하시겠습니까?");
		
		if(c){
			//modifyForm.attr("action","/notice/delete");
			modifyForm.attr("action","/notice/deletion");
			modifyForm.attr("method","post");
			modifyForm.submit();
		}else{
			return false;
		}
	});
})