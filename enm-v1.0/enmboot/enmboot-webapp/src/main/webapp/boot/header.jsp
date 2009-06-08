<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	Ext.onReady(function(){
		var _toolbar=new Ext.Toolbar({
    		region:'center',
    		el:'toolbar',
    		cls:'header_backgroud',
    		border:false,
	    	items:['-',
		    	{
		    		tooltip:'重新登录',
		    		iconCls:'reboot_login',
		    		handler:function(){
		    			window.location="index.jsp"
		    		}
		    	},'-',
		    	{
		    		tooltip:'修改密码',
		    		iconCls:'modify_password',
		    		handler:_indexPage.modifyPassword
		    	},'-',
		    	{
		    		tooltip:'退出系统',
		    		iconCls:'system_close',
		    		handler:function(){
		    			window.close();
		    		}
		    	},'-',
		    	{
		    		tooltip:'日程管理',
		    		text:'日程管理',
		    		handler:function(){
		    			window.close();
		    		}
		    	},'-',
		    	{
		    		tooltip:'系统设置',
		    		text:'系统设置',
		    		handler:function(){
		    			window.close();
		    		}
		    	},'-',
		    	{
		    		tooltip:'系统说明',
		    		text:'系统说明',
		    		handler:function(){
		    			window.close();
		    		}
		    	},' '
	    	]
    	});
	    
   		_toolbar.render();
	});
</script>
<table width="100%" height="60" border="0" align="left" cellpadding="0" cellspacing="0" class="header_backgroud">
  <tr>
    <td>
    	<table width="200" border="0" align="left" cellpadding="0" cellspacing="0" class="header_logo">
	      <tr height="60">
	      	<td></td>
	      </tr>
	    </table>
    </td>
    <td>
	    <table width="630" border="0" align="right" cellpadding="0" cellspacing="0">
	      <tr height="25">
	      	<td>
			    <table border="0" cellpadding="0" cellspacing="0" align="right">
			      <tr height="25">
			        <td>系统管理员:您好! 今天是2009年06月07日</td>
			        <td>&nbsp;&nbsp;&nbsp;</td>
			        <td><div id="toolbar"></div></td>
			      </tr>
			    </table>
			</td>
	      </tr>
	      <tr height="35">
	      	<td>
			    <table border="0" cellpadding="0" cellspacing="0" align="right">
			      <tr height="35">
			      	<td>
				        <a href="javascript:_indexPage.workTable();">工作台</a> |
				        <a href="javascript:_indexPage.selectfirstMenuItem('menu1');">menu1</a> | 
				        <a href="javascript:_indexPage.selectfirstMenuItem('menu2');">menu2</a> | 
				        <a href="javascript:_indexPage.selectfirstMenuItem('menu3');">menu3</a> | 
				        <a href="javascript:_indexPage.selectfirstMenuItem('menu4');">menu4</a> | 
				        <a href="javascript:_indexPage.selectfirstMenuItem('menu5');">menu5</a> | 
				        <a href="javascript:_indexPage.selectfirstMenuItem('menu6');">menu6</a> | 
				        <a href="javascript:_indexPage.selectfirstMenuItem('menu7');">menu7</a> | 
				        <a href="javascript:_indexPage.selectfirstMenuItem('menu8');">menu8</a>
			        </td>
			        <td>&nbsp;&nbsp;&nbsp;</td>
			      </tr>
			    </table>
			</td>
	      </tr>
	    </table>
    </td>
  </tr>
</table>
