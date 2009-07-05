<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
Ext.onReady(function(){
	var enmMenuInfo = {};
	var enmMenuTypeStore = new  Ext.data.SimpleStore({fields:["value", "name"],data:[["1","非叶子结点"],["2","叶子结点"]]});
	var enmMenuDisableStore = new  Ext.data.SimpleStore({fields:["value", "name"],data:[["Y","显示"],["N","不显示"]]});

	var fpReader = new Ext.data.JsonReader( {root : 'enmMenu'}, 
	   [
   		{name: 'enmMenuName', mapping :'enmMenuName'},
   		{name: 'enmMenuDisplayNo', mapping :'enmMenuDisplayNo'},
   		{name: 'enmMenuType', mapping :'enmMenuType'},
   		{name: 'enmMenuHref', mapping :'enmMenuHref'},
   		{name: 'enmMenuIco', mapping :'enmMenuIco'},
   		{name: 'enmMenuDisable', mapping :'enmMenuDisable'}
	   ]
	);
	
	var getFpItems = function(isUpdate){
		return [{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [{
				xtype : 'textfield',
				allowBlank:false,
				maxLength: 6,
				minLength: 1,
				fieldLabel : '菜单名',
				name : 'enmMenuName'
			}]
		},{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [isUpdate?{
				xtype: 'textfield',
				fieldLabel: '结点Id',
				readOnly:true,
	            name: 'enmMenuId',
	            value:enmMenuInfo.selectId==null?-1:enmMenuInfo.selectId
	        }:{
				xtype: 'textfield',
				fieldLabel: '父结点Id',
				readOnly:true,
	            name: 'enmMenuParentId',
	            value:enmMenuInfo.selectId==null?-1:enmMenuInfo.selectId
	        }]
		},{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [{
				xtype : 'combo',
				allowBlank:false,
				maxLength: 10,
				minLength: 1,
				width: 127,
				fieldLabel : '菜单类型',
				name : 'enmMenuType',
				hiddenName : 'enmMenuType',
				editable: false,
				store:  enmMenuTypeStore,
			    valueField :'value',
			    displayField: 'name',
	       		triggerAction: 'all',
			    mode: 'local',
			    blankText:'',
			    emptyText:'请选择'
	      }]
		},{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [{
				xtype : 'combo',
				allowBlank:false,
				maxLength: 10,
				minLength: 1,
				width: 127,
				fieldLabel : '是否显示',
				name : 'enmMenuDisable',
				hiddenName : 'enmMenuDisable',
				editable: false,
				store:  enmMenuDisableStore,
			    valueField :'value',
			    displayField: 'name',
	       		triggerAction: 'all',
			    mode: 'local',
			    blankText:'',
			    emptyText:'请选择'
	      }]
		},{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [{
				xtype: 'numberfield',
				fieldLabel: '显示顺序',
				name: 'enmMenuDisplayNo',
				allowBlank:false
	    	}]
		},{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [{
				xtype: 'textfield',
				fieldLabel: '图标',
				name: 'enmMenuIco',
				allowBlank:false
	    	}]
		},{
			style:'margin-top:10px;',
			columnWidth : .99,
			layout : 'form',
			border : false,
			items : [{
				width:350,
				xtype : 'textfield',
				maxLength: 100,
				minLength: 1,
				fieldLabel : '页面地址',
				name : 'enmMenuHref'
			}]
		}];
	}

	//修改
	var editMenu = function(){
		if(enmMenuInfo.selectId==null||enmMenuInfo.selectId==-1) {
			Ext.MessageBox.alert('提示', "请选择一个非根结点进行编辑！");
			return ;
		}
		var _fp = new Ext.form.FormPanel({
	    	labelWidth : 90,
	    	width:500,
	    	height:400,
			url : '/enmboot/updateEnmMenu.action',
			labelAlign : 'right',
			enctype:'multipart/form-data' ,
			layout : 'column',
			labelSeparator : '：',
			autoCreate:false,
			method:'POST',
			reader : fpReader,
	        items: getFpItems(true)
    	});
    	_fp.form.load( {
			url : '/enmboot/editEnmMenu.action',
			params:{enmMenuId:enmMenuInfo.selectId},
			waitMsg : '正在载入...'
		});
    	
    	var _win = new Ext.Window( {
			title : '修改',
			iconCls:'edit',
			width : 530,
			height : 250,
			minWidth : 300,
			minHeight : 160,
			layout : 'column',
			plain : true,
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			items : _fp,
			closable : true,
			buttons:[{
	    		text:'确定',
	    		handler:function(){
					if (_fp.form.isValid()) {
						_fp.form.submit({
							waitTitle: '提示',
							waitMsg : '请等待...',
							failure : function(form, action) {
								Ext.MessageBox.alert('错误', "修改失败！");
							},
							success : function(form, action) {
								_win.close();
								Ext.MessageBox.alert('提示', "修改成功！");
							}
						});
					} else {
						Ext.MessageBox.alert('Errors', '请检查需要填写的信息！');
					}
				}
	    	},{
				text : '取消',
				handler : function() {
					_win.close();
				}
			}]
		});
	
		_win.show();
	}
	//增加
	var insertMenu = function(){
		var _fp = new Ext.form.FormPanel({
	    	labelWidth : 90,
	    	width:500,
	    	height:400,
	        url:'/enmboot/insertEnmMenu.action',
			labelAlign : 'right',
			enctype:'multipart/form-data' ,
			layout : 'column',
			labelSeparator : '：',
			autoCreate:false,
			method:'POST',
			reader : fpReader,
	        items: getFpItems(false)
    	});
    	
    	var _win = new Ext.Window({
			title : '增加',
			iconCls:'add',
			width : 530,
			height : 250,
			minWidth : 300,
			minHeight : 160,
			layout : 'column',
			plain : true,
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			items : _fp,
			closable : true,
			buttons:[{
	    		text:'确定',
	    		handler:function(){
					if (_fp.form.isValid()) {
						_fp.form .submit({
							waitTitle: '提示',
							waitMsg : '请等待...',
							failure : function(form, action) {
								Ext.MessageBox.alert('错误', "保存失败！");
							},
							success : function(form, action) {
								_win.close();
								Ext.MessageBox.alert('提示', "保存成功！");
							}
						});
					} else {
						Ext.MessageBox.alert('Errors', '请检查需要填写的信息！');
					}
				}
	    	},{
				text : '取消',
				handler : function() {
					_win.close();
				}
			}]
		});
	
		_win.show();
	}
		
		
		var tree = new Ext.tree.TreePanel({
			region:'center',
			title: '菜单',
			closable:true,
			autoScroll:true,
			autoShow:true,
			split:true,
	        margins:'0 0 0 0',
	        autoScroll:true,
	        rootVisible:true,
	        width:500,
	        height:700,
	        animate:true,
	        el:'enmMenuTree',
	        containerScroll: true,
	        border:false,
	        loader: new Ext.tree.TreeLoader({
	            dataUrl:indexPage.muneTreeUrl
	        }),
	        tbar:[{
		    		tooltip:'增加',
		    		iconCls:'add',
		    		handler:function(){
		    			insertMenu();
		    		}
		    	},'-',
		    	{
		    		tooltip:'删除',
		    		iconCls:'remove',
		    		handler:function(){
		    		
		    		}
		    	},'-',
		    	{
		    		tooltip:'修改',
		    		iconCls:'edit',
		    		handler: function(){
		    			editMenu();
		    		}
		    }],
	        listeners:{click : function(node,e){
	        		e.stopEvent();
	        		enmMenuInfo.selectId = node.id;
	        	}
	        }
	    });
	    var root = new Ext.tree.AsyncTreeNode({
	        text: '菜单',
	        draggable:false,
	        id:-1
	    });
	    tree.setRootNode(root);
	    root.expand();
	    tree.render();
});
</script>
<div id="enmMenuTree"></div>