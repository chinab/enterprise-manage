package components
{
	import components.icons.DecisionIcon;
	import components.icons.EndIcon;
	import components.icons.ForkIcon;
	import components.icons.JoinIcon;
	import components.icons.NodeIcon;
	import components.icons.StartIcon;
	import components.icons.TaskIcon;
	
	import events.IconEvent;
	
	import flash.events.ContextMenuEvent;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	
	import mx.collections.ArrayCollection;
	import mx.containers.TitleWindow;
	import mx.controls.Alert;
	import mx.controls.Image;
	import mx.controls.TextInput;
	import mx.core.Application;
	import mx.managers.PopUpManager;
	
	import util.Utils;
	[Event(name=IconEvent.ICON_MOUSE_DOWN, type="events.IconEvent")]
	[Event(name=IconEvent.ICON_MOUSE_UP, type="events.IconEvent")]
	[Event(name=IconEvent.ICON_MOVE, type="events.IconEvent")]

	public class FlowIcon extends Image
	{
		private var _type:int;
//		private var _label: String; //图标上的文字说明
		private var _memo: String; //备注
		private var _selected: Boolean; //图标是否被选中
		private var _identity: String; //图标唯一标识符
		private var _icon: Object; //图像数据
		private var _iconIndex: int; //图像数据
		
		public var inLines:ArrayCollection = new ArrayCollection();
		public var outLines:ArrayCollection = new ArrayCollection();
		
		private var text: TextField = new TextField();
		
		protected var propertiesView:TitleWindow;
		
		// :~getters and setters
		public function get type():int{
			return _type;
		}
		
		public function set type(value:int): void{
			this._type = value;
		}
		
		public function get iconIndex(): int{
			return _iconIndex;
		}
		
		public function set iconIndex(value: int): void{
			this._iconIndex = value;
		}
		
		public function get icon(): Object{
			return _icon;
		}
		
		public function set icon(value: Object): void{
			this._icon = value;
			draw();
		}
		
		public function get memo(): String{
			return _memo;
		}
		
		public function set memo(value: String): void{
			this._memo = value;
			draw();
		}
		
		public function get identity(): String{
			return _identity;
		}
		
		public function set identity(value: String): void{
			this._identity = value;
			draw();
		}
		
		public function get selected(): Boolean{
			return _selected;
		}
		
		public function set selected(value: Boolean): void{
			this._selected = value;
			draw();
		}
//		
//		public function get label(): String{
//			return _label;
//		}
//		
//		public function set label(value: String): void{
//			this._label = value;
//			draw();
//		}
		// end of getters and setters ~:
		
		/**
		 * 构造方法
		 **/
		public function FlowIcon(self:FlowIcon,iconIndex: int, label: String = "节点",
				selected : Boolean = false, name: String = null,
				memo: String = "",TYPE:int=0){
			super();
			this._type = TYPE;
			this._icon = Application.application.icons.dataProvider[iconIndex].icon;
			this._iconIndex = iconIndex;
			if(!setPropertiesView(label)) return;
			if(self!=this){
            	throw new ArgumentError("Can't new Base instance!");
			}
			this._icon = icon;
			this.name = label + Utils.getCode();
//			this._label = this.name;
			this._identity = name;
			this._selected = selected;
			this._memo = memo;
			
			this.source = icon;
			
			text.autoSize = TextFieldAutoSize.LEFT;
			text.x = 0;
			text.y = 30 + 3;
			
			this.addChild(text);
			this.addMenus();
			this.draw();
			this.addEvents();
		}
		
		/**
		 * 为结点添加菜单
		 **/
		private function addMenus(): void{
			var cm:ContextMenu = new ContextMenu();
			cm.hideBuiltInItems();
			var editenableContextmenuitem:ContextMenuItem = new ContextMenuItem("可写字段");
			editenableContextmenuitem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT,function(event:ContextMenuEvent):void{
				propertiesView.title = name+"-属性";
	        	PopUpManager.addPopUp(propertiesView,Application.application.document);
	        	PopUpManager.centerPopUp(propertiesView);
	    	});
			var mentItem1: ContextMenuItem = new ContextMenuItem("删除");
			cm.customItems.push(editenableContextmenuitem);
			cm.customItems.push(mentItem1);
			mentItem1.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, 
				function(e: ContextMenuEvent): void{
					Alert.show("确认删除吗？", "确认", Alert.YES|Alert.NO, null, function(dlg_obj:Object):void{if(dlg_obj.detail == Alert.YES) removeFlowIcon();},null, Alert.YES);
				});
				
			this.contextMenu = cm;
		}
		
		/**
		 * 给出编辑面板
		 * 要用子类来覆盖
		 **/
		protected function setPropertiesView(label: String):Boolean{
			return false;
		}
		
		/**
		 * 给出xml属性结点
		 * 要用子类来覆盖
		 **/
		protected function getXMLProperties():String{
			return "";
		}
		
		/**
		 * 删除结点
		 **/
		public function removeFlowIcon():void{
			this.removeEvents();
			var line:ArrowLine;
			for each(line in outLines){
				removeLine(line,true);
			}
			for each(line in inLines){
				removeLine(line,false);
			}
			outLines.removeAll();
			inLines.removeAll();

			parent.removeChild(this);
			var iconArray:ArrayCollection = Application.application.iconArray;
			iconArray.removeItemAt(iconArray.getItemIndex(this));
		}
		
		private function removeLine(line:ArrowLine,isOut:Boolean):void{
			if(parent.contains(line)){
				parent.removeChild(line);
				//删除index中的线
				var lineArray:ArrayCollection = Application.application.lineArray;
				lineArray.removeItemAt(lineArray.getItemIndex(line));
				
				//删除fromIcon中的线
				if(!isOut){
					lineArray = line.fromIcon.outLines;
					lineArray.removeItemAt(lineArray.getItemIndex(line));
				}
				//删除toIcon中的线
				if(isOut){
					lineArray = line.toIcon.inLines;
					lineArray.removeItemAt(lineArray.getItemIndex(line));
				}
				
				line.fromIcon = null;
				line.toIcon = null;
			}
		}
		
		/**
		 * 生成xml
		 **/
		public function toXml():String{
			return getXMLProperties();
		}
		
		/**
		 * 生成属性列表
		 **/
		public function getProperties(): ArrayCollection{
			var properties:ArrayCollection = new ArrayCollection();
			properties.addItem({name: "name",label:"名称", value: this.name});
//			properties.addItem({name: "label",label:"标签", value: this.label});
			properties.addItem({name: "memo",label:"备注", value: this.memo});
			properties.addItem({name: "x",label:"X坐标", value: this.x});
			properties.addItem({name: "y",label:"Y坐标", value: this.y});
			properties.addItem({name: "selected",label:"选择", value: this.selected});
			return properties;
		}
		
		public function setProperties(rowIndex:int,value:String,tf:TextInput):void{
			switch(rowIndex){
					case 0:
						//从属性对话框中改变图标的name属性时，线条的fromIcon和toIcon都必须要改
						this.name = value;
						break;
//					case 1:
//						this.label = value;
//						break;
					case 1:
						this.memo = value;
						break;
					case 2:
						var n: Number = Number(value);
						if(n < 0) n = 0;
						this.x = n;
						tf.text = n + "";
						break;
					case 3:
						n = Number(value);
						if(n < 0) n = 0;
						this.y = n;
						tf.text = n + "";
						break;
					case 4:
						tf.text = this.selected + "";
						break;
				}
		}
		
		/**
		 * 添加进连线
		 **/
		public function addInLine(line:ArrowLine):void{
			inLines.addItem(line);
		}
		 
		/**
		 * 添加出连线
		 **/
		public function addOutLine(line:ArrowLine):void{
			outLines.addItem(line);
		}
		
		/**
		 * 去除进连线
		 **/
		public function removeInLine(line:ArrowLine):void{
			if(inLines.contains(line))
				inLines.removeItemAt(inLines.getItemIndex(line));
		}
		 
		/**
		 * 去除出连线
		 **/
		public function removeOutLine(line:ArrowLine):void{
			if(outLines.contains(line))
				outLines.removeItemAt(outLines.getItemIndex(line));
		}
		
		/**
		 * 画也结点内容
		 **/
		private function draw(): void{
			this.source = this.icon;
			
			text.htmlText = "<b>" + this.name + "</b>";
		}
		
		/**
		 * 添加事件
		 **/
		private function addEvents(): void{
			this.addEventListener(MouseEvent.MOUSE_DOWN, onIconMouseDown);
			this.addEventListener(MouseEvent.MOUSE_UP, onIconMouseUp);
			this.addEventListener(MouseEvent.MOUSE_MOVE, onIconMove);
		}
		
		/**
		 * 清除事件
		 **/
		private function removeEvents(): void{
			this.removeEventListener(MouseEvent.MOUSE_DOWN, onIconMouseDown);
			this.removeEventListener(MouseEvent.MOUSE_UP, onIconMouseUp);
			this.removeEventListener(MouseEvent.MOUSE_MOVE, onIconMove);
		}
		
		/**
		 * 触发鼠标按下事件
		 **/
		private function onIconMouseDown(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_DOWN);
			e.icon = this;
			this.dispatchEvent(e);
		}
		
		/**
		 * 触发鼠标弹起事件
		 **/
		private function onIconMouseUp(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOUSE_UP);
			e.icon = this;
			this.dispatchEvent(e);
		}
		
		/**
		 * 触发鼠标移动事件
		 **/
		private function onIconMove(event: MouseEvent): void{
			var e: IconEvent= new IconEvent(IconEvent.ICON_MOVE);
			e.icon = this;
			this.dispatchEvent(e);
		}
		
		/**
		 * 结点工厂方法
		 **/
		public static function newInstance(iconIndex:int,iconType:int):FlowIcon{
			switch(iconType){
				case NodeIcon.TYPE:
					return new NodeIcon(iconIndex);
				case TaskIcon.TYPE:
					return new TaskIcon(iconIndex);
				case DecisionIcon.TYPE:
					return new DecisionIcon(iconIndex);
				case ForkIcon.TYPE:
					return new ForkIcon(iconIndex);
				case JoinIcon.TYPE:
					return new JoinIcon(iconIndex);
				case StartIcon.TYPE:
					return new StartIcon(iconIndex);
				case EndIcon.TYPE:
					return new EndIcon(iconIndex);
			}
			return null;
		}
		
		/**
		 * 将持久化的实例激活
		 **/
		public static function objectToInstance(varFlowIcon:Object):FlowIcon{
			var flowIcon:FlowIcon = null;
			switch (varFlowIcon.type){
				case NodeIcon.TYPE:
					flowIcon=NodeIcon.objectToInstance(varFlowIcon);
					break;
				case TaskIcon.TYPE:
					flowIcon=TaskIcon.objectToInstance(varFlowIcon);
					break;
				case DecisionIcon.TYPE:
					flowIcon=DecisionIcon.objectToInstance(varFlowIcon);
					break;
				case ForkIcon.TYPE:
					flowIcon=ForkIcon.objectToInstance(varFlowIcon);
					break;
				case JoinIcon.TYPE:
					flowIcon=JoinIcon.objectToInstance(varFlowIcon);
					break;
				case StartIcon.TYPE:
					flowIcon=StartIcon.objectToInstance(varFlowIcon);
					break;
				case EndIcon.TYPE:
					flowIcon=EndIcon.objectToInstance(varFlowIcon);
					break;
			}
			flowIcon.x = varFlowIcon.x;
			flowIcon.y = varFlowIcon.y;
			flowIcon.memo = varFlowIcon.memo;
			flowIcon.selected = varFlowIcon.selected;
			flowIcon.identity = varFlowIcon.identity;
			return flowIcon;
		}
	}
}