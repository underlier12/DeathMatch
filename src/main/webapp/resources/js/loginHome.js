$(function(){
	
		var loginForm = $("#loginProc");
		var idflag = 0;
		
		$("#regiBtn").click(function(e){
			$("#registerModal").modal();
		});
		
		$("#findPw").click(function(e){
			$("#findModal").modal();
		});
		
		$("#findPwCheckEmail").click(function(e){
			var email1 = $('#findPwEmail1').val();
			var email2 = $('#findPwEmail2').val();
			if(!email1 || !email2){
				alert("이메일을 입력해 주세요");
				$('#userEmail1').focus();
				return false;
			}
			var userCheckEmail = email1 + '@' +email2;
			console.log(userCheckEmail);
			var userEmailInfo ={
					userEmail : userCheckEmail
			};
			checkFindPwEmail(userEmailInfo);
		})
		
		$("#findUserPw").click(function(){
			var email1 = $('#findPwEmail1').val();
			var email2 = $('#findPwEmail2').val();
			var userCheckEmail = email1 + '@' +email2;
			console.log(userCheckEmail);
			
			var userInfo = {
					userEmail : userCheckEmail,
			};
			
			if(!email1 || !email2){
				alert("이메일을 입력해 주세요");
				$('#findPwEmail1').focus();
				return false;
			}else if(idflag == 1){
				alert("ID 중복체크가 필요합니다");
				return false;
			}
			var findPwCheck = confirm('이메일로 임시 비밀번호가 전송됩니다');
			
			if(findPwCheck){
				findPw(userInfo);
				alert('전송 완료!');
				$('#findModal').modal('hide');
			}
		})
		
		$("#checkEmail").click(function(e){
			var email1 = $('#userEmail1').val();
			var email2 = $('#userEmail2').val();
			if(!email1 || !email2){
				alert("이메일을 입력해 주세요");
				$('#userEmail1').focus();
				return false;
			}
			var userCheckEmail = email1 + '@' +email2;
			console.log(userCheckEmail);
			var userEmailInfo ={
					userEmail : userCheckEmail
			};
			console.log(userEmailInfo);
			checkUserEmail(userEmailInfo);
		})
		
		 // 이메일 포커스 아웃될 때
        $("#userEmail").blur(function() {
            var userEmail = $("#userEmail").val();
         
            if(userEmail.length < 4) { // 이메일이 4글자 보다 짧을 경우
                $("#email_check_text").text('아이디는 영문소문자 또는 숫자 4~12자로 입력해 주세요.');
            } else if(chkEmail(userEmail)) { // 아이디 유효성 검사에 문제가 있을 경우
                $("#email_check_text").text('공백/특수문자가 사용된 이메일은 사용 불가능합니다');
            } else if(userEmail == '' || !(chkEmail(userEmail))){
				alert("올바른 이메일 형식이 아닙니다");
				$("#email_check_text").text('이미 사용중인 이메일 입니다');
			} 
        });
		
		
		
		$("#join").click(function(e){
			
			//이메일
			var email1 = $('#userEmail1').val();
			var email2 = $('#userEmail2').val();
			var userEmail = email1 + '@' +email2;
			//패스워드
			var userPw = $('#pw').val();
			//패스워드 확인
			var checkUserPw = $('#checkPw').val();
			//이름
			var userName = $('#userName').val();
			
			var userInfo = {
					userEmail : userEmail,
					pw : userPw,
					name : userName,
			};
				
			if(!email1 || !email2){
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
			}else if(idflag == 0){
				alert("Email 중복체크가 필요합니다");
				$('#userEmail').focus();
				return false;
			}else if(userEmail == '' || !(chkEmail(userEmail))){
				alert("올바른 이메일 형식이 아닙니다");
				$('#userEmail').focus();
				return false;
			}else if(checkName(userName)){
				alert("올바른 닉네임 형식이 아닙니다");
				$('#name').focus();
				return false;
			}else if(checkUserPw != userPw){
				alert("비밀번호와 비밀번호 확인이 일치하지 않습니다");
				$('#pw').focus();
				return false;
			}else if(!checkUserPw){
				alert("비밀번호 확인을 입력해 주세요");
				$('#checkUserPw').focus();
				return false;
			}
			
			var joinCheck = confirm('가입하시겠습니까?');
			
			if(joinCheck){
				registerMember(userInfo);
				alert('가입 완료!');
				$('#registerModal').modal('hide');
				
			}
			
		});
		function checkUserEmail(userInfo){
			$.ajax({
				type : 'post',
				url : '/genious/user/findPw',
				data : JSON.stringify(userInfo),
				contentType : 'application/json; charset=utf-8',
				success : function(result){
					console.log(result)
				}
			})
		}
		
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
		
        function checkUserEmail(userEmailInfo){
            $.ajax({
                type : 'post',
                url : '/genious/user/checkEmail',
                data : JSON.stringify(userEmailInfo),
                dataType:"json",
                contentType : 'application/json; charset=utf-8',
                success : function(data){
                	console.log(data);
                    if(data == 1){
                    	alert("이미 있는 이메일 입니다");
                    	idflag = 0;
                    	return false;
                    }else {
                    	alert("사용 가능한 아이디 입니다");
                    	idflag = 1;
                    	return true;
                    }
                }
            })
        }
        
        function checkFindPwEmail(userEmailInfo){
            $.ajax({
                type : 'post',
                url : '/genious/user/checkEmail',
                data : JSON.stringify(userEmailInfo),
                dataType:"json",
                contentType : 'application/json; charset=utf-8',
                success : function(data){
                	console.log(data);
                    if(data == 1){
                    	alert("존재하는 이메일 입니다! PW 확인시 이메일로 보내드리겠습니다");
                    	idflag = 0;
                    	return false;
                    }else {
                    	alert("존재하지 않는 이메일 입니다.");
                    	idflag = 1;
                    	return true;
                    }
                }
            })
        }
        
        function findPw(userInfo){
        	$.ajax({
				type : 'post',
				url : '/genious/user/findPw',
				data : JSON.stringify(userInfo),
				contentType : 'application/json; charset=utf-8',
				success : function(result){
					console.log(result)
				}
			})
        }
        
        //이메일 유효성 검사
        function chkEmail(str) {
            var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

            if (regExp.test(str)){
            	return true;
            }else{
            	return false;
            }
        }
        
        // 이름 유효성 검사
        function checkName(userName) {
            var pattern1 = /\s/; // 공백 여부
            var pattern2 = /[[~!@#$%^&*()_+|<>?:{}]/; // 특수문자 여부
            var pattern3 = /[0-9]/; //숫자 여부
            
            // 2가지 중 한 가지라도 포함 되어 있으면 true
            if(pattern1.test(userName) || pattern2.test(userName) || pattern3.test(userName)) {
                return true;
            } else {
                return false;
            }
        }
        
        //셀렉트 박스에서 이메일 선택시 email2에 자동 기입
        function changeEmail() {
            var select_email = $("#select_email").val();
            $("#userEmail2").val(select_email);
        }
     
});

	
	