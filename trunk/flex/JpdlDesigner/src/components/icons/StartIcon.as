package components.icons
{
	import components.ArrowLine;
	import components.FlowIcon;
	
	import mx.utils.StringUtil;
	
	import view.iconitemviews.StartPropertiesView;

	public class StartIcon extends FlowIcon
	{
		public static var TYPE:int = 6;
		
		public function StartIcon(iconIndex:int=0,label:String="开始", selected:Boolean=false, name:String=null, memo:String="")
		{
			super(this, iconIndex, label, selected, name, memo,TYPE);
		}
		
		/**
		 * 给出编辑面板
		 * 要用子类来覆盖
		 **/
		override protected function setPropertiesView(label: String):Boolean{
			var startPropertiesView:StartPropertiesView= new StartPropertiesView();
			startPropertiesView.startIcon = this;
			propertiesView = startPropertiesView;
			return true;
		}
		
		/**
		 * 给出xml属性结点
		 * 要用子类来覆盖
		 **/
		override protected function getXMLProperties():String{
//			return Utils.space(4) + "&lt;" + Utils.nodeHTML("node") + 
//						" " + Utils.attributeHTML("name") + "=\"" + this.name + 
//						"\" " + Utils.attributeHTML("selected") + "=\"" + this.selected +
//						"\" " + Utils.attributeHTML("memo") + "=\"" + this.memo +
//						"\" " + Utils.attributeHTML("x") + "=\"" + this.x +
//						"\" " + Utils.attributeHTML("y") + "=\"" + this.y +
//						"\"/&gt;";
			var xmlStr:String ='<start-state name="'+name+'">';
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
			xmlStr+='</start-state>';
			return xmlStr;
		}
		
		/**
		 * 将持久化的实例激活
		 **/
		public static function objectToInstance(varFlowIcon:Object):FlowIcon{
			var flowIcon:StartIcon = new StartIcon(varFlowIcon.iconIndex);
			flowIcon.name = varFlowIcon.name;
			return flowIcon;
		}
	}
}