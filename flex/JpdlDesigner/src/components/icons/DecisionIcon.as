package components.icons
{
	import components.ArrowLine;
	import components.FlowIcon;
	
	import mx.utils.StringUtil;
	
	import view.iconitemviews.DecisionPropertiesView;

	public class DecisionIcon extends FlowIcon
	{
		public static var TYPE:int = 3;
		private var _handlerTargetBean:String = "";

		public function set handlerTargetBean(v:String):void{
			_handlerTargetBean = v;
		}
		
		public function get handlerTargetBean():String{
			return _handlerTargetBean;
		}
		
		public function DecisionIcon(iconIndex:int=0, label:String="判定", selected:Boolean=false, name:String=null, memo:String="")
		{
			super(this, iconIndex, label, selected, name, memo,TYPE);
		}
		
		/**
		 * 给出编辑面板
		 * 要用子类来覆盖
		 **/
		override protected function setPropertiesView(label: String):Boolean{
			var decisionPropertiesView:DecisionPropertiesView = new DecisionPropertiesView();
			decisionPropertiesView.decisionIcon = this;
			propertiesView = decisionPropertiesView;
			return true;
		}
		
		/**
		 * 给出xml属性结点
		 * 要用子类来覆盖
		 **/
		override protected function getXMLProperties():String{
			var xmlStr:String ='<decision name="'+name+'">'
				+'<handler config-type="bean" '
      					+'class="org.springmodules.workflow.jbpm31.JbpmHandlerProxy">'
    					+'<targetBean>'+_handlerTargetBean+'</targetBean>'
				+'</handler>';
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
			xmlStr+='</decision>';
			return xmlStr;
		}
		
		public static function objectToInstance(varFlowIcon:Object):FlowIcon{
			var flowIcon:DecisionIcon = new DecisionIcon(varFlowIcon.iconIndex);
			flowIcon.name = varFlowIcon.name;
			flowIcon.handlerTargetBean = varFlowIcon.handlerTargetBean;
			return flowIcon;
		}
	}
}