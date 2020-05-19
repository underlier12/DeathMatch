$(function(){
	
	$('.paginate_button a').on("click",function(e){
		e.preventDefault();
		
		var targetPage = $(this).attr("href");
		var actionForm = $("#actionForm");
		
		e.preventDefault();
		actionForm.find('input[name=page]').val($(this).attr('href'));
		actionForm.submit();
	});
	
	$('.battleInfo').addClass(function(){
		return $(this).find('.wl').html() == "WIN" ? "label-win" : "label-lose";
	});
	
	$('#btnBack').click(function(){
    	location.href='../rooms';
    });
	
})