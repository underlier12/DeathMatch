$(function(){
	
	var contentForm = $("#contentForm");
	
	var bno =  $('input[name="bno"]').val();
	var userId = $('input[name="userId"]').val();
	
	getAllReplies();
	
	$("#modifyBtn").click(function(){

		contentForm.attr("action","/suggestion/post-edit");
		contentForm.submit();
	});
	
	$("#goListBtn").click(function(){
		
		contentForm.attr("action","/suggestion");
		contentForm.submit();
	});
	
	$("#answerBtn").click(function(){
		contentForm.attr("action","/suggestion/registration/answer");
		contentForm.submit();
	})
	
	$("#deleteBtn").click(function(){
		
		var c = confirm("건의글을 삭제하시겠습니까?");
		
		if(c){
			
			contentForm.attr("action","/suggestion/deleteion");
			contentForm.attr("method","post");
			contentForm.submit();
		}else{
			return false;
		}
	});
	
	$("#writeReply").click(function(){
		
		bno = $('input[name="bno"]').val();
		var userId = $('input[name="userId"]').val();
		var content = $("#reply_content").val();
		
		console.log("replyContent" + content);
		console.log("userId : " + userId);
		console.log("bno " + bno);
		
		var replyData = {
			bno : bno,
			userId : userId,
			content : content
		};
		
		if(!content){
			alert("내용을 입력하세요");
			$('#reply_content').focus();
			return false;
		}
		
		var c = confirm('댓글을 등록하시겠습니까?');
		
		if(c){
			registerReply(replyData);
		}else{
			return false;
		}
	});
	
	function dateFormat(date) {
		var year = date.year;
		
		var month = date.monthValue;
		if(String(month).length < 2)
			month = '0' + month;
		
		var day = date.dayOfMonth;
		if(String(day).length < 2)
			day = '0' + day;
		
		var hour = date.hour;
		if(String(hour).length < 2)
			hour = '0' + hour;
		
		var minute = date.minute;
		if(String(minute).length < 2)
			minute = '0' + minute;
		
		var second = date.second; 
		if(String(second).length < 2)
			second = '0' + second;
			
		return [year, month, day].join('-') + ' ' + [hour, minute, second].join('');
	} 
	
	function registerReply(replyData){
		$.ajax({
			type : 'post',
			url :"/suggestion/registration/reply",
			data: JSON.stringify(replyData),
			contentType : 'application/json; charset=utf-8',
			success: function(result){
				if(result == "Success"){
					console.log("댓글 입력 성공");
					$("#reply_content").val("");
					getAllReplies();
				}
			}
		})
	}
	
	function getAllReplies(){
		var url = "/suggestion/reply/"+bno;
		$.getJSON(
	  	  		url,
	  	  		function(jsonData){
	  	  		 console.log(jsonData);
	  	  		 console.log(jsonData.bno);
	  	  		  var list = ''; // JSON 데이터에서 테이터를 꺼내 태그 + 데이터 형식으로 저장할 변수
	  	  		  $(jsonData).each( // jsonData를 사용하는 each 반복문
	  	  		    function(){
	  	  		  	console.log("댓글 번호 : " + this.rno); 
	  	  		  	
	  	  		  	var regdate = dateFormat(this.regdate);
	  	  		  	list += '<div class="reply_item">'
	  	  		  		+ '<pre>'
	  	  		  		+ '<input type="hidden" id="rno" value="' + this.rno + '" /><br>'
	  	  		  		+ '<input type="hidden" id="userId" value="' + this.userId + '" /><br>' // 변경한부분 id="reply_id"
	  	  		  		+ this.userId
	  	  		  		+ '&nbsp;&nbsp;'
	  	  		  		+ '<input type="text" id="content" value="' + this.content + '" />'
	  	  		  		+ '&nbsp;&nbsp;'
	  	  		  		+ regdate // 변경한 부분
	  	  		  		+ '&nbsp;&nbsp;' 
	  	  		  		if(userId == this.userId){
	  	  		  			list+=  '<button class="btn_delete" id="btn_delete type="button">삭제</button>'
	  	  		  		}
	  	  		  		list+='</pre>' + '</div>';
	  	  		  }); 
	  	  		  $('#getReply').html(list);
	  	  		} 
	  	  	); 
		};
		
		
	$("#getReply").on("click", '.reply_item .btn_delete',function(){
		var c = confirm("댓글을 삭제하시겠습니까?");
		var rno = $(this).prevAll("#rno").val();
		
		console.log("삭제 댓글: " + rno);
		if(c){
			$.ajax({
				type : 'post',
				url :"/suggestion/deletion/reply/" + rno,
				data: JSON.stringify({'rno':rno}),
				contentType : 'application/json;',
				success: function(result){
					if(result == "Success"){
						console.log("댓글 삭제 성공");
						getAllReplies();
					}
				}
			})
		}else{
			return false;
		}
	});
	
})