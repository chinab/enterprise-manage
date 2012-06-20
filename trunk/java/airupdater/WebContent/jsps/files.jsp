<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="css/files.css" />
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/files.js"></script>
<title>发布文件列表</title>
</head>
<body>
	<table>
		<thead>
			<tr class="tableTitle">
				<th colspan="4">发布文件列表</th>
			</tr>
			<tr>
				<th>版本</th>
				<th>更新信息</td>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${fileGroups }" var="fileGroup">
			<tr class="productTr" title="${fileGroup.product.name }(${fileGroup.product.productInfo })">
				<td class="productName" colspan="3">${fileGroup.product.name }</td>
				<td id="${fileGroup.product.id }"><a href="${fileGroup.product.name }.xml" target="_blank">xml</a><a href="#" class="uploadButton">上传</a></td>
			</tr>
			<c:forEach items="${fileGroup.files }" var="file">
			<tr title="${file.version }">
				<td class="version">${file.version }</td>
				<td class="updateInfo">${file.updateInfo }</td>
				<td width="155">${file.createTime }</td>
				<td id="${file.id }" title="${fileGroup.product.name }_${file.version }" width="140">
					<a href="#" class="downloadButton" title="${fileGroup.product.name }_${file.version }.air下载">下载</a> 
					<a href="#" class="editButton"  title="${fileGroup.product.name }_${file.version }编辑">编辑</a> 
					<a href="#" class="deleteButton"  title="${fileGroup.product.name }_${file.version }删除">删除</a>
				</td>
			</tr>
			</c:forEach>
		</c:forEach>
		</tbody>
	</table>
	<br>
	<a href="ProductServlet?method=all" target="_blank">管理产品</a>
	<div class="uploadContent">
		<form action="FileServlet" method="post" enctype="multipart/form-data">
			<input type="hidden" name="method" value="upload"> 
			<input id="uploadProductIdInput" type="hidden" name="productId"> 
			文件<br/><input type="file" name="fileUpload"><br/>
			版本<br /> <input type="text" name="version"> <br /> 
			更新信息<br /> <textarea name="updateInfo" rows="10" cols="35"></textarea><br/>
			<button type="submit">上传</button>
			<button type="button" id="closeUpdate">关闭</button>
		</form>
	</div>
	<div class="editContent">
		<form action="FileServlet" method="post">
			<input type="hidden" name="method" value="edit"> 
			<input id="uploadIdInput" type="hidden" name="id">
			版本<br /> <input id="uploadVersionInput" type="text" name="version"> <br /> 
			更新信息<br /> <textarea id="uploadInfoInput" name="updateInfo" rows="10" cols="35"></textarea><br/>
			<button type="submit">修改</button>
			<button type="button" id="closeEdit">关闭</button>
		</form>
	</div>
</body>
</html>