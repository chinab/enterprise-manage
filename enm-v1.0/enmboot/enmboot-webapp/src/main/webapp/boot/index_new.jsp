<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en" xmlns:ext="http://www.rtdata.cn/rc3">

<head>
  <title>上实RP3电力数据视窗系统应用软件V2.0</title>
	<s:include value="/common/common.jsp"></s:include>    
  	<link rel="stylesheet" type="text/css" href="/ajaxlib/ext/resources/css/xtheme-gray.css"></link>
    <link rel="stylesheet" type="text/css" href="/css/main.css"></link>
	<link rel="stylesheet" type="text/css" href="/css/style.css"></link>
	<link rel="shortcut icon" href="/images/favicon.ico" />
	<link rel="icon" href="/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="/css/welcome.css"/>
	<script type="text/javascript" src="/jscript/common/Ext.ux.VTypes.js"></script>
	<script type="text/javascript" src="/jscript/TabCloseMenu.js"></script>
	<script type="text/javascript">
		var RC3UserInfo = ${RC3UserInfo}; 
	   	<%session.setAttribute("COMPANY_INFO", "{id:900,name:\"HAB发电有限公司\",alias:\"HAB公司\"}");%>
    	var COMPANY_INFO = ${COMPANY_INFO};
    </script>
    <script type="text/javascript" src="/jscript/portal/Ext.ux.Portal.js"></script>
    <script type="text/javascript" src="/jscript/portal/Ext.ux.MaximizeTool.js"></script>
    <script type="text/javascript" src="/jscript/portal/Ext.ux.ManagedIFrame.js"></script>
    <link rel="stylesheet" type="text/css" href="/jscript/portal/portal.css" />
	<script type="text/javascript" src="/jscript/portal/Ext.ux.PortalManager.js"></script>
	<script type="text/javascript" src="/jscript/portal/portlet-sample-grid.js"></script>
	<script type="text/javascript" src="/jscript/portal/Ext.ux.PortalState.js"></script>
	<script type="text/javascript" src="/jscript/portal/Ext.ux.ManagedIFrame.js"></script>
	<script type="text/javascript" src="/boot/index_new.js"></script>
	
	<style type="text/css">
		html, body {
	        font:normal 12px verdana;
	        margin:0;
	        padding:0;
	        border:0 none;
	        overflow:hidden;
	        height:100%;
	    }
    </style>
</head>
<body>
 </body>
</html>