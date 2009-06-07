var indexPage = {};
Ext.onReady(function(){
       Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
       
       indexPage.selectMenuItem = function(title,url,params){
       		main.setTitle(title);
       		main.load({url:url,params:params});
       }
       
       var secondMune = [{
                        title:'Navigation',
                        html:'<p onclick=\'indexPage.selectMenuItem("销售管理 >> treeNode1","/testDatas/treelogon.txt",{a:"sss"})\'>treeNode1</p>'
                        	+'<p onclick=\'indexPage.selectMenuItem("销售管理 >> treeNode2","/testDatas/treelogon.txt",{a:"sss"})\'>treeNode2</p>'
                        	+'<p onclick=\'indexPage.selectMenuItem("销售管理 >> treeNode3","/testDatas/treelogon.txt",{a:"sss"})\'>treeNode3</p>',
                        border:false,
                        iconCls:'icon-house'
                    },{
                        title:'Settings',
                        html:'<p onclick=\'indexPage.selectMenuItem("销售管理 >> treeNode4","/boot/welcome.jsp",{a:"sss"})\'>treeNode4</p>',
                        border:false,
                        iconCls:'icon-house'
                    }];
                    
	   var main = new Ext.Panel({
                    region:'center',
                    title: '首页',
                    closable:true,
                    autoScroll:true,
                    autoLoad:'/boot/welcome.jsp'
                });
        
       var viewport = new Ext.Viewport({
            layout:'border',
            items:[
                new Ext.BoxComponent({ // raw
                    region:'north',
                    html:'header',
                    height:32
                }),{
                    region:'west',
                    id:'west-panel',
                    title:'销售管理',
                    split:true,
                    width: 200,
                    minSize: 175,
                    maxSize: 400,
                    collapsible: true,
                    margins:'0 0 0 5',
                    layout:'accordion',
                    //hidden:true,
                    layoutConfig:{
                        animate:true
                    },
                    items: secondMune
                },
                main
             ]
        });
    });