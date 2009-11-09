package components.icons
{
	import components.ArrowLine;
	import components.FlowIcon;
	import components.bean.TaskBean;
	
	import mx.collections.ArrayCollection;
	import mx.utils.StringUtil;
	
	import view.iconitemviews.TaskPropertiesView;

	public class TaskIcon extends FlowIcon
	{
		public static var TYPE:int = 2;
		
		private var _signal:String = "last";
		private var _taskBeanArray:ArrayCollection = new ArrayCollection();
		
		[Bindable]
		public function set signal(v:String):void{
			_signal = v;
		}
		
		public function get signal():String{
			return _signal;
		}
		
		public function set taskBeanArray(v:ArrayCollection):void{
			_taskBeanArray = v;
		}
		
		public function get taskBeanArray():ArrayCollection{
			return _taskBeanArray;
		}
				
		public function TaskIcon(iconIndex:int=0, label:String="任务", selected:Boolean=false, name:String=null, memo:String="")
		{
			super(this, iconIndex, label, selected, name, memo,TYPE);
		}
		
		/**
		 * 给出编辑面板
		 * 要用子类来覆盖
		 **/
		override protected function setPropertiesView(label: String):Boolean{
			var taskPropertiesView:TaskPropertiesView = new TaskPropertiesView();
			taskPropertiesView.taskIcon = this;
			propertiesView = taskPropertiesView;
			return true;
		}
		
		/**
		 * 给出xml属性结点
		 * 要用子类来覆盖
		 **/
		override protected function getXMLProperties():String{
			var xmlStr:String = '<task-node name="'+name+'" signal="'+signal+'">';
			for each(var taskBean:TaskBean in taskBeanArray){
				xmlStr+='<task name="'+taskBean.name+'">'
					+'<assignment config-type="bean" '
		      			+'class="org.springmodules.workflow.jbpm31.JbpmHandlerProxy">'
		    			+'<targetBean>'+taskBean.assignment+'</targetBean>'
					+'</assignment>'
				+'</task>';
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
			xmlStr+='</task-node>';
			return xmlStr;
		}
		
		/**
		 * 将持久化的实例激活
		 **/
		public static function objectToInstance(varFlowIcon:Object):FlowIcon{
			var flowIcon:TaskIcon = new TaskIcon(varFlowIcon.iconIndex);
			flowIcon.name = varFlowIcon.name;
			flowIcon.signal = varFlowIcon.signal;
			for each(var varTaskBean:Object in varFlowIcon.taskBeanArray){
				var taskBean:TaskBean = new TaskBean(varTaskBean.name,varTaskBean.assignment);
				flowIcon.taskBeanArray.addItem(taskBean);
			}
			return flowIcon;
		}
	}
}