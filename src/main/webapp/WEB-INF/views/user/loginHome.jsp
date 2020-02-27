<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <!-- 네이버 아이디 인증 -->
    <script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.0.js" charset="utf-8"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>데스 매치</title>
</head>
<!-- BootStrap 적용 -->
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="/css/bootstrap.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<body>
<div style="margin-top: 80px">
    <div class="col-md-10 col-md-offset-1" style="text-align: center">
        <p>Death Match</p>
    </div>
</div>

<form action="/user/loginPost" method="post" id="loginProc">
    <div class="row loginForm-row" style="margin-top: 150px;">
        <div class="col-md-4 col-md-offset-4">
            <input type="email" class="form-control" name="userEmail" placeholder="아이디">
        </div>
        <div class="col-md-4 col-md-offset-4">
            <input type="password" class="form-control" name="pw" placeholder="비밀번호">
        </div>
        <div class="col-md-4 col-md-offset-4">
            <c:if test ="${empty login}">
            <button type="submit" id="loginBtn" class="btn btn-default btn-block login-btn">로그인</button>
            </c:if>
        </div>
        <div class="col-md-4 col-md-offset-4">
            <div class="checkbox">
                <label><input type="checkbox" id="checkId"> <span>아이디 저장</span></label>
            </div>
        </div>
        <div class="col-md-4 col-md-offset-4" style="text-align: center; margin-bottom: 40px;">
                <span><a href="join" id ="join">회원가입</a>
                      <a href="findId">아이디 찾기</a>
                      <a href="findPw">비밀번호 찾기</a></span>
        </div>
        <div class="col-md-5 col-md-offset-4" style="margin-bottom: 50px;">
            <div>
                <a href="https://kauth.kakao.com/oauth/authorize?client_id=24ff4b9dce4ffc7531bff6ac4abb6bc2&redirect_uri=http://localhost:8003/user/kakaoLogin&response_type=code">
                    <img src="/images/btn_kakao_login.gif">
                </a>
            </div>
            <div id="naver_id_login" style="text-align:center"><a href="${url}"/>
                <img src="/images/btn_naver_login.gif">
            </div>
            <%--<div id ="naverIdLogin">--%>
                <%--<img src="/images/btn_naver_login.gif">--%>
        </div>
    </div>
</form>

<form action ="/user/logout" method ="get">
    <div>
        <c:if test ="${!empty login}">
            <button type="submit" id="loginBtn" class="btn btn-default btn-block login-btn">로그아웃</button>
        </c:if>
    </div>
</form>

</body>
<script type ="text/javascript">
    /*var naverLogin = new naver.LoginWithNaverId(
        {
            clientId: "U7v56brPWZpVGMdWjoaW",
            callbackUrl: "http://localhost:8003/gameHome",
            isPopup: false, /!* 팝업을 통한 연동처리 여부 *!/
            loginButton: {color: "green", type: 3, height: 30 , width : 170} /!* 로그인 버튼의 타입을 지정 *!/
        }
    );
    naverLogin.init();

    naverLogin.getLoginStatus(function (status) {
        if (status) {
            var email = naverLogin.user.getEmail();
            var name = naverLogin.user.getNickName();
            var profileImage = naverLogin.user.getProfileImage();
            var birthday = naverLogin.user.getBirthday();
            var uniqId = naverLogin.user.getId();
            var age = naverLogin.user.getAge();
        } else {
            console.log("AccessToken이 올바르지 않습니다.");
        }
    });*/
</script>
</html>