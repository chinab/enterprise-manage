<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	Ext.onReady(function(){
		var _toolbar=new Ext.Toolbar({
    		region:'center',
    		el:'toolbar',
    		cls:'toolbar_backgroud',
    		border:false,
	    	items:[' ',
		    	{
		    		tooltip:'旧架构',
		    		iconCls:'old_layout',
		    		handler:function(){
		    			window.location="saveStrutslogon.action?systemType=index";
		    		}
		    	},'-',
		    	{
		    		tooltip:'重新登录',
		    		iconCls:'reboot_login',
		    		handler:function(){
		    			window.location="index.jsp";
		    		}
		    	},'-',
		    	{
		    		tooltip:'修改密码',
		    		iconCls:'modify_password',
		    		handler: function(){
		    			indexPage.modifyPassword();
		    		}
		    	},'-',
		    	{
		    		tooltip:'系统帮助',
		    		iconCls:'system_help',
		    		handler: function(){
		    			indexPage.help();
		    		}
		    	},'-',
		    	{
		    		tooltip:'退出系统',
		    		iconCls:'system_close',
		    		handler:function(){
		    			window.close();
		    		}
		    	},'-',
		    	{
		    		tooltip:'上一页',
		    		id:'backPageBtn',
		    		disabled:true,
		    		iconCls:'top_page',
		    		handler:function(){
		    			indexPage.backPage();
		    		}
		    	},'-',
		    	{
		    		tooltip:'下一页',
		    		id:'forwardPageBtn',
		    		disabled:true,
		    		iconCls:'next_page',
		    		handler:function(){
		    			indexPage.forwardPage();
		    		}
		    	},'-',
		    	{
		    		tooltip:'刷新',
		    		iconCls:'renovate',
		    		handler:function(){
		    			indexPage.refreshPage();
		    		}
		    	},'-'
	    	]
    	});
    	
	    Ext.Ajax.request({
			url:indexPage.muneListUrl,params:{enmMenuId:-1},
			success: function(response, options){
				var responseArr = Ext.util.JSON.decode(response.responseText);
				/**
				var indexPageMenuTable='<table height="40" border="0" cellpadding="0" cellspacing="0" align="right">'+
   		   			'<tr align="center"><td width="90" class="guidance-start" onclick="indexPage.workTable();">'+
			        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="memu" href="#">工作台</a></td>';
				for(var i=0;i<responseArr.length;i++){
					if(responseArr[i].enmMenuDisable=='Y'){
						indexPageMenuTable+='<td width="80" class="guidance-min" onclick="indexPage.selectfirstMenuItem(\''+responseArr[i].enmMenuName+'\',\''+indexPage.muneListUrl+'\','+responseArr[i].id+');">'+
					        '<a class="memu" href="#">'+responseArr[i].enmMenuName+'</a>'+
					    	'</td>';
				    }else if(responseArr[i].enmMenuDisable=='N'){
				    	indexPageMenuTable+='<td width="80" class="guidance-min">'+
					        '<a class="memuNoDisable" href="#">'+responseArr[i].enmMenuName+'</a>'+
					    	'</td>';
				    }
				}
				indexPageMenuTable+='</tr></table>';
				document.getElementById("indexPageMenuTable").innerHTML=indexPageMenuTable;
				**/
				var indexPageMenuUl='<ul class="guidance-min">'+
				    '<li class="guidance-start"></li>'+
					'<li class="li_css2" onclick="indexPage.workTable();"><a href="#" class="menu">工作台</a></li>';
				for(var i=0;i<responseArr.length;i++){
					indexPageMenuUl+='<li class="li_css2" onclick="indexPage.selectfirstMenuItem(\''+responseArr[i].enmMenuName+'\',\''+indexPage.muneListUrl+'\','+responseArr[i].id+',\''+responseArr[i].enmMenuDisable+'\');">'+
				        (responseArr[i].enmMenuDisable=='Y'?('<a class="menu" href="#">'+responseArr[i].enmMenuName+'</a>'):('<span class="disabel_link">'+responseArr[i].enmMenuName+'</span>'))+
				    	'</li>';
				}
				indexPageMenuUl+='</ul>';
				document.getElementById("indexPageMenuUl").innerHTML=indexPageMenuUl;
			} ,
			failure: function(response, options){
				Ext.MessageBox.alert('提示', '菜单加载失败！');
			}
		});
		
   		_toolbar.render();
	});
</script>
<%-- 
<div style="top:0px;left:0px;height:65px;width:100%; position:absolute;" class="header_mid">
	<div style="top:0px; left:0px; height:65px; width:225px; position:absolute;" class="header_left"></div>
	<div style="top:0px; right:0px; height:65px; width:25px; position:absolute;" class="header_right"></div>
	
	<div style="top:0px; right:25px; height:25px; width:630px; position:absolute;">
		<table height="25" border="0" cellpadding="0" cellspacing="0" align="right">
	      <tr>
	        <td>系统管理员:您好! 今天是2009年06月07日</td>
	        <td class="toolbar_backgroud_start">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	        <td class="toolbar_backgroud"><div id="toolbar"></div></td>
	      </tr>
	    </table>
	</div>
	
	<div style="top:25px; right:25px; height:25px;width:630px; position:absolute;"  id="indexPageMenuTable">
		<table height="40" border="0" cellpadding="0" cellspacing="0" align="right">
   		   <tr align="center">
	      	<td width="90" class="guidance-start" onclick="indexPage.workTable();">
		        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="memu" href="#">工作台</a>
		    </td>
	      </tr>
    	</table>
	</div>
	
</div>
 --%>

<div  class="header_mid">
	<div class="header_left"></div>
	<div class="header_right"></div>
	<div class="toolbar">
		<ul class="toolbar_backgroud_start">
		    <li class="li_css"><div id="toolbar"/></div></li>
		    <%--
		    <li class="li_css"><img src="images/new_main/exit_site.gif" /></li>
			<li class="li_css"><img src="images/new_main/help.gif"/></li>
			<li class="li_css"><img src="images/new_main/modify_pw.gif" /></li>
			<li class="li_css"><img src="images/new_main/re_login.gif" /></li>
		     --%>
		</ul>
		<ul>
			<li class="text">系统管理员:您好! 今天是2009年06月07日</li>
		</ul> 
  	</div>
	
	<div  class="guidance" id="indexPageMenuUl">
		<ul class="guidance-min">
			<li class="guidance-start"></li>
			<li class="li_css2" onclick="indexPage.workTable();">
				<a href="#" class="menu">工作台</a>
			</li>
		</ul>
	</div>
</div>
