<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<title>后台管理</title>
<%--	<%--%>
<%--		String path = request.getContextPath();--%>
<%--		String basePath = request.getScheme() + "://" + request.getServerName() + ":" +--%>
<%--				request.getServerPort() + path + "/";--%>
<%--	%>--%>
<%--	<base href="<%=basePath%>">--%>
	<base href="http://119.91.22.246:8080/McDonalds_web/">

	<link type="text/css" rel="stylesheet" href="css/bootstrap.css"/>
</head>
<body>
<div class="container-fluid">

	


<jsp:include page="header.jsp"></jsp:include>

	<br><br>
	
	<div class="alert alert-success" role="alert">恭喜你! 登录成功了</div>
	
</div>	
</body>
</html>