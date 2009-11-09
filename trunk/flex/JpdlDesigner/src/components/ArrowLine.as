package components
{
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.core.UIComponent;
	
	public class ArrowLine extends UIComponent
	{
		private var _startX: int; //起点X坐标
		private var _startY: int;//起点Y坐标
		private var _endX: int; //终点X坐标
		private var _endY: int; //终点Y坐标
		private var _radio: int; //小圆的半径
		private var _thickness: Number; //线条粗细
		
		private var _fromIcon: FlowIcon; //从..开始 名称
		private var _toIcon: FlowIcon; //到..结束 名称
		
		private var _lineColor: uint;//线条颜色
		private var _circleColor: uint; //尾部小圆的颜色
		private var _action:String;
		
		public function get action():String{
			return _action;
		}
		
		public function set action(v:String):void{
			_action = v;
		}
		
		public function get circleColor(): uint{
			return _circleColor;
		}
		
		public function set circleColor(value: uint): void{
			this._circleColor = value;
		}
		
		public function get toIcon(): FlowIcon{
			return _toIcon;
		}
		
		public function set toIcon(value: FlowIcon): void{
			if(value==null){
				this._fromIcon = null;
				return;
			}
			if(_fromIcon==value && _fromIcon!=null){
				Alert.show("对不起,不能流向自己,请选择其他结点!", "错误");
				return;
			}
			var line:ArrowLine = Application.application.hasLine(_fromIcon,value)
			if(line){
				if(line==this){
					return;
				}
				Alert.show("对不起,直线已存在!", "错误");
				return;
			}
			if(_toIcon)
				_toIcon.removeInLine(this);
			if(value)
				value.addInLine(this);
			this._toIcon = value;
		}
		
		public function get fromIcon(): FlowIcon{
			return _fromIcon;
		}
		
		public function set fromIcon(value: FlowIcon): void{
			if(value==null){
				this._fromIcon = null;
				return;
			}
			if(_fromIcon)
				_fromIcon.removeOutLine(this);
			if(value)
				value.addOutLine(this);
			this._fromIcon = value;
		}
		
		public function get thickness(): Number{
			return _thickness;
		}
		
		public function set thickness(value: Number): void{
			this._thickness = value;
			draw();
		}
		
		public function get radio(): int{
			return _radio;
		}
		
		public function set radio(value: int): void{
			this._radio = value;
			draw();
		}
		
		public function get lineColor(): uint{
			return _lineColor;
		}
		
		public function set lineColor(value: uint): void{
			this._lineColor = value;
			draw();
		}
		
		public function get startX(): int{
			return _startX;
		}
		
		public function set startX(value: int): void{
			this._startX = value;
			draw();
		}
		
		public function get startY(): int{
			return _startY;
		}
		
		public function set startY(value: int): void{
			this._startY = value;
			draw();
		}
		
		public function get endX(): int{
			return _endX;
		}
		
		public function set endX(value: int): void{
			this._endX = value;
			draw();
		}
		
		public function get endY(): int{
			return _endY;
		}
		
		public function set endY(value: int): void{
			this._endY = value;
			draw();
		}
		
//		/**
//		 * 生成xml
//		 **/
//		public function toXml():String{
//			return Utils.space(4) + "&lt;" + Utils.nodeHTML("line") + 
//					" " + Utils.attributeHTML("name") + "=\"" +  this.name +
//					"\" " + Utils.attributeHTML("startX") + "=\"" + this.startX + 
//					"\" " + Utils.attributeHTML("startY") + "=\"" +  this.startY +
//					"\" " + Utils.attributeHTML("endX") + "=\"" + this.endX +
//					"\" " + Utils.attributeHTML("endY") + "=\"" + this.endY +
//					"\" " + Utils.attributeHTML("fromIcon") + "=\"" + this.fromIcon +
//					"\" " + Utils.attributeHTML("toIcon") + "=\"" + this.toIcon +
//					"\"/&gt;";
//		}
		
		public function ArrowLine(startX: int = 0,
			startY: int = 0,
			endX: int = 0,
			endY: int = 0,
			lineColor: uint = 0x000000,
			thickness: Number = 0.5,
			radio: int = 0,
			circleColor: uint = 0xFF0000)
		{
			super();
			this._endX = endX;
			this._endY = endY;
			this._startX = startX;
			this._startY = startY;
			this._lineColor = lineColor;
			this._radio = radio;
			this._thickness = thickness;
			this._circleColor = circleColor;
			
			this.draw();
		}
		
		//画直线,并在尾端画一个小圆
		public function draw(): void{
			this.graphics.clear();//清除前面画的东东
			
			//画线
			this.graphics.lineStyle(thickness, lineColor);
//			this.graphics.moveTo(startX, startY);
//			this.graphics.lineTo(endX, endY);
			
			//在线条尾部画小圆
			
			this.graphics.lineStyle(1, circleColor);
			//this.graphics.drawCircle(endX, endY, radio);
			//this.graphics.moveTo(endX,endY);
			//this.graphics.lineTo(endX-10,endY-10);
			var startX:Number = startX;
            var startY:Number = startY;
            var endX:Number = endX;
            var endY:Number = endY;
           
            var arrowLength : Number = 10;
            var arrowAngle : Number = Math.PI / 5;
            var lineAngle : Number;
            if(endX - startX != 0)                          
                    lineAngle = Math.atan((endY - startY) / (endX - startX));
            else{
                    if(endY - startY < 0)
                            lineAngle = Math.PI / 2;
                    else
                            lineAngle = 3 * Math.PI / 2;
            }                              
            if(endY - startY >= 0 && endX - startX <= 0){
                    lineAngle = lineAngle + Math.PI;
            }else if(endY - startY <= 0 && endX - startX <= 0){
                    lineAngle = lineAngle + Math.PI;
            }
           //定义三角形
            var angleC : Number = arrowAngle;
            var rimA : Number = arrowLength;
            var rimB : Number = Math.pow(Math.pow(endY - startY,2) + Math.pow(endX - startX,2),1/2);
            var rimC : Number = Math.pow(Math.pow(rimA,2) + Math.pow(rimB,2) - 2 * rimA * rimB * Math.cos(angleC),1/2);
            var angleA : Number = Math.acos((rimB - rimA * Math.cos(angleC)) / rimC);
           
            var leftArrowAngle : Number = lineAngle + angleA;
            var rightArrowAngle : Number = lineAngle - angleA;                      
            var leftArrowX : Number = startX + rimC * Math.cos(leftArrowAngle);
            var leftArrowY : Number = startY + rimC * Math.sin(leftArrowAngle);                    
            var rightArrowX : Number = startX + rimC * Math.cos(rightArrowAngle);
            var rightArrowY : Number = startY + rimC * Math.sin(rightArrowAngle);
   
            this.graphics.moveTo(endX, endY);
            this.graphics.lineTo(leftArrowX, leftArrowY);
            this.graphics.moveTo(endX, endY);
            this.graphics.lineTo(rightArrowX, rightArrowY);
            
            this.graphics.moveTo(startX, startY);
			this.graphics.lineTo(endX, endY);
          
		}
		
		/**
		 * 将持久化的实例激活
		 **/
		public static function objectToInstance(varArrowLine:Object):ArrowLine{
			var fromIcon:FlowIcon = findIcon(varArrowLine.fromIcon.name);
			var toIcon:FlowIcon = findIcon(varArrowLine.toIcon.name);
			
			var line: ArrowLine = new ArrowLine(
				varArrowLine.startX,
				varArrowLine.startY,
				varArrowLine.endX,
				varArrowLine.endY,
				varArrowLine.lineColor,
				varArrowLine.thickness,
				varArrowLine.radio,
				varArrowLine.circleColor
			);
			line.action = varArrowLine.action;
			
			line.fromIcon = fromIcon;
			line.toIcon = toIcon;
			line.name = varArrowLine.name;
			return line;
		}
		
		/**
		 * 根据图标的名称找到该图标
		 */
		private static function findIcon(name: String): FlowIcon{
			var iconArray:ArrayCollection = Application.application.iconArray;
			var length: int = iconArray.length;
			for(var i: int = 0; i < length; i ++){
				if(iconArray[i].name == name){
					return iconArray[i];
				}
			}
			return null;
		}
	}
}

