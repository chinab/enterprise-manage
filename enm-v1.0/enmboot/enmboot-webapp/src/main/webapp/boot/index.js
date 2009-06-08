var _indexPage = {};
Ext.onReady(function(){
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider()); 
	
	var SecondMuneItem = function(){
		this.title
	}
	
	_indexPage.selectSecondMenuItem = function(title,url,params){
		_indexPage.main.setTitle(title);
		_indexPage.main.load({url:url,params:params});
	}
	
	_indexPage.selectfirstMenuItem = function(title,url){
		Ext.Ajax.request({
								url:'/ahboot/deleteFckEditerCotext.action',
					 			params:{deleteData:jsonData},
								success: function(response, options){
									var responseArray = Ext.util.JSON.decode(response.responseText);
										if(responseArray.success==true){
											loadTableDs.reload();
										Ext.Msg.alert('完成','删除成功！');
									}
									else{
										Ext.Msg.alert('失败','删除失败!');
									}
								} ,
								failure: function(response, options){
									Ext.MessageBox.alert('Failed', 'Failed');
								}
							});
							
		var menu = Ext.getCmp('secondMenu_panel');
		_indexPage.selectSecondMenuItem('销售管理 > 管理1 > treeNode1','/boot/test/text1.html');
		menu.setTitle(title);
		while(menu.items.length!=0){
			menu.remove(menu.items.get(0));
		}
		menu.add({
			title:'管理1',
			html:'<p onclick=\'_indexPage.selectSecondMenuItem("销售管理 > 管理1 > treeNode1","/boot/test/text1.html",{a:"sss"})\'>treeNode1</p>'
				+'<p onclick=\'_indexPage.selectSecondMenuItem("销售管理 > 管理1 > treeNode2","/boot/test/text2.html",{a:"sss"})\'>treeNode2</p>'
				+'<p onclick=\'_indexPage.selectSecondMenuItem("销售管理 > 管理1 > treeNode3","/boot/test/text3.html",{a:"sss"})\'>treeNode3</p>',
			border:false,
			iconCls:'icon-house'
		});
		menu.add({
			title:'管理2',
			html:'<p onclick=\'_indexPage.selectSecondMenuItem("销售管理 > 管理2 > treeNode4","/boot/welcome.jsp",{a:"sss"})\'>treeNode4</p>',
			border:false,
			iconCls:'icon-house'
		});
		
		menu.doLayout();
		menu.expand();
	}
	
	_indexPage.workTable = function(){
		//w.collapsed ? w.expand() : w.collapse();
		var menu = Ext.getCmp('secondMenu_panel');
		_indexPage.selectSecondMenuItem("工作台","/boot/welcome.jsp");
		menu.setTitle('工作台');
		while(menu.items.length!=0){
			menu.remove(menu.items.get(0));
		}
		menu.collapse();
	}
	
	_indexPage.modifyPassword = function(){
		Ext.MessageBox.alert("提示","修改密码");
	}
	
	_indexPage.help = function(){
		Ext.MessageBox.alert("提示","帮助");
	}
	
	/**
	 * 头部，包含一级菜单和快键按扭
	 * 一级菜单可以决定二级菜单的内容
	 **/
	_indexPage.header = new Ext.Panel({
		region:'north',
		broder:false,
		height:60,
		autoLoad:{url:'/boot/header.jsp',scripts:true}
	});

	/**
	 * 左边的二级菜单
	 * 每一个菜单下都有一棵二级树
	 **/
	_indexPage.secondMenu ={
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
	_indexPage.main = new Ext.Panel({
		region:'center',
		title: '工作台',
		closable:true,
		autoScroll:true,
		autoLoad:'/boot/welcome.jsp'
	});
	 
	/**
	 *首页面的整体布局
	 **/
	_indexPage.viewport = new Ext.Viewport({
		layout:'border',
		items:[_indexPage.header,_indexPage.secondMenu,_indexPage.main]
	});
	
	_indexPage.workTable();
});