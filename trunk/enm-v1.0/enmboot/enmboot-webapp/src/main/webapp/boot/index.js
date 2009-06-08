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
	
	_indexPage.selectfirstMenuItem = function(title){
		_indexPage.selectSecondMenuItem('销售管理 > 管理1 > treeNode1','/boot/test/text1.html');
		_indexPage.secondMenu.title = title;
		_indexPage.secondMenu.items = [{
			title:'管理1',
			html:'<p onclick=\'_indexPage.selectSecondMenuItem("销售管理 > 管理1 > treeNode1","/boot/test/text1.html",{a:"sss"})\'>treeNode1</p>'
				+'<p onclick=\'_indexPage.selectSecondMenuItem("销售管理 > 管理1 > treeNode2","/boot/test/text2.html",{a:"sss"})\'>treeNode2</p>'
				+'<p onclick=\'_indexPage.selectSecondMenuItem("销售管理 > 管理1 > treeNode3","/boot/test/text3.html",{a:"sss"})\'>treeNode3</p>',
			border:false,
			iconCls:'icon-house'
		},{
			title:'管理2',
			html:'<p onclick=\'_indexPage.selectSecondMenuItem("销售管理 > 管理2 > treeNode4","/boot/welcome.jsp",{a:"sss"})\'>treeNode4</p>',
			border:false,
			iconCls:'icon-house'
		}];
				
		var w = Ext.getCmp('secondMenu_panel');
		w.expand();
	}
	
	_indexPage.workTable = function(){
		//w.collapsed ? w.expand() : w.collapse();
		_indexPage.secondMenu.title = '工作台';
		_indexPage.selectSecondMenuItem("工作台","/boot/welcome.jsp");
		_indexPage.secondMenu.items = {};
		
		var w = Ext.getCmp('secondMenu_panel');
		w.collapse();
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
	_indexPage.secondMenu = {
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
		}
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