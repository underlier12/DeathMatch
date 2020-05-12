$(function(){
	
	var contentForm = $("#contentForm");
	
	$("#modifyBtn").click(function(){
		contentForm.attr("action","/suggestion/post-edit");
		contentForm.submit();
	});
	
	
	/*$("#answerBtn").click(function(){
		alert("구현이 필요함");
	})*/
	
})