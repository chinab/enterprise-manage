package components.icons
{
	import components.ArrowLine;
	import components.FlowIcon;
	
	import mx.utils.StringUtil;
	
	import view.iconitemviews.NodePropertiesView;

	public class NodeIcon extends FlowIcon
	{
		public static var TYPE:int = 1;
		
		private var _actionTargetBean:String = "";
		
		public function set actionTargetBean(v:String):void{
			_actionTargetBean = v;
		}
		
		public function get actionTargetBean():String{
			return _actionTargetBean;
		}
		
		public function NodeIcon(iconIndex:int=0, type:int=0, label:String="结点", selected:Boolean=false, name:String=null, memo:String="")
		{
			super(this, iconIndex, label, selected, name, memo,TYPE);
		}
		
		/**
		 * 给出编辑面板
		 * 要用子类来覆盖
		 **/
		override protected function setPropertiesView(label: String):Boolean{
			var nodePropertiesView:NodePropertiesView = new NodePropertiesView();
			nodePropertiesView.nodeIcon = this;
			propertiesView = nodePropertiesView;
			return true;
		}
		
		/**
		 * 给出xml属性结点
		 * 要用子类来覆盖
		 **/
		override protected function getXMLProperties():String{
			var xmlStr:String ='<node name="'+name+'">';
			if(_actionTargetBean && StringUtil.trim(_actionTargetBean)!=""){
				xmlStr+='<action name="Action_AccountantProcess" config-type="bean" '
		      			+'class="org.springmodules.workflow.jbpm31.JbpmHandlerProxy">'
		    			+'<targetBean>'+_actionTargetBean+'</targetBean>'
					+'</action>';
			}
			for each(var line:ArrowLine in outLines){
				xmlStr+='<transition to="'+line.toIcon.name+'" name="'+line.name+'">';
				if(line.action && StringUtil.trim(line.action)!=""){
					xmlStr+='<action config-type="bean" '+
			      			'class="org.springmodules.workflow.jbpm31.JbpmHandlerProxy">'+
			    			'<targetBean>'+line.action+'</targetBean>'+
						'</action>';
				}
				xmlStr+='</transition>';
			}
			xmlStr+='</node>';
			return xmlStr;
		}
		
		public static function objectToInstance(varFlowIcon:Object):FlowIcon{
			var flowIcon:NodeIcon = new NodeIcon(varFlowIcon.iconIndex);
			flowIcon.name = varFlowIcon.name;
			flowIcon.actionTargetBean = varFlowIcon.actionTargetBean;
			return flowIcon;
		}
	}
}