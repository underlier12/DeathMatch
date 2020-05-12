$(function(){
	
	var modifyForm = $("#modifyForm");
	
	$("#modifyBtn").click(function(){
		
		var title = $('input[name=title]').val();
		var content = $('.content').val();
		
		console.log("Title " + title);
		console.log("Content " + content);
		
		if(title == "" || title == null){
			alert("제목을 입력해 주세요");
			return false;
		}
		if(content == "" || content == null){
			alert("내용을 입력해 주세요");
			return false;
		}
		
		modifyForm.submit();
	})
	
	$("#deleteBtn").click(function(){
		
		var c = confirm("건의글을 삭제하시겠습니까?");
		
		if(c){
			modifyForm.attr("action","/suggestion/delete");
			modifyForm.attr("method","post");
			modifyForm.submit();
		}else{
			return false;
		}
	});
})