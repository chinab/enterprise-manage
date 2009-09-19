Ext.onReady(function(){
	var mainPanelWidth = mainPanel.getInnerWidth()-6;
	var mainPanelHeight = mainPanel.getInnerHeight()-2;
	var loadUrl = '/common/getFilesFileTools.action';
		
	var browseFilesTree = new Ext.tree.TreePanel({
        autoScroll:true,
        title:'文件系统',
        rootVisible:false,
        animate:true,
        broder:true,
        margins:'0 5 5 5',
        region:'center',
        height : mainPanelHeight,
        width: mainPanelWidth*0.3,
        containerScroll: true,
        border:false,
        loader: new Ext.tree.TreeLoader({
            dataUrl:loadUrl
        }),
        listeners:{click : function(node,e){
				browseFilesGridDs.load({params:{'node':node.attributes.bizfield+'*false*'+node.attributes.filePath}});
        	}
        }
    });
    var root = new Ext.tree.AsyncTreeNode({
        text: '文件系统',
        draggable:false,
        id:'root*true*/'
    });
    browseFilesTree.setRootNode(root);
    browseFilesTree.expand(root);

	var browseFilesGridDs = new Ext.data.JsonStore({ 
    	url:loadUrl,
    	root: 'files',
    	id: 'id',
    	fields:[
			{name:'id'},
			{name:'bizfield'},
			{name:'fileName'},
			{name:'fileType'},
			{name:'filePath'},
			{name:'contentType'},
			{name:'lastModified'},
			{name:'size'}]
    });

	var browseFilesGrid = new Ext.grid.GridPanel({
        title: '数据列表',
    	border : false,
        region:'east',
        width: mainPanelWidth*0.73,
        margins:'0 5 5 0',
        selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
        autoScroll : true,
        ds: browseFilesGridDs,
        cm: new Ext.grid.ColumnModel([
    		new Ext.grid.RowNumberer(),
			{header:'id', width:80, sortable:true,hidden:true, dataIndex:'id'},
			{header:'业务域', width:80, sortable:true, dataIndex:'bizfield'},
			{header:'业务域下路径', width:150, sortable:true, dataIndex:'filePath'},
			{header:'文件名', width:150, sortable:true, dataIndex:'fileName'},
			{header:'扩展名', width:50, sortable:true, dataIndex:'fileType'},
			{header:'网络传输类型', width:75,hidden:true, sortable:true, dataIndex:'contentType'},
			{header:'大小(B)', width:70, sortable:true, dataIndex:'size'},
			{header:'最后修改日期', width:110, sortable:true, dataIndex:'lastModified'}
		]),
		listeners:{rowdblclick : function(grid,rowIndex,e){
				var m=grid.getSelections()[0];
				if(m.get('fileType')!='folder'){
					window.location.href = '/common/downloadFileTools.action?bizfield='+m.get('bizfield')+'&filePath='+m.get('filePath')+'&contentType='+m.get('fileType');
				}
			}
		}
    });
	browseFilesGridDs.load({params:{node:'root*false*/'}});

	var browseFilesPanle = new Ext.Panel({
    	layout : 'border' ,
    	border : false,
        region:'center',
    	renderTo: 'browseFilesSystemManagerDiv',
        height : mainPanelHeight,
        width: mainPanelWidth,
        items : [browseFilesTree,browseFilesGrid]
    });
    
    browseFilesPanle.render();
});