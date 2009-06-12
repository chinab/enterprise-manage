<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <s:include value="/common/common.jsp"></s:include>    
  	<link rel="stylesheet" type="text/css" href="/ajaxlib/ext/resources/css/xtheme-gray.css"></link>
    <link rel="stylesheet" type="text/css" href="/css/main.css"></link>
	<link rel="stylesheet" type="text/css" href="/css/style.css"></link>
	<link rel="shortcut icon" href="/images/favicon.ico" />
	<link rel="icon" href="/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="/css/welcome.css"/>
	<script type="text/javascript" src="/jscript/common/Ext.ux.VTypes.js"></script>
	<script type="text/javascript" src="/jscript/TabCloseMenu.js"></script>
    <script type="text/javascript" src="/jscript/portal/Ext.ux.Portal.js"></script>
    <script type="text/javascript" src="/jscript/portal/Ext.ux.MaximizeTool.js"></script>
    <script type="text/javascript" src="/jscript/portal/Ext.ux.ManagedIFrame.js"></script>
    <link rel="stylesheet" type="text/css" href="/jscript/portal/portal.css" />
	<script type="text/javascript" src="/jscript/portal/Ext.ux.PortalManager.js"></script>
	<script type="text/javascript" src="/jscript/portal/portlet-sample-grid.js"></script>
	<script type="text/javascript" src="/jscript/portal/Ext.ux.PortalState.js"></script>
	<script type="text/javascript" src="/jscript/portal/Ext.ux.ManagedIFrame.js"></script>
    
    <style type="text/css">
    .login_form{
    background: url(/images/login/dt_login_mid.jpg) no-repeat;
    }
    .login_header{
    background: url(/images/login/head.JPG) no-repeat;
    }
    .login_bottom{
    background: url(/images/login/bottom.jpg) no-repeat;
    }
    .login_left{
    background: url(/images/login/left.jpg) no-repeat;
    }
    .login_right{
    background: url(/images/login/right.jpg) no-repeat;
    }
    .login_submit{
    background: url(/images/login/login_submit3.gif) no-repeat !important;
    }
    </style>
    
    <script type="text/javascript">
    Ext.onReady(function(){
    	Ext.QuickTips.init();
    	
    	var login_header = new Ext.Panel({
    		baseCls:'login_header',
    		height:95,
    		region:'north',
    		border:false
    	});
    	var login_bottom = new Ext.Panel({
    		baseCls:'login_bottom',
    		region:'south',
    		height:53,
    		border:false
    	});
    	var login_left = new Ext.Panel({
    		baseCls:'login_left',
    		width:239,
    		region:'west',
    		border:false
    	});
    	var login_right = new Ext.Panel({
    		baseCls:'login_right',
    		region:'east',
    		width:262,
    		border:false
    	});

		function modifyPassword(userId, userPassword){
		   var _form = new Ext.form.FormPanel({
		        baseCls: 'x-plain',
		        labelWidth: 80,
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
		            value: userPassword,
					//disabled : true,
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
		            value:userId,
		            inputType:'hidden'
		            //anchor: '80%'  // anchor width by percentage
		        }]
		    });
    		var _win = new Ext.Window({
		        title: '首次登录，请修改密码！',
		        width: 300,
		        height:190,
		        minWidth: 300,
		        minHeight: 200,
		        modal: true,
		        layout: 'fit',
		        plain:true,
		        bodyStyle:'padding:5px;',
		        buttonAlign:'center',
		        items: _form,
		        buttons: [{
		            text: '修改',
		            handler : function() {
						var new_password = Ext.getCmp('new_password').getValue();
						if (_form.form.isValid()) {
							_form.form.submit( {
								waitMsg : '正在保存',
								failure : function(form, action) {
									Ext.MessageBox.alert('错误', action.result.info+"密码修改失败");
								},
								success : function(form, action) {
									Ext.MessageBox.alert('提示', "密码修改成功");
									_win.close();
									Ext.getCmp('j_password').setValue(new_password);
								}
							});
						} else {
							Ext.MessageBox.alert('错误', '请检查需要填写的信息！');
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
    	};
    	
    	var loginSubmit = function(){
    		if (login.form.isValid()) {
						login.form.submit({
							waitMsg : '正在登录',
							failure : function(form,action) {
								var responseArray = Ext.util.JSON.decode(action.response.responseText);
								if (responseArray.info2=="4"){
									modifyPassword(responseArray.info1, responseArray.info);
								} else
									Ext.MessageBox.alert('错误', responseArray.info);
							},
							success : function(form, action) {
								var screenX = screen.availWidth;
								var screenY = screen.availHeight;
								var tmp=window.open("about:blank","rp3","toolbar=0,directories=0,location=0,status=0,scrollbars=0");
								tmp.moveTo(0,0);
								tmp.resizeTo(screenX,screenY);
								tmp.focus();
								tmp.location="/enmboot/loginEnmMenu.action";
								window.opener = null;
								window.open("","_self");
								window.close();
							}
						});
				}
			};
 
	    var login = new Ext.form.FormPanel({
	    	url : '/enmboot/loginValEnmMenu.action',
	    	region:'center',
	        labelWidth:43,
	        border:false,
	        baseCls:'login_form',
	        width: 456,
	        height:400,
	        items:[{ 
	        	style:'margin-top:180px;margin-left:265px;',
				layout : 'form',
				width : 150,
				border : false,
				items : [{
					xtype : 'textfield',
					fieldLabel : '用户名',
					name : 'j_username',
					anchor:'99%',
					allowBlank:false,
					value:'xvxv'
				}] 
	        },{
	        	style:'margin-left:265px;',
	        	width : 150,
				layout : 'form',
				border : false,
				items : [{
					xtype : 'textfield',
					fieldLabel : '密&nbsp;&nbsp;&nbsp;码',
					inputType:'password', 
					name : 'j_password',
					id : 'j_password',
					anchor:'99%',
					allowBlank:false,
					value:'3742591'
				}]
	        },{ 
	        	style:'margin-top:5px;margin-left:305px;',
	        	width : 100,
				layout : 'form',
				border : false,
				items : [{
					xtype : 'button',
					iconCls:'login_submit',
					text : '确定',
					handler:loginSubmit
				}]
	        }],
		     keys:[{
		     	key:13,
		     	fn:loginSubmit
		     }]
	    });
	 
	    var win = new Ext.Window({
	        layout:'border',
	        border:false,
	        width: 960,
	        height:550,
	        closable: false,
	        resizable: false,
	        items: [login_header,login_bottom,login_left,login_right,login]
	    });
	    win.show();
	});
    </script>

  </head>
  
  <body bgcolor=#EEEEEE>
  </body>
</html>
