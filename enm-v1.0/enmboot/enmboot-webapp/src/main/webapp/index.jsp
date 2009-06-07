<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns:mcs="http://www.rtdata.cn/mcs">
	<head>
		<title>com.lineboom.enm</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="com.eboom365.estore">
		<meta http-equiv="description" content="com.eboom365.estore">
	</head>
	<body bgcolor=#EEEEEE>
		<%session.setAttribute("RC3UserInfo","{'sex':'1','loginIp':'192.168.32.28','userId':9999999999,'name':'测试员','browserEdition':'IE6','organizeId':900,'stationId':41,'computerName':'192.168.32.28','departmentId':63,'departmentName':'测试','loginCode':'admin'}"); %>
     	<%session.setAttribute("COMPANY_INFO", "{id:900,name:\"大唐安徽发电有限公司\",alias:\"安徽分公司\",value:\"安徽公司\"}");%>
		<jsp:forward page="/boot/index.jsp"></jsp:forward>
	</body>
</html>