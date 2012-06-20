<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="css/products.css" />
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/products.js"></script>
<title>产品管理</title>
</head>
<body>
	<table>
		<thead>
			<tr class="tableTitle">
				<th colspan="4">产品列表</th>
			</tr>
			<tr>
				<th>产品名称</th>
				<th>产品信息</td>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${products }" var="product">
			<tr>
				<td class="productName">${product.name }</td>
				<td class="productInfo">${product.productInfo }</td>
				<td width="155">${product.createTime }</td>
				<td id="${product.id }" width="140"><a href="${product.name }.xml" target="_blank">xml</a> <a class="editButton" href="#">编辑</a> <a class="deleteButton" href="#">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<br>

	<a class="addButton" href="#">添加</a>
	<div class="addContent">
		<form action="ProductServlet" method="post">
			<input type="hidden" name="method" value="add"> 
			名称<br /> <input type="text" name="name"> <br /> 
			信息<br /> <textarea name="productInfo" rows="10" cols="35"></textarea>
			<br>
			<button type="submit">添加</button>
			<button type="button" id="closeAdd">关闭</button>
		</form>
	</div>
	<div class="editContent">
		<form action="ProductServlet" method="post">
			<input type="hidden" name="method" value="edit">
			<input id="editIdInput" type="hidden" name="id"> 
			名称<br /> <input id="editNameInput" type="text" name="name"> <br /> 
			信息<br /> <textarea id="editProductInfoInput" name="productInfo" rows="10" cols="35"></textarea>
			<br>
			<button type="submit">编辑</button>
			<button type="button" id="closeEdit">关闭</button>
		</form>
	</div>
</body>
</html>