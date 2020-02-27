<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1> 채팅방 목록 </h1>
	<ul>
		<c:forEach var = "room" items="${rooms }">
	       <a href="rooms/<c:out value="${room.roomId}"/>"><c:out value = "${room.name}"/></a><p>
	    </c:forEach>
	</ul>	
</body>
</html>