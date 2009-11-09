package components.icons
{
	import components.FlowIcon;
	
	import view.iconitemviews.EndPropertiesView;

	public class EndIcon extends FlowIcon
	{
		public static var TYPE:int = 7;
		public function EndIcon(iconIndex:int=0, label:String="结束", selected:Boolean=false, name:String=null, memo:String="")
		{
			super(this, iconIndex, label, selected, name, memo,TYPE);
		}
		
		/**
		 * 给出编辑面板
		 * 要用子类来覆盖
		 **/
		override protected function setPropertiesView(label: String):Boolean{
			var endPropertiesView:EndPropertiesView = new EndPropertiesView();
			endPropertiesView.endIcon = this;
			propertiesView = endPropertiesView;
			return true;
		}
		
		/**
		 * 给出xml属性结点
		 * 要用子类来覆盖
		 **/
		override protected function getXMLProperties():String{
			return '<end-state name="'+name+'"></end-state>';
		}
		
		public static function objectToInstance(varFlowIcon:Object):FlowIcon{
			var flowIcon:EndIcon = new EndIcon(varFlowIcon.iconIndex);
			flowIcon.name = varFlowIcon.name;
			return flowIcon;
		}
	}
}