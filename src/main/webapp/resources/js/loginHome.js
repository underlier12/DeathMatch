/**
 *  loginForm .js
 *  
 */
	
	$(document).ready(function() {
		var loginForm = $("#loginProc");
		
		$("#loginBtn").click(function(e) {
			if ($('input[name="userEmail"]').val() == ""|| $('input[name="userEmail"]').val() == null) {
					alert("ID를 입력해주세요");
					return false;
			}
			if ($('input[name="pw"]').val() == "" || $('input[name="pw"]').val() == null) {
					alert("PW를 입력해주세요");
					return false;
			}
			loginForm.submit();
		});
		
		
		$("#regiBtn").click(function(e){
			$("#registerModal").modal();
		});
		
		
		$("#join").click(function(e){
			
			var userEmail = $('#userEmail').val();
			var userPw = $('#pw').val();
			var userName = $('#userName').val();
			var phone = $('#phone').val();
			
			var userInfo = {
					userEmail : userEmail,
					pw : userPw,
					name : userName,
					phone : phone
			};
			
			
			if(!userEmail){
				alert("이메일을 입력해 주세요");
				$('#userEmail').focus();
				return false;
			}else if(!userPw){
				alert("비밀번호를 입력해 주세요");
				$('#pw').focus();
				return false;
			}else if(!userName){
				alert("이름을 입력해주세요");
				$('#name').focus();
				return false;
			}else if(!phone){
				alert("핸드폰 번호를 입력해주세요");
				$('#phone').focus();
				return false;
			} 
			
			var joinCheck = confirm('가입하시겠습니까?');
			
			if(joinCheck){
				registerMember(userInfo);
				alert('가입 완료!');
				$('#registerModal').modal('hide');
				
			}else{
				return false;
			}
			
		});
		
		
		function registerMember(userInfo){
			
			$.ajax({
				type : 'post',
				url : '/genious/user/join',
				data : JSON.stringify(userInfo),
				contentType : 'application/json; charset=utf-8',
				success : function(result){
					console.log(result)
				}
			})
		}
	});
	
	