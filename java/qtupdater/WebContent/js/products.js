$(function() {
	var addContentsDiv = $("div.addContent");
	var editContentsDiv = $("div.editContent");
	var closeAddButton = $("button#closeAdd");
	var closeEditButton = $("button#closeEdit");
	addContentsDiv.hide();
	editContentsDiv.hide();
	$("a.addButton").click(function() {
		addContentsDiv.slideToggle();
	});
	$("a.deleteButton").click(function() {
		var id = $(this).parent().attr("id");
		var productName = $(this).parent().siblings(".productName").html();
		var isOk = confirm("确定要删除'"+productName+"'？");
		if (isOk) {
			window.location.href = "ProductServlet?method=remove&id=" + id;
		}
	});
	$("a.editButton").click(function() {
		var id = $(this).parent().attr("id");
		var productName = $(this).parent().siblings(".productName").html();
		var productInfo = $(this).parent().siblings(".productInfo").html();
		$("#editIdInput").val(id);
		$("#editNameInput").val(productName);
		$("#editProductInfoInput").val(productInfo);
		
		editContentsDiv.show("fast");
		var offsetButton = $(this).offset();
		editContentsDiv.offset({top:offsetButton.top+20,left:offsetButton.left});
		
		
	});

	closeAddButton.click(function(){
		addContentsDiv.hide("fast");
	});
	
	closeEditButton.click(function(){
		editContentsDiv.hide("fast");
	});

	
});