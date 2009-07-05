<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
Ext.onReady(function(){
	var url='http://www.baidu.com/?wd=5';

	var fp = new Ext.form.FormPanel({
		title:'留言',
    	labelWidth : 90,
    	width:500,
    	height:400,
		url : url,
		labelAlign : 'right',
		labelSeparator : '：',
		autoCreate:false,
		method:'get',
		el:'ylb_form_panel',
		items:[{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [{
				xtype : 'textfield',
				fieldLabel:'姓名',
				name:'username'
			}]
		},{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [{
				xtype : 'textfield',
				fieldLabel:'联系电话',
				name:'phone'
			}]
		},{
			style:'margin-top:10px;',
			columnWidth : .45,
			layout : 'form',
			border : false,
			items : [{
				xtype : 'textfield',
				fieldLabel:'内容',
				name:'desc',
				xtype:'textarea'
			}]
		}],
		buttons:[{
			text:'确定',
			handler:function(){
				fp.form.submit();
			}
		}]
	});
	
	fp.render();
});
</script>
<div id="ylb_form_panel"></div>