<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1> 요청이 거부 되었습니다</h1>
<h2><c:out value="${SPRING_SECURITY_403_EXCEPTION.getMessage()}"/> </h2>
<h2><c:out value ="${msg}"/></h2>
</body>
</html>