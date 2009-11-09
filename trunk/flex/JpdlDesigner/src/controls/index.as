
			import components.ArrowLine;
			import components.FlowIcon;
			import components.RectBorder;
			
			import events.IconEvent;
			
			import mx.collections.ArrayCollection;
			import mx.controls.Alert;
			import mx.controls.Image;
			import mx.controls.TextInput;
			import mx.events.DataGridEvent;
			import mx.events.ItemClickEvent;
			
			import util.Utils;
			Alert.yesLabel = "是";
            Alert.noLabel = "否";
            Alert.cancelLabel = "取消";
			
			public var iconArray: ArrayCollection = new ArrayCollection();//存储所有的图片
			public var lineArray: ArrayCollection = new ArrayCollection();//存储所有的线条
			[Bindable]
			public var flowData:Object = {'iconArray':iconArray,'lineArray':lineArray,'flowName':'jbpmFlow'+new Date().time};
			private var iconPressable: Boolean = false; //存储图标的按下状态
			
			//画布右键菜单
			[Bindable]
			private var canvasCm: ContextMenu;
			//属性对话框中的数据
			[Bindable]
			private var properties: ArrayCollection = new ArrayCollection();
			
			//右击画布弹出的菜单
			internal function onCanvasContextMenu(event: Event): void{
				canvasCm = new ContextMenu();
				canvasCm.hideBuiltInItems();
				//全选菜单项
				var mi_selectAll: ContextMenuItem = new ContextMenuItem("全选", true);
				canvasCm.customItems.push(mi_selectAll);
				mi_selectAll.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT,
					function(event: ContextMenuEvent): void{
						drawBorders();
					});
				//全不选菜单项
				var mi_selectNotAll: ContextMenuItem = new ContextMenuItem("全不选");
				canvasCm.customItems.push(mi_selectNotAll);
				mi_selectNotAll.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT,
					function(event: ContextMenuEvent): void{
						clearBorders();
					});
				
				flow.contextMenu = canvasCm;
			}
			
			private function newFlow():void{
				Alert.show("是否要保存"+flowData.flowName, "提示", Alert.YES|Alert.NO|Alert.CANCEL, null, function(dlg_obj:Object):void{
					if (dlg_obj.detail == Alert.YES){
						Utils.saveFlow(flowData,onNewFlow);
					}else if (dlg_obj.detail == Alert.NO){
						onNewFlow();
					}
				}, null, Alert.YES);
			}
			
			private function onNewFlow():void{
				iconArray.removeAll();
				lineArray.removeAll();
				flow.removeAllChildren();
				flowData.flowName = 'jbpmFlow'+new Date().time;
				flow.label = flowData.flowName + '-流程图';
			}
			
			//改变大小
			internal function onApplicationReSize(event: Event): void{
				
				if(hdbox != null && btnBar != null && vbox != null && tabBar != null){
					hdbox.width = this.width - 5;
					hdbox.height = this.height - btnBar.height - 30;
					vbox.height = hdbox.height;
					viewstack1.height = vbox.height - tabBar.height
				}
			}
			
			//鼠标点击流程图画布时,画出一个图标,如果指针不在任何图标上,则清除所有图标的蓝色边框
			internal function onFlowCanvasMouseDown(event: MouseEvent): void{
				//如果用户点击的是画布,而不是按钮冒泡过来的事件
				if(!(event.target is FlowIcon)){
					if(icons.selectedItem != null){
						if(icons.selectedIndex != 0 && icons.selectedIndex != 1){
//							var newImage: FlowIcon = new FlowIcon(icons.selectedItem.icon,icons.selectedIndex);
//							newImage.label = icons.selectedItem.label;
							
							var newImage:FlowIcon = FlowIcon.newInstance(icons.selectedIndex,icons.selectedItem.iconType);
							
							//var newImage : Image = new Image();
							//newImage.source = icons.selectedItem.icon;
							//定位图标
							var p: Point = new Point();
							p = flow.globalToLocal(new Point(mouseX, mouseY));//转换坐标
							newImage.x = p.x;
							newImage.y = p.y;
							
							//newImage.addEventListener(MouseEvent.MOUSE_DOWN, onImageMouseDown);
							//newImage.addEventListener(MouseEvent.MOUSE_UP, onImageMouseUp);
							addNewImage(newImage);
						}else{
							this.clearBorders();
							properties.removeAll();
						}
						icons.selectedIndex = 0; //重新选择第一个,不然老是添加了.
					}
				}
			}
			
			private function addNewImage(newImage:FlowIcon):void{
				newImage.addEventListener(IconEvent.ICON_MOUSE_DOWN, onIconMouseDown);
				newImage.addEventListener(IconEvent.ICON_MOUSE_UP, onIconMouseUp);
				newImage.addEventListener(IconEvent.ICON_MOVE, onIconMove);
				
				//添加到图标集合中去
				iconArray.addItem(newImage);
				flow.addChild(newImage);
			}
			
			//当单击流程图画布上的图标时,呈选中状态,并出现发光效果.
			internal function onIconMouseDown(event: IconEvent): void{
				var img: FlowIcon = FlowIcon(event.icon);
				
				//建立连线
				var line: ArrowLine = this.drawLine(img);
				
				
				if(line != null){
					lineArray.addItem(line);
				}
				icons.selectedIndex = 0;
				
				//添加选中效果(发光)
				glow1.target = img;//将效果指定到图标上(编程时不是为图标设置效果)
				glow1.end();
				glow1.play();
				
				this.clearBorders(); //删除所有图标的边框
				
				//判断是否已经加了蓝色边框,如果加了就不需要再加了,只发光就行.
				//img.numChildren == 2的意思是:FlowIcon上已经有了图标和文字,没有边框
				if(img.numChildren == 2){
					//numChildren == 2表示还没有加蓝色边框
					this.drawBorder(img);
				}
				
				//设置属性对话框
				if(this.getSelectedIconCount() == 1)
					this.properties = img.getProperties();
				else
					properties.removeAll();
				
				iconPressable = true;
				//实施拖动
				img.startDrag();
			}
			
			/**
			 * 拖动图标时重画与该图标相关的线条
			 */
			internal function onIconMove(event: IconEvent): void{
				if(iconPressable && event.target is FlowIcon){
					reDrawIcon(FlowIcon(event.target));
					
					this.properties = FlowIcon(event.icon).getProperties();
				}
			}
			
			internal function onIconMouseUp(event: IconEvent): void{
				var img: Image = Image(event.icon);
				glow2.target = img;
				glow2.end();
				glow2.play();
				
				iconPressable = false;
				
				//停止拖动
				img.stopDrag();
			}
			
			internal function onTabIndexChanged(event: ItemClickEvent): void{
				if(event.index == 1){
					if(xmlHTML != null){
						xmlHTML.text = Utils.filter(this.xmlContent());
					}
				}
			}
			
			internal function onXmlHTMLComplete(event: Event): void{
				xmlHTML.text = Utils.filter(this.xmlContent());
			}
			
			/**
			 * 数据网格编辑完成
			 */
			internal function onGridEditEnd(event: DataGridEvent): void{
				var tf: TextInput = TextInput(dgProp.itemEditorInstance);
				var value: String = tf.text;
				//如果只有一个图标被选择
				var icon: FlowIcon = this.hasSelectedIcon();
				if(icon != null){
					icon.setProperties(event.rowIndex,value,tf);
					reDrawIcon(icon);//重画
				}
				
			}
			
			/**
			 * 移动图标或从属性对话框里设置值时重新画一次
			 * 改变线程的XY坐标后会自动重画, 在ArrowLine.cs中已实现
			 */
			private function reDrawIcon(icon:  FlowIcon): void{
				if(icon.x <= 0) icon.x = 0;
				if(icon.y <= 0) icon.y = 0;
				
				//起始的
				var ac: ArrayCollection = icon.outLines;
				var length: int = ac.length;
				var line: ArrowLine = null;
				for(var i: int = 0; i < length; i ++){
					line = ArrowLine(ac.getItemAt(i));
					line.startX = icon.width / 2 + icon.x;
					line.startY = icon.height / 2 + icon.y;
				}
				
				//结束的
				ac = icon.inLines;
				length = ac.length;
				var p: Point = null;
				for(i = 0; i < length; i ++){
					line = ArrowLine(ac.getItemAt(i));
					p = this.getEndDotPosition(line.fromIcon, icon);
					line.endX = p.x;
					line.endY = p.y;
				}
			}
			
			/**
			 * 重新画一次所有元素
			 */
			public function reDrawIconAll(): void{
				var p: Point = null;
				for each(var flowIcon:FlowIcon in iconArray){
					if(flowIcon.x <= 0) flowIcon.x = 0;
					if(flowIcon.y <= 0) flowIcon.y = 0;
				}
				for each(var line:ArrowLine in lineArray){
					line.startX = line.fromIcon.width / 2 + line.fromIcon.x;
					line.startY = line.fromIcon.height / 2 + line.fromIcon.y;
					p = this.getEndDotPosition(line.fromIcon, line.toIcon);
					line.endX = p.x;
					line.endY = p.y;
				}
			}
			
			/**
			 * 重新画一次所有元素
			 */
			public function activationAll(varFlowData:Object): void{
				var flowIcon:FlowIcon = null;
				var arrowLine:ArrowLine = null;
				
				var varObject:Object;
				flow.removeAllChildren();
				iconArray.removeAll();
				lineArray.removeAll();
				flowData.flowName = varFlowData.flowName;
				flow.label = flowData.flowName + '-流程图';
				
				var varIconArray:ArrayCollection = varFlowData.iconArray;
				var varLineArray:ArrayCollection = varFlowData.lineArray;
				for each(varObject in varIconArray){
					flowIcon = FlowIcon.objectToInstance(varObject);
					addNewImage(flowIcon);
				}
				
				for each(varObject in varLineArray){
					arrowLine = ArrowLine.objectToInstance(varObject);
					flow.addChild(arrowLine);
					lineArray.addItem(arrowLine);
				}
			}
			
			/**
			 * 为指定图标添加边框
			 */
			private function drawBorder(img: FlowIcon): void{
				var rect: RectBorder = new RectBorder(2, 0xFF0000, img.width, img.height, 0x000000, 2,2);
				img.selected = true; //保存选中状态
				img.addChild(rect);
			}
			
			/**
			 * 删除画布上所有的图标边框
			 */
			private function drawBorders():void{
				var length: int = flow.numChildren;
				var icon: Object = null;
				properties.removeAll();
				for(var i: int = 0; i < length; i ++){
					icon = flow.getChildAt(i);
					if(icon is FlowIcon){
						this.drawBorder(FlowIcon(icon));
					}
				}
			}
			
			//清除所有图标的边框
			private function clearBorders(): void{
				var currentImage: FlowIcon = null;
				for(var i: int = 0; i < flow.numChildren; i ++){
					if(flow.getChildAt(i) is mx.controls.Image){
						//找到当前的图标
						currentImage = FlowIcon(flow.getChildAt(i));
						//找到当前图标带蓝色方框的, 清除该方框
						if(this.hasBorderSprite(currentImage)){
							for(var j: int = currentImage.numChildren - 1; j >= 0; j --){
								if(this.hasBorderSprite(currentImage)){
									currentImage.selected = false;
									currentImage.removeChildAt(j);
								}
							}
						}
					}
				}
			}
			
			//检查Image内有没有BorderSprite,以便判断是否有蓝色边框
			private function hasBorderSprite(iconImage: Image): Boolean{
				for(var j: int = iconImage.numChildren - 1; j >= 0 ; j --){
					if(iconImage.getChildAt(j) is RectBorder){
						return true;
					}
				}
				return false;
			}
			
			/**
			 * 判断在画布内是否有选中的图标,如果有,则返回该图标,否则返回null
			 * 如果同时有多个图标被选择，则以第一个为准
			 */
			private function hasSelectedIcon(): FlowIcon{
				var icon: FlowIcon = null;
				for(var i: int = 0; i < iconArray.length; i ++){
					icon = FlowIcon(iconArray[i]);
					if(icon.selected){
						return icon;
					}
				}
				return null;
			}
			
			/**
			 * 画线
			 **/
			private function drawLine(img: FlowIcon): ArrowLine{
				//先判断选择的是选择图标
				if(icons.selectedIndex == 1){
					//判断之前是否有选择的起始图标
					var beforeSelectedIcon: FlowIcon = this.hasSelectedIcon();
					
					if(beforeSelectedIcon != null && beforeSelectedIcon != img){
						if(hasLine(beforeSelectedIcon, img)){
							Alert.show("对不起,直线已存在!", "错误");
							return null;
						}
						//在当前图标和之前选中的图标之间画线条
						//计算中心点
						var centerStartX: int = beforeSelectedIcon.width / 2;
						var centerStartY: int = beforeSelectedIcon.height / 2;
						
						var p: Point = this.getEndDotPosition(beforeSelectedIcon, img);
						
						var line: ArrowLine = new ArrowLine(
							centerStartX + beforeSelectedIcon.x,
							centerStartY + beforeSelectedIcon.y,
							p.x,
							p.y,
							0x000000,
							1,
							3,
							0x000000
						);
						
						//从..开始,到..结束
						line.fromIcon = beforeSelectedIcon;
						line.toIcon = img;
						line.name = "line" + Utils.getCode();
						flow.addChild(line);
						return line;
					}
				}
				return null;
			}
			
			/**
			 * 画线时获取线条终点的坐标
			 * 判断终点图标和起始图标的相对位置,共九个
			 * 位置:左上,中上,右上;左中,中中,右中,左下,中下,右下
			 */
			public function getEndDotPosition(startIcon: FlowIcon, endIcon: FlowIcon): Point{
				var space: Number = 50;
				
				//计算起始点的中心点坐标
				var startCenterX: Number = startIcon.x + startIcon.width / 2;
				var startCenterY: Number = startIcon.y + startIcon.height / 2;
				
				//计算终点的中心点坐标
				var endCenterX: Number = endIcon.x + endIcon.width / 2;
				var endCenterY: Number = endIcon.y + endIcon.height / 2;
				
				var p: Point = new Point();
				
				//1左上
				if(endCenterX <= startCenterX - space && endCenterY <= startCenterY - space){
					p.x = endIcon.x + endIcon.width;
					p.y = endIcon.y + endIcon.height;
					//trace("左上");
				}
				//2中上
				else if(endCenterX >= startCenterX - space && endCenterX <= startCenterX + space
					&& endCenterY < startCenterY - space){
					p.x = endIcon.x + endIcon.width / 2;
					p.y = endIcon.y + endIcon.height;
					//trace("中上");
				}
				//3右上
				else if(endCenterX >= startCenterX + space && endCenterY <= startCenterY - space){
					p.x = endIcon.x;
					p.y = endIcon.y + endIcon.height;
					//trace("右上");
				}
				//4中左
				else if(endCenterX <= startCenterX - space && 
					endCenterY >= startCenterY - space && endCenterY <= startCenterY + space){
					p.x = endIcon.x + endIcon.width;
					p.y = endIcon.y + endIcon.height / 2;
					//trace("中左");
				}
				//5中中
				else if(endCenterX >= startCenterX - space && endCenterX <= startCenterX + space
					&& endCenterY >= startCenterY - space && endCenterY <= startCenterY + space){
					p.x = endIcon.x;
					p.y = endIcon.y;
					//trace("中中");
				}
				//6中右
				else if(endCenterX >= startCenterX + space && 
					endCenterY >= startCenterY - space && endCenterY <= startCenterY + space){
					p.x = endIcon.x;
					p.y = endIcon.y + endIcon.height / 2;
					//trace("中右");
				}
				//7下左
				else if(endCenterX <= startCenterX - space && endCenterY >= startCenterY + space){
					p.x = endIcon.x + endIcon.width;
					p.y = endIcon.y;
					//trace("下左");
				}
				//8下中
				else if((endCenterX >= startCenterX - space && endCenterX <= startCenterX + space) &&
					endCenterY >= startCenterY + space){
					p.x = endIcon.x + endIcon.width / 2;
					p.y = endIcon.y;
					//trace("下中");
				}
				//9右中
				else if(endCenterX >= startCenterX + space && endCenterY >= startCenterY + space){
					p.x = endIcon.x;
					p.y = endIcon.y;
					//trace("右中");
				}
				
				return p;
			}
			
			//判断两个图标之间是否已经有直线
			public function hasLine(fromIcon: FlowIcon, toIcon: FlowIcon): ArrowLine{
				var line: ArrowLine = null;
				var length: int = lineArray.length;
				for(var i:int = 0; i < length; i ++){
					line = ArrowLine(lineArray[i]);
					if(line.fromIcon == fromIcon && line.toIcon == toIcon){
						return line;
					}
					if(line.toIcon == fromIcon && line.fromIcon == toIcon){
						return line;
					}
				}
				return null;
			}
			
			
			/**
			 * 找到指定图标上所有的线条集合
			private function getLines(iconName: String, start: Boolean): ArrayCollection{
				var ac: ArrayCollection = new ArrayCollection();
				//遍历线条
				var length: int = lineArray.length;
				for(var i: int = 0; i < length; i ++){
					var obj: * = lineArray[i];
					if(start && obj.fromIcon == iconName){
						ac.addItem(obj);
					}else if((!start) && (obj.toIcon == iconName)){
						ac.addItem(obj);						
					}
				}
				return ac;
			}
			 */
			
			/**
			 * 删除与指定图标关联的所有线条
			private function removeLines(icon: FlowIcon): void{
				var length: int = lineArray.length;
				var line: ArrowLine = null;
				for(var i: int = length - 1; i >= 0 ; i --){
					line = lineArray[i];
					if(line.fromIcon == icon.name || line.toIcon == icon.name){
						flow.removeChild(line);
						lineArray.splice(i, 1); //同时删除数组中对应的元素
					}
				}
			}
			 */
			
			/**
			 * 删除多个图标
			 */
			private function removeIcons(icons: ArrayCollection): void{
				var icon: FlowIcon = null;
				var length: int = icons.length;
				for(var i: int = 0; i < length; i ++){
					icon = FlowIcon(icons.getItemAt(i));
//					this.removeIcon(icon);
					icon.removeFlowIcon();
				}
			}
			
			/**
			 * 删除一个图标
			private function removeIcon(icon: FlowIcon): void{
				var length: int = iconArray.length;
				var curIcon: FlowIcon = null;
				//删除事件
				icon.removeEventListener(IconEvent.ICON_MOUSE_DOWN, onIconMouseDown);
				icon.removeEventListener(IconEvent.ICON_MOUSE_UP, onIconMouseUp);
				icon.removeEventListener(IconEvent.ICON_MOVE, onIconMove);
				flow.removeChild(icon); //删除图标
				//删除该图标上的所有直线
				this.removeLines(icon);
				
				//删除数组中的元素
				for(var i: int = length - 1; i >= 0; i --){
					curIcon = iconArray[i];
					if(curIcon.name == icon.name){
						iconArray.splice(i, 1);
					}
				}
				
				//清空属性对话框
				properties.removeAll();
			}
			 */
			
			/**
			 * 删除画布上的所有选中图标
			 */
			private function removeIconsFlow(): void{
				Alert.show("确认删除吗？", "确认", Alert.YES|Alert.NO, null, removeIconsFlowHandler, null, Alert.YES);
			}
			/**
			 * 删除画布上的所有选中图标(是上面方法的延续)
			 */
			private function removeIconsFlowHandler(dlg_obj:Object):void{
				if (dlg_obj.detail == Alert.YES){
					var length: int = flow.numChildren;
					var obj: Object = null;
					for(var i: int = length - 1; i >= 0; i --){
						obj = flow.getChildAt(i);
						if(obj is FlowIcon){
							if(obj.selected){
								FlowIcon(obj).removeFlowIcon();
							}
						}
					}
				}
				properties.removeAll();
			}
		
			/**
			 * 返回选中的图标个数
			 */
			private function getSelectedIconCount(): int{
				var length: int = iconArray.length;
				var total: int = 0;
				for(var i: int = 0; i < length; i ++){
					if(iconArray[i].selected){
						total ++;
					}
				}
				return total;
			}
			
			/**
			 * 找到指定图标的关联图标(只找与之关联的起点图标)
			 */
			private function getRelationIcons(icon: FlowIcon): ArrayCollection{
				var ac: ArrayCollection = new ArrayCollection();
				//先找到该图标上的所有线条（该图标为终点），再找与该线条相关的起点
				var length: int = lineArray.length;
				var line: ArrowLine = null;
				for(var i: int = 0; i < length; i ++){
					line = ArrowLine(lineArray[i]);		
					if(line.toIcon == icon){
						ac.addItem(line.fromIcon);
					}
				}
				return ac;
			}
			
			public function xmlContent():String{
				var xmlStr: String = '<?xml version="1.0" encoding="UTF-8"?>'+
					'<process-definition  xmlns=""  name="'+this.flowData.flowName+'">';
				for each(var flowIcon:FlowIcon in iconArray){
					xmlStr += flowIcon.toXml();
				}
				xmlStr += '</process-definition>';
				return xmlStr;
			}

			//test
			private function printIconsStatus(): void{
				for(var i: int = 0; i < iconArray.length; i ++){
					if(iconArray[i] is FlowIcon){
						trace(FlowIcon(iconArray[i]).selected);
					}
				}
			}
			
