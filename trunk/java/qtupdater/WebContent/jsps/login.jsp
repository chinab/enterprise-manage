<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="css/files.css" />
<link type="text/css" rel="stylesheet" href="css/uploadify.css" />
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/files.js"></script>
<script type="text/javascript" src="js/jquery.uploadify.min.js"></script>
<title>发布文件列表</title>
</head>
<body>
	<span style="color: red;">${msg }</span>
	<form action="LoginServlet" method="post">
		用户名:<input type="text" name="username"><br> 密　码:<input type="password" name="password"><br> <input type="submit" value="登录">
	</form>
</body>
</html>