if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(val) {
		var value = this;
		for ( var i = 0; i < value.length; i++) {
			if (value[i] == val)
				return i;
		}
		return -1;
	};
}

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
		var updatedFilesStr = $(this).parent().siblings(".updatedFiles").html();
		var updatedFiles =  updatedFilesStr.split(",");
		var filesStr = $(this).parent().siblings(".files").html();
		var files =  filesStr.split(",");
    	var htmlContent = "本次更新的文件<br/>";
    	for(var i=0;i<files.length;i++){
        	htmlContent += ' <div class="updatedFile"><label><input name="updatedFile" value="'+files[i]+'" type="checkbox"  ';
        	if(updatedFiles.indexOf(files[i])!=-1){
        		htmlContent += 'checked="checked"';
        	}
    		htmlContent += '>'+files[i]+'</label></div>';
    	}
    	htmlContent += "<div>　</div>";
    	$("#file_edit_files_div").html(htmlContent);
		
		$("#uploadIdInput").val(id);
		$("#uploadVersionInput").val(version);
		$("#uploadInfoInput").val(updateInfo);

		editContentDiv.show("fast");
		var offsetButton = $(this).offset();
		editContentDiv.offset({top:offsetButton.top+20,left:offsetButton.left-490});
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
		uploadContentDiv.offset({top:offsetButton.top+20,left:offsetButton.left-490});
	});

	closeUpdateButton.click(function(){
		uploadContentDiv.hide("fast");
	});

	closeEditButton.click(function(){
		editContentDiv.hide("fast");
	});
	
	$("#file_upload_input").uploadify({
        'formData'      : {'method' : 'upload'},
        'swf'           : 'js/uploadify.swf',
        'uploader'      : 'FileServlet',
        'width'			: 60,
        'height'		: 20,
        'buttonText'	:'选择文件',
        'onUploadSuccess' : function(file, data, response) {
        	if(response){
        		$(".uploadify-queue").html(file.name+"上传成功.");
	        	eval("var jsonData = "+data);
	        	$("#fileIdInput").val(jsonData.id);
	        	var htmlContent = "本次更新的文件<br/>";
	        	
	        	for(var i=0;i<jsonData.files.length;i++){
	        		htmlContent += ' <div class="updatedFile"><label><input name="updatedFile" value="'+jsonData.files[i]+'" type="checkbox" ';
	        		if (jsonData.files[i].indexOf("/: idb")==0||(jsonData.files[i].indexOf(".dll")!=jsonData.files[i].length-4&&jsonData.files[i].indexOf(".ttf")!=jsonData.files[i].length-4)) {
	        			htmlContent += 'checked="checked"';
					}
	        		htmlContent += '>'+jsonData.files[i]+'</label></div>';
	        	}
	        	htmlContent += "<div>　</div>";
	        	//jsonData.files;
	        	$("#file_upload_files_div").html(htmlContent);
        	}
        }
    });
});