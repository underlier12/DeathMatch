$(function(){
	
	var contentForm = $("#contentForm");
	
	$("#modifyBtn").click(function(){
		contentForm.attr("action","/suggestion/post-edit");
		contentForm.submit();
	});
	
	$("#goListBtn").click(function(){
		
		//contentForm.find($("#bno")).remove();
		
		contentForm.attr("action","/suggestion/suggestionboard");
		contentForm.submit();
	});
	
	$("#deleteBtn").click(function(){
		
		var c = confirm("건의글을 삭제하시겠습니까?");
		
		if(c){
			contentForm.attr("action","/suggestion/delete");
			contentForm.attr("method","post");
			contentForm.submit();
		}else{
			return false;
		}
	});
	
})