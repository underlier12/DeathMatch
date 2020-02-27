<%--
  Created by IntelliJ IDEA.
  User: hogu8
  Date: 2020-02-24
  Time: 오후 3:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원 가입</title>
</head>
<!-- BootStrap 적용 -->
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="/css/bootstrap.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<body>

<div id="joinMember">
    <div style="margin-top :80px; text-align : center">
        <div>
            <p>Member Join</p>
        </div>
    </div>

    <form action ="join" method ="post">
        <div>
            <div class="col-md-10 col-md-offset-1">
                <table class="table">
                    <tr>
                        <th>이메일</th>
                        <td><input type="email" name="userEmail" id="userEmail"></td>
                    </tr>
                    <tr>
                        <th>패스워드</th>
                        <td><input type="password" name="pw" id="pw"></td>
                    </tr>
                    <tr>
                        <th>이름</th>
                        <td><input type="text" name="name" id="name"></td>
                    </tr>
                    <tr>
                        <th>Phone</th>
                        <td><input type="text" name="phone" id="phone"></td>
                    </tr>
                    <td>
                        <input type="submit" value = "회원 가입">
                    </td>
                </table>
            </div>
        </div>
    </form>
</div>

</body>
</html>
