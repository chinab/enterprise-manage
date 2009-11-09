package components.icons
{
	import components.ArrowLine;
	import components.FlowIcon;
	
	import mx.utils.StringUtil;
	
	import view.iconitemviews.ForkPropertiesView;

	public class ForkIcon extends FlowIcon
	{
		public static var TYPE:int = 4;
		public function ForkIcon(iconIndex:int=0, label:String="分叉", selected:Boolean=false, name:String=null, memo:String="")
		{
			super(this, iconIndex, label, selected, name, memo,TYPE);
		}
		
		/**
		 * 给出编辑面板
		 **/
		override protected function setPropertiesView(label: String):Boolean{
			var forkPropertiesView:ForkPropertiesView= new ForkPropertiesView();
			forkPropertiesView.forkIcon = this;
			propertiesView = forkPropertiesView;
			return true;
		}
		
		/**
		 * 给出xml属性结点
		 **/
		override protected function getXMLProperties():String{
			var xmlStr:String ='<fork name="'+name+'">';
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
			xmlStr+='</fork>';
			return xmlStr;
		}
		
		public static function objectToInstance(varFlowIcon:Object):FlowIcon{
			var flowIcon:ForkIcon = new ForkIcon(varFlowIcon.iconIndex);
			flowIcon.name = varFlowIcon.name;
			return flowIcon;
		}
	}
}