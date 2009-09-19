Ext.onReady(function(){
	var file = {};
	
	var toolBar = new Ext.Toolbar({
		el:'file_tool_bar',
        items:[{
		        text:'上传',
		        tooltip:'上传文件',
		        iconCls:'add',
		        handler: function(){
        			getDialog().show();
		        }
	    	},'-',{
		        text:'下载',
		        tooltip:'下载文件',
		        iconCls:'remove',
		        handler: function(){
		            window.location = '/common/downloadFileTools.action?bizfield=doc-files&filePath='+file.filePath+'&contentType='+file.filePath;
		        }
	    	}]
	})
    toolBar.render();

    var dialog = null;
	function getDialog() {
		if (!dialog) {
			dialog = new Ext.ux.UploadDialog.Dialog( {
				url : '/common/uploadFileTools.action?bizfield=doc-files&folderpath=upload',
				reset_on_hide : false,
				allow_close_on_upload : true,
				upload_autostart : false, 
				post_var_name : 'doc',
		        permitted_extensions: ['jpg', 'jpeg', 'gif','doc','png','wma','jar','csv']
			});

			dialog.on('uploadsuccess', onUploadSuccess);
		}
		return dialog;
	}

	function onUploadSuccess(dialog, filename, responseObject, record) {
		file.filePath = responseObject.fileInfo.filePath;
		file.contentType= responseObject.fileInfo.contentType;
	}
    
});