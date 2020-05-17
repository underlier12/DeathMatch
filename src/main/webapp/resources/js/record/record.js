$(function(){
	
	$('.paginate_button a').on("click",function(e){
		e.preventDefault();
		
		var targetPage = $(this).attr("href");
		var actionForm = $("#actionForm");
		
		e.preventDefault();
		actionForm.find('input[name=page]').val($(this).attr('href'));
		actionForm.submit();
	});
	
})