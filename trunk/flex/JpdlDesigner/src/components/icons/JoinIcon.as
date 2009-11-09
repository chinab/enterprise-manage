package components.icons
{
	import components.ArrowLine;
	import components.FlowIcon;
	
	import mx.utils.StringUtil;
	
	import view.iconitemviews.JoinPropertiesView;

	public class JoinIcon extends FlowIcon
	{
		public static var TYPE:int = 5;
		
		private var _discriminator:Boolean = false;
		
		public function set discriminator(v:Boolean):void{
			this._discriminator = v;
		}
		
		public function JoinIcon(iconIndex:int=0, label:String="聚合", selected:Boolean=false, name:String=null, memo:String="")
		{
			super(this, iconIndex, label, selected, name, memo,TYPE);
		}
		
		public function get discriminator():Boolean{
			return this._discriminator;
		}
		
		/**
		 * 给出编辑面板
		 * 要用子类来覆盖
		 **/
		override protected function setPropertiesView(label: String):Boolean{
			var joinPropertiesView:JoinPropertiesView = new JoinPropertiesView();
			joinPropertiesView.joinIcon = this;
			propertiesView = joinPropertiesView;
			return true;
		}
		
		/**
		 * 给出xml属性结点
		 * 要用子类来覆盖
		 **/
		override protected function getXMLProperties():String{
			var xmlStr:String = '<join name="'+name+'">'
			if(discriminator){
				xmlStr+='<event type="node-enter">'
						+'<script>'
							+'org.jbpm.graph.node.Join join= (org.jbpm.graph.node.Join)node; join.setDiscriminator(true);'
						+'</script>'
					+'</event>';
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
			xmlStr+='</join>';
			return xmlStr;
		}
		
		public static function objectToInstance(varFlowIcon:Object):FlowIcon{
			var flowIcon:JoinIcon = new JoinIcon(varFlowIcon.iconIndex);
			flowIcon.name = varFlowIcon.name;
			flowIcon.discriminator = Boolean(varFlowIcon.discriminator);
			return flowIcon;
		}
	}
}