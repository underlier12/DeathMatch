$(function(){
	
	var contentForm = $("#contentForm");
	
	var bno;
	
	$("#modifyBtn").click(function(){

		contentForm.attr("action","/suggestion/post-edit");
		contentForm.submit();
	});
	
	$("#goListBtn").click(function(){
		
		contentForm.attr("action","/suggestion");
		contentForm.submit();
	});
	
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
	})
	
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
		$.getJSON(url,function(jsonData){
			console.log(jsonData);
			var list = "";
			$(jsonData).each(function(){
				var date = new Data(this.regdate);
				console.log("댓글 번호 " + this.rno);
				list += '<div class="reply_item">'
  	  		  		+ '<pre>'
  	  		  		+ '<input type="hidden" id="rno" value="' + this.rno + '" /><br>'
  	  		  		+ '<input type="hidden" id="userId" value="' + this.userID + '" /><br>'
  	  		  		+ this.userId
  	  		  		+ '&nbsp;&nbsp;'
  	  		  		+ '<input type="text" id="reply_content" value="' + this.content + '" />'
  	  		  		+ '&nbsp;&nbsp;' 
  	  		  		+ date // 변경한 부분
  	  		  		+ '&nbsp;&nbsp;' 
  	  		  		+ '<button class="btn_update" type="button">수정</button>'
  	  		  		+ '&nbsp;'
  	  		  		+ '<button class="btn_delete" type="button">삭제</button>'
  	  		  		+ '</pre>' + '</div>';
			});
			$("#getReply").html(list);
		})
	}
	
})