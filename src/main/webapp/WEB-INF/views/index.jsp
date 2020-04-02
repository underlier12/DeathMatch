<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="includes/meta.jsp" %>
    <title>Genius Death Match</title>
    <%@ include file="includes/header.jsp" %>
    <link href="/css/index.css" rel="stylesheet">
</head>

<body>	
    <div id="index">
    	<div class="flex-box">
       		<img src="/resources/images/geniusLogo.png">
    	</div>
    	<div class="flex-box">
       		<img src="/resources/images/deathmatchLogo.png">
    	</div>
    	<div class="flex-box">
	        <input id="startBtn" type="button" value="Game Start" 
	        		onclick = "location.href='user/loginHome'"/>    	
    	</div>
	    <%@ include file="includes/footer.jsp" %>
    </div>
    
</body>

