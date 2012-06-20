$(function(){
	var uploadContentDiv = $("div.uploadContent");
	var editContentDiv = $("div.editContent");
	var closeUpdateButton = $("button#closeUpdate");
	var closeEditButton = $("button#closeEdit");
	uploadContentDiv.hide();
	editContentDiv.hide();
	
	$("a.downloadButton").click(function(){
		var id = $(this).parent().attr("id");
		var filename = $(this).parent().attr("title");
		window.location.href = "FileServlet?method=download&id=" + id + "&filename=" + filename;
	});

	$("a.editButton").click(function(){
		var id = $(this).parent().attr("id");
		var version = $(this).parent().siblings(".version").html();
		var updateInfo = $(this).parent().siblings(".updateInfo").html();
		
		$("#uploadIdInput").val(id);
		$("#uploadVersionInput").val(version);
		$("#uploadInfoInput").val(updateInfo);

		editContentDiv.show("fast");
		var offsetButton = $(this).offset();
		editContentDiv.offset({top:offsetButton.top+20,left:offsetButton.left});
	});
	
	$("a.deleteButton").click(function(){
		var id = $(this).parent().attr("id");
		var filename = $(this).parent().attr("title");;
		var isOk = confirm("确定要删除'"+filename+"'？");
		if (isOk) {
			window.location.href = "FileServlet?method=remove&id=" + id;
		}
	});
	
	$("a.uploadButton").click(function(){
		var productId = $(this).parent().attr("id");
		$("#uploadProductIdInput").val(productId);
		uploadContentDiv.show("fast");
		var offsetButton = $(this).offset();
		uploadContentDiv.offset({top:offsetButton.top+20,left:offsetButton.left});
	});

	closeUpdateButton.click(function(){
		uploadContentDiv.hide("fast");
	});

	closeEditButton.click(function(){
		editContentDiv.hide("fast");
	});
});