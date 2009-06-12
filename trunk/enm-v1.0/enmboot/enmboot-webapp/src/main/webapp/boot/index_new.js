/**
 * rp3新架构首页面对象
 * 包含了首页面大部分的属性与动作
 **/
var indexPage = {};

/**
 * 主面板，用来展示页面的面板，同indexPage.main，为其他页面的调用开放为全局变量
 **/
var mainPanel = {};
Ext.onReady(function(){
	/**
	 * 开启了页面缓存，可以存储布局
	 **/
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	
	/**
	 * 构建第一级与第二级菜单时请求的路径
	 **/
	indexPage.muneListUrl = '/enmboot/getMenuListEnmMenu.action';
	
	/**
	 * 构建第二级菜单下的树时请求的路径
	 **/
	indexPage.muneTreeUrl = '/enmboot/getMenuTreeEnmMenu.action';
	
	/**
	 * 存储访问记录的属性
	 **/
	indexPage.pages={curSize:-1,maxSize:-1};
	
	/**
	 * 点击二级菜单上的树的叶子结点时的事件
	 * 在主面板上打开一个页面，并在浏览历史中存储记录
	 **/
	indexPage.selectSecondMenuItem = function(title,url,params){
		indexPage.openPage(title,url,params)
		if(indexPage.pages['page'+indexPage.pages.curSize]==null||title!=indexPage.pages['page'+indexPage.pages.curSize].title||url!=indexPage.pages['page'+indexPage.pages.curSize].url){
			curSize=indexPage.pages.curSize+1;
			indexPage.pages.curSize=curSize;
			indexPage.pages.maxSize=curSize;
			indexPage.pages['page'+curSize]={title:title,url:url,params:params};
			indexPage.controlPageBtn();
		}
	}
	
	/**
	 * 上一页，点击上一页按扭时触发
	 **/
	indexPage.forwardPage = function(){
		if(indexPage.pages.curSize<indexPage.pages.maxSize){
			indexPage.pages.curSize=indexPage.pages.curSize+1;
			var page = indexPage.pages['page'+indexPage.pages.curSize];
			indexPage.openPage(page.title,page.url,page.params);
			indexPage.controlPageBtn();
		}
	}
	
	/**
	 * 下一页，点击“下一页”按扭时触发
	 **/
	indexPage.backPage = function(){
		if(indexPage.pages.curSize>0){
			indexPage.pages.curSize=indexPage.pages.curSize-1;
			var page = indexPage.pages['page'+indexPage.pages.curSize];
			indexPage.openPage(page.title,page.url,page.params);
			indexPage.controlPageBtn();
		}
	}
	
	/**
	 * 刷新，点击“刷新”按扭时触发
	 **/
	indexPage.refreshPage=function(){
		indexPage.openPage(indexPage.curPage.title,indexPage.curPage.url,indexPage.curPage.params);
	}
	
	/**
	 * 通过检查浏览历史记录，来控制“上一页”与“下一页”按扭是否可用
	 **/
	indexPage.controlPageBtn = function(){
		var backPageBtn = Ext.getCmp('backPageBtn');
		var forwardPageBtn = Ext.getCmp('forwardPageBtn');
		if(indexPage.pages.curSize>=indexPage.pages.maxSize){
			forwardPageBtn.disable();
		}else{
			forwardPageBtn.enable();
		}
		if(indexPage.pages.curSize<=0){
			backPageBtn.disable();
		}else{
			backPageBtn.enable();
		}
	}
	
	/**
	 * 在主面板上打开一个页面，顺便传些信息给后台
	 **/
	indexPage.openPage = function(title,url,params){
		indexPage.curPage={title:title,url:url,params:params};
		indexPage.main.setTitle(title);
		indexPage.main.load({url:url,params:params,scripts:true});
		if(params!=null){
			var visitRecord = "[{";
	        visitRecord += "\"id\":\"\",";
	        visitRecord += "\"deptId\":\"" + RC3UserInfo.departmentId + "\",";
	        visitRecord += "\"employeeInformId\":\"" + RC3UserInfo.userId + "\",";
	        visitRecord += "\"loginDate\":" + Ext.util.JSONExtender.encode(new Date()) + ",";
	        visitRecord += "\"enmMenuId\":\"" + params.resourceNode + "\",";
	        visitRecord += "\"loginIp\":\"" + RC3UserInfo.loginIp + "\",";
	        visitRecord += "\"browserEdition\":\"" + RC3UserInfo.browserEdition + "\",";
	        visitRecord += "\"computerName\":\"" + RC3UserInfo.computerName + "\"";
	        visitRecord = visitRecord + "}]";
	        Ext.Ajax.request({
				url:"/log/batchLoginRecord.action?jsonData="+visitRecord
			}); 
		}
	}
	
	
	/**
	 * 在主面板上打开一个页面，顺便传些信息给后台
	 **/
	indexPage.selectfirstMenuItem = function(title,url,enmMenuId,enable){
		if(enable!='Y'){
			Ext.MessageBox.alert('提示','没有权限！');
			return;
		}
		indexPage.openedMain = false;
		var menu = Ext.getCmp('secondMenu_panel');
		menu.setTitle(title);
		/**
		while(menu.items.length!=0){
			menu.remove(menu.items.get(0));
		}
		**/
		var otherMenuItems = new Array(menu.items.length);
		for(var i=0;i<menu.items.length;i++){
			otherMenuItems[i]=menu.items.get(i);
		}
		if(indexPage['menuItem'+enmMenuId]!=null&&indexPage['menuItem'+enmMenuId].length>=1){
			indexPage.changeSecondMenu(otherMenuItems,enmMenuId,menu,true);
		}else
			Ext.Ajax.request({
				url:url,params:{enmMenuId:enmMenuId},
				success: function(response, options){
					var responseArr = Ext.util.JSON.decode(response.responseText);
					
					indexPage['menuItem'+enmMenuId] =new Array(responseArr.length);
					for(var i=0;i<responseArr.length;i++){
						var menuItemId = responseArr[i].id;
						var menuItemTitle = responseArr[i].enmMenuName;
						var tree = indexPage.openMenuTree(menuItemTitle,indexPage.muneTreeUrl,menuItemId,i==0);
						var menuItem = {
							id:'menuItemId'+menuItemId,
							title:menuItemTitle,
							border:false,
							collapsed:false,
							iconCls:'icon-house',
							items:tree
						}
						menu.add(menuItem);
						indexPage['menuItem'+enmMenuId][i]=Ext.getCmp(menuItem.id);
					}
					
					indexPage.changeSecondMenu(otherMenuItems,enmMenuId,menu,false);
				} ,
				failure: function(response, options){
					Ext.MessageBox.alert('提示', '没有菜单项！');
				}
			});
	}
	
	indexPage.changeSecondMenu=function(otherMenuItems,enmMenuId,menu,treeExpendEd){
		menu.doLayout();
		menu.expand(true);
		
		for(var i=0;i<otherMenuItems.length;i++){
			otherMenuItems[i].hide();
			otherMenuItems[i].collapse(true);
		}
		for(var i=0;i<indexPage['menuItem'+enmMenuId].length;i++){
			indexPage['menuItem'+enmMenuId][i].show();
			if(i==0){
				indexPage['menuItem'+enmMenuId][i].expand(true);
				if(treeExpendEd){
					var tree = indexPage['menuItem'+enmMenuId][i].items.get(0);
					var firstChild =tree.getRootNode();
					var mainPanelTitle = firstChild.text;
					while(!firstChild.isLeaf()){
						firstChild=firstChild.firstChild;
						mainPanelTitle+=' > ' + firstChild.text;
					}
					indexPage.selectSecondMenuItem(mainPanelTitle,firstChild.attributes.href,{resourceNode:firstChild.id});
				}
			}
		}
		menu.doLayout();
		menu.expand(true);
	}
	
	indexPage.workTable = function(){
		var menu = Ext.getCmp('secondMenu_panel');
		indexPage.openPage('工作台','/boot/welcome.jsp');
		menu.setTitle('工作台');
		for(var i=0;i<menu.items.length;i++){
			menu.items.get(i).hide();
			menu.items.get(i).collapse(true);
		}
		menu.collapse(true);
	}
	
	indexPage.openMenuTree=function(title,url,nodeId,isFirstTree){
	    var tree = new Ext.tree.TreePanel({
	        autoScroll:true,
	        rootVisible:false,
	        animate:true,
	        //enableDD:true,
	        containerScroll: true,
	        border:false,
	        loader: new Ext.tree.TreeLoader({
	            dataUrl:url
	        }),
	        listeners:{click : function(node,e){
	        		e.stopEvent();
	        		if(node.isLeaf()){
		        		var parentNode = node.parentNode;
		        		var mainPanelTitle = node.text;
		        		while(parentNode.id != nodeId){
		        			mainPanelTitle=parentNode.text+' > '+mainPanelTitle;
		        			parentNode = parentNode.parentNode;
		        		}
		        		indexPage.selectSecondMenuItem(title +' > '+ mainPanelTitle,node.attributes.href,{resourceNode:node.id});
	        		}
	        	},expandnode:function(node){
	        		if(isFirstTree){
		        		if(!node.isLeaf()|| indexPage.openedMain) return;
		        		if(!node.isLeaf()||node.attributes.href==''||node.attributes.href==null||node.attributes.href=='undefind') return;
			        	var parentNode = node.parentNode;
		        		while(parentNode.id != tree.getRootNode().id){
		        			if(!parentNode.isFirst()) return;
		        			parentNode = parentNode.parentNode;
		        		}
		        		indexPage.openedMain = true;
			        	parentNode = node.parentNode;
		        		var mainPanelTitle = node.text;
		        		while(parentNode.id != nodeId){
		        			mainPanelTitle=parentNode.text+' > '+mainPanelTitle;
		        			parentNode = parentNode.parentNode;
		        		}
		        		indexPage.selectSecondMenuItem(title +' > '+ mainPanelTitle,node.attributes.href,{});
	        		}
	        	}
	        }
	    });
	    var root = new Ext.tree.AsyncTreeNode({
	        text: title,
	        draggable:false,
	        id:nodeId
	    });
	    tree.setRootNode(root);
	    tree.expandAll();
	    return tree;
	}
	
	indexPage.modifyPassword = function(){
		var _form = new Ext.form.FormPanel({
		        baseCls: 'x-plain',
		        labelWidth: 100,
		        url : '/modifyPasswordlogon.action',
		        defaultType: 'textfield',
				labelAlign : 'right',
				style:'margin-top:20px;',
				defaults: {
					width: 175,
			        inputType: 'password'
				},
		        items: [{
		            fieldLabel: '旧密码',
		            name: 'old_password',
		            anchor:'95%'
		            
		        },new Ext.ux.PasswordMeter({
					fieldLabel: '新密码',
		            name: 'new_password',
		            id:'new_password',
		            allowBlank: false,
		            vtype:'alphanum',
		            minLength:6,   
                    minLengthText:"密码最少为6位",  
		            anchor: '95%'  // anchor width by percentage
				}),{
		            fieldLabel: '新密码确认',
		            name: 'new_password_confirmation',
		            vtype: 'password',
		            allowBlank: false,
		            initialPassField: 'new_password',
		            vtype:'alphanum',
		            minLength:6,   
                    minLengthText:"密码最少为6位", 
		            anchor: '95%',
					invalidText:'两次密码不一致！',  
                    validator:function(){  
						var v = Ext.util.JSON.encode(_form.form.getValues(false));
			    		var values = Ext.util.JSON.decode(v);
						if(values.new_password == values.new_password_confirmation)  
							return true;  
							return false;  
					}
		        },{
		            //fieldLabel: 'userId',
		            name: 'userId',
		            value:RC3UserInfo.userId,
		            inputType:'hidden'
		            //anchor: '80%'  // anchor width by percentage
		        }]
		    });
    		var _win = new Ext.Window({
		        title: '修改密码',
		        width: 350,
		        height:200,
		        minWidth: 300,
		        minHeight: 200,
		        layout: 'fit',
		        plain:true,
		        bodyStyle:'padding:5px;',
		        buttonAlign:'center',
		        items: _form,
		
		        buttons: [{
		            text: '修改',
		            handler : function() {
						if (_form.form.isValid()) {
							_form.form.submit( {
								
								waitMsg : '正在保存',
								failure : function(form, action) {
									Ext.MessageBox.alert('错误', action.result.info+"密码修改失败");
								},
								success : function(form, action) {
									Ext.MessageBox.alert('提示', "密码修改成功");
									_win.close();
								}
							});
						} else {
							Ext.MessageBox.alert('Errors', '请检查需要填写的信息！');
						}
					}
		        },{
		            text: '取消',
		            handler : function() {
						_win.hide();
					}
		        }]
		    });
		
		    _win.show();
	}
	
	indexPage.help = function(){
		Ext.MessageBox.alert('提示','帮助');
	}
	
	/**
	 * 头部，包含一级菜单和快键按扭
	 * 一级菜单可以决定二级菜单的内容
	 **/
	indexPage.header = new Ext.Panel({
		region:'north',
		broder:false,
		height:60,
		autoLoad:{url:'/boot/header.jsp',scripts:true}
	});

	/**
	 * 左边的二级菜单
	 * 每一个菜单下都有一棵二级树
	 **/
	indexPage.secondMenu ={
		region:'west',
		id:'secondMenu_panel',
		title:'工作台',
		split:true,
		width: 200,
		minSize: 175,
		maxSize: 400,
		collapsible: true,
		margins:'0 0 0 5',
		layout:'accordion',
        collapseMode:'mini',
		layoutConfig:{
			animate:true
		},items:[{
			title:'工作台',
			html:'',
			border:false,
			iconCls:'icon-house'
		}]
	};
	
	/**
	 * 页面的主体部分
	 * 根据选择的菜单进行切换
	 **/
	mainPanel = indexPage.main = new Ext.Panel({
		region:'center',
		title: '工作台',
		closable:true,
		autoScroll:true,
		autoShow:true,
		split:true,
        margins:'0 0 0 0',
		loadMask: {msg:'正在加载页面，请稍侯……'},
		autoLaod:{url:'/boot/welcome.jsp',scripts:true}
	});
	 
	/**
	 *首页面的整体布局
	 **/
	indexPage.viewport = new Ext.Viewport({
		layout:'border',
		items:[indexPage.header,indexPage.secondMenu,indexPage.main]
	});
	
	indexPage.workTable();
});