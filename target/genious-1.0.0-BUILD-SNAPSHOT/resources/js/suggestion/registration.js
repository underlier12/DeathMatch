$(function(){
	
	var registerForm = $("#registForm");
	
	$("#regiBtn").click(function(){
		
		var title = $('input[name=title]').val();
		var content = $('.content').val();
		
		console.log("title " + title);
		console.log("content " + content);
		
		if(title == "" || title == null){
			alert("제목을 입력해 주세요");
			return false;
		}
		if(content == "" || content == null){
			alert("내용을 입력해 주세요");
			return false;
		}
		
		registerForm.submit();
			
	})
})