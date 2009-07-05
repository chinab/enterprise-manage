/**
 * rp3新架构首页面对象
 * 包含了首页面大部分的属性与动作
 **/
var indexPage = {};

/**
 * 主面板，用来展示页面的面板，同indexPage.main，为其他页面的调用开放为全局变量
 **/
var mainPanel = {};

/**
 * 选择的菜单的id，为其他页面的调用开放为全局变量
 **/
Ext.onReady(function(){
	/**
	 * 开启了页面缓存，可以存储布局
	 **/
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	
	/********************************************页面属性***********************************************/
	/**
	 * 构建第一级与第二级菜单时请求的路径
	 **/
	indexPage.muneListUrl = '/enmboot/getMenuListEnmMenu.action';
	
	/**
	 * 构建第二级菜单下的树时请求的路径
	 **/
	indexPage.muneTreeUrl = '/enmboot/getMenuTreeEnmMenu.action';
	
	/**
	 * 逆向打开菜单，请求一级二级菜单id的url
	 **/
	indexPage.menuReverseUrl = '/enmboot/reverseMenuNodeEnmMenu.action';
	
	/**
	 * 存储访问记录的属性
	 **/
	indexPage.pages={curSize:-1,maxSize:-1,isClickOpenPage:true};
	
	/********************************************页面布局元素***********************************************/
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
		iconCls:'icon-org',
		id:'secondMenu_panel',
		title:'工作台',
		split:true,
		width: 165,
		minSize: 120,
		maxSize: 300,
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
		iconCls:'icon-computer-cls',
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
	 * 首页面的整体布局
	 **/
	indexPage.viewport = new Ext.Viewport({
		layout:'border',
		items:[indexPage.header,indexPage.secondMenu,indexPage.main]
	});
	
	/********************************************页面方法***********************************************/
	/**
	 * 点击二级菜单上的树的叶子结点时的事件
	 * 在主面板上打开一个页面，并在浏览历史中存储记录
	 * @param title 显示在同mainPanel上的标题
	 * @param url 要打开的页面的url
	 * @param params 请求页面所需的参数
	 **/
	indexPage.selectSecondMenuItem = function(title,url,params,iconCls){
		indexPage.openPage(title,url,params,iconCls);
		indexPage.savePageInfo(title,url,params,iconCls);
		indexPage.controlPageBtn();
	}
	
	/**
	 * 下一页，点击"下一页"按扭时触发
	 **/
	indexPage.forwardPage = function(){
		if(indexPage.pages.curSize<indexPage.pages.maxSize){
			indexPage.pages.isClickOpenPage=false;
			indexPage.pages.curSize=indexPage.pages.curSize+1;
			var page = indexPage.pages['page'+indexPage.pages.curSize];
			//indexPage.openPage(page.title,page.url,page.params);
			if(page.params=='workTable'){
				indexPage.workTable();
			}else{
				indexPage.reverseOpenMenu(page.params.resourceNode,page.title,page.url,page.iconCls);
			}
			indexPage.controlPageBtn();
		}
	}
	
	/**
	 * 上一页，点击"上一页"按扭时触发
	 **/
	indexPage.backPage = function(){
		if(indexPage.pages.curSize>0){
			indexPage.pages.isClickOpenPage=false;
			indexPage.pages.curSize=indexPage.pages.curSize-1;
			var page = indexPage.pages['page'+indexPage.pages.curSize];
			//indexPage.openPage(page.title,page.url,page.params);
			if(page.params=='workTable'){
				indexPage.workTable();
			}else{
				indexPage.reverseOpenMenu(page.params.resourceNode,page.title,page.url,page.iconCls);
			}
			indexPage.controlPageBtn();
		}
	}
	
	/**
	 * 刷新，点击"刷新"按扭时触发
	 **/
	indexPage.refreshPage=function(){
		indexPage.openPage(indexPage.curPage.title,indexPage.curPage.url,indexPage.curPage.params,indexPage.curPage.iconCls);
	}
	
	/**
	 * 保存当前页面信息
	 * @param title 显示在同mainPanel上的标题
	 * @param url 要打开的页面的url
	 * @param params 请求页面所需的参数
	 **/
	indexPage.savePageInfo=function(title,url,params,iconCls){
		if(indexPage.pages.isClickOpenPage&&indexPage.pages['page'+indexPage.pages.curSize]==null||title!=indexPage.pages['page'+indexPage.pages.curSize].title||url!=indexPage.pages['page'+indexPage.pages.curSize].url){
			curSize=indexPage.pages.curSize+1;
			indexPage.pages.curSize=curSize;
			indexPage.pages.maxSize=curSize;
			indexPage.pages['page'+curSize]={title:title,url:url,params:params,iconCls:iconCls};
		}
		indexPage.pages.isClickOpenPage=true;
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
	 * @param title 显示在同mainPanel上的标题
	 * @param url 要打开的页面的url
	 * @param params 请求页面所需的参数
	 **/
	indexPage.openPage = function(title,url,params,iconCls){
		indexPage.curPage={title:title,url:url,params:params,iconCls:iconCls};
		indexPage.main.setTitle(title);
		indexPage.main.setIconClass((iconCls==null||iconCls=='')?'icon-page-cls':iconCls);
		indexPage.main.load({url:url,params:params,scripts:true,callback:function(){document.title = COMPANY_TITLE;}});
	}
	
	/**
	 *已知一个结点id，要打开这个页面，并逆向打开菜单树
	 *@param leafNodeId 叶子结点的id
	 *@param title 打开页面的标题 可以为null
	 *@param url 请求页面的路径 可以为null
	 **/
	indexPage.reverseOpenMenu = function(leafNodeId,title,url,iconCls){
		Ext.Ajax.request({
			url:indexPage.menuReverseUrl,params:{enmMenuId:leafNodeId},
			success: function(response, options){
				var responseObject = Ext.util.JSON.decode(response.responseText);
				var enmMenuId = responseObject.enmMenuId;
				var enmMenuName = responseObject.enmMenuName;
				var nodeId = responseObject.nodeId;
				
				indexPage.selectfirstMenuItem(enmMenuName,indexPage.menuListUrl,enmMenuId,'Y',{nodeId:nodeId,leafNodeId:leafNodeId});
			},
			failure: function(response, options){
				Ext.MessageBox.alert('提示', '菜单级联失败，仅打开页面！');
				if(title!=null&&url!=null){
					indexPage.selectSecondMenuItem(title,url,leafNodeId,iconCls);
				}
			}
		});
	}
	
	/**
	 * 点击第一级菜单
	 * 判断是不是第一次打开，如果是就去访问后台，加载这些菜单项，并打开第一个叶子结点，保存这些信息
	 * 如果不是就，调用已存信息，打开第一个叶子结点，将其他菜单项隐藏
	 * @param title 显示在二级菜单面板（indexPage.secondMenu）上的标题
	 * @param url 请求二级菜单的url
	 * @param enmMenuId 一级菜单的id
	 * @param enable 是否可用，由后台构造，‘Y’可用，‘N’或其他不可用
	 * @param nodeIdAndLeafNodeId 当逆向打开菜单时有值
	 **/
	indexPage.selectfirstMenuItem = function(title,url,enmMenuId,enable,nodeIdAndLeafNodeId){
		if(enable!='Y'){
			Ext.MessageBox.alert('提示','没有权限！');
			return;
		}
		indexPage.isOpenedFirstNode = false;
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
			indexPage.changeSecondMenu(otherMenuItems,enmMenuId,menu,true,nodeIdAndLeafNodeId);
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
					
					indexPage.changeSecondMenu(otherMenuItems,enmMenuId,menu,false,nodeIdAndLeafNodeId);
				} ,
				failure: function(response, options){
					Ext.MessageBox.alert('提示', '没有菜单项！');
				}
			});
	}
	
	/**
	 * 当切换一级单时，将其他一级菜单下的二级菜单隐藏，并收缩
	 * 而该一级菜单下的二级菜单显示并展开
	 * @param otherMenuItems 不用显示的二级菜单项，将被隐藏
	 * @param enmMenuId 一级菜单的id
	 * @param menu 也就是indexPage.secondMenu的控件对象（不同于indexPage.secondMenu，因为已被包装成Ext控件）
	 * @param treeExpendEd 树是否已展开，如果已展开就要进行遍历得到第一个叶子，并打开，否则树结点的监听可以完成任务
	 * @param nodeIdAndLeafNodeId 当逆向打开菜单时有值
	 **/
	indexPage.changeSecondMenu=function(otherMenuItems,enmMenuId,menu,treeExpendEd,nodeIdAndLeafNodeId){
		menu.doLayout();
		menu.expand(true);
		
		for(var i=0;i<otherMenuItems.length;i++){
			otherMenuItems[i].hide();
			otherMenuItems[i].collapse(true);
		}
		for(var i=0;i<indexPage['menuItem'+enmMenuId].length;i++){
			var selectedMenuItem = indexPage['menuItem'+enmMenuId][i];
			selectedMenuItem.show();
			if(nodeIdAndLeafNodeId==null&&i==0){
				selectedMenuItem.expand(true);
				if(treeExpendEd){
					var tree = selectedMenuItem.items.get(0);
					var firstChild =tree.getRootNode();
					var mainPanelTitle = firstChild.text;
					while(!firstChild.isLeaf()){
						firstChild=firstChild.firstChild;
						mainPanelTitle+=' > ' + firstChild.text;
					}
					indexPage.selectSecondMenuItem(mainPanelTitle,firstChild.attributes.href,{resourceNode:firstChild.id},firstChild.attributes.iconCls);
				}
			}
		}
		if(nodeIdAndLeafNodeId!=null){
			var selectedMenuItem = Ext.getCmp('menuItemId'+nodeIdAndLeafNodeId.nodeId);
			selectedMenuItem.expand(true);
			if(treeExpendEd){
				var tree = selectedMenuItem.items.get(0);
				var node = tree.getNodeById(nodeIdAndLeafNodeId.leafNodeId);
				var parentNode = node.parentNode;
        		var mainPanelTitle = node.text;
        		while(parentNode.id != nodeIdAndLeafNodeId.nodeId){
        			mainPanelTitle=parentNode.text+' > '+mainPanelTitle;
        			parentNode = parentNode.parentNode;
        		}
        		indexPage.selectSecondMenuItem(parentNode.text +' > '+ mainPanelTitle,node.attributes.href,{resourceNode:node.id},node.attributes.iconCls);
			}
		}
		menu.doLayout();
	}
	
	/**
	 * 打开工作台
	 **/
	indexPage.workTable = function(){
		var menu = Ext.getCmp('secondMenu_panel');
		var title = '工作台';
		var url = '/boot/welcome.jsp';
		indexPage.openPage(title,url,{},'icon-computer-cls');
		indexPage.savePageInfo(title,url,'workTable');
		menu.setTitle('工作台');
		for(var i=0;i<menu.items.length;i++){
			menu.items.get(i).hide();
			menu.items.get(i).collapse(true);
		}
		
		menu.collapse(true);
	}
	
	/**
	 * 打开一棵菜单树，其中isFirstTree表示是否为一个二级菜单的第一棵树
	 * 因为第一棵树要优先展开，并将第一个叶子结点在mainPanel上打开
	 * 返回结果就是这棵树
	 * @param title 二级菜单项的标题
	 * @param url 加载树结点的url
	 * @param nodeId 树的根结点id
	 * @param isFirstTree 是否为二级菜单中的第一项（第一棵树），如果是就要打开第一个叶子结
	 **/
	indexPage.openMenuTree=function(title,url,nodeId,isFirstTree,nodeIdAndLeafNodeId){
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
		        		indexPage.selectSecondMenuItem(title +' > '+ mainPanelTitle,node.attributes.href,{resourceNode:node.id},node.attributes.iconCls);
	        		}
	        	},expandnode:function(node){
	        		if(nodeIdAndLeafNodeId==null&&isFirstTree){
		        		if(!node.isLeaf()|| indexPage.isOpenedFirstNode) return;
		        		if(!node.isLeaf()||node.attributes.href==''||node.attributes.href==null||node.attributes.href=='undefind') return;
			        	var parentNode = node.parentNode;
		        		while(parentNode.id != tree.getRootNode().id){
		        			if(!parentNode.isFirst()) return;
		        			parentNode = parentNode.parentNode;
		        		}
		        		indexPage.isOpenedFirstNode = true;
			        	parentNode = node.parentNode;
		        		var mainPanelTitle = node.text;
		        		while(parentNode.id != nodeId){
		        			mainPanelTitle=parentNode.text+' > '+mainPanelTitle;
		        			parentNode = parentNode.parentNode;
		        		}
		        		indexPage.selectSecondMenuItem(title +' > '+ mainPanelTitle,node.attributes.href,{resourceNode:node.id},node.attributes.iconCls);
	        		}else if(nodeIdAndLeafNodeId!=null&&node.id==nodeIdAndLeafNodeId.leafNodeId){
		        		indexPage.isOpenedFirstNode = true;
	        			var parentNode = node.parentNode;
		        		var mainPanelTitle = node.text;
		        		while(parentNode.id != nodeId){
		        			mainPanelTitle=parentNode.text+' > '+mainPanelTitle;
		        			parentNode = parentNode.parentNode;
		        		}
		        		indexPage.selectSecondMenuItem(title +' > '+ mainPanelTitle,node.attributes.href,{resourceNode:node.id},node.attributes.iconCls);
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
	
	/**
	 * 修改密码
	 **/
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
	
	/**
	 * 帮助
	 **/
	indexPage.help = function(){
		Ext.MessageBox.alert('提示','帮助');
	}
	
	/**
	 * 默认打开工作台
	 **/
	indexPage.workTable();
});