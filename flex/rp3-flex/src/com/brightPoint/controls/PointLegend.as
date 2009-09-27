package com.brightPoint.controls
{
	import mx.controls.Label;
	import mx.core.Container;
	import mx.core.UIComponent;

	public class PointLegend extends Container
	{
		private var _colors:Array = [0x0000FF,0x8B008B,0x1E90FF,0x218868,0xCD2626,0x8B2252,0x7FFF00];
		private var _names:Array ;
		private var _points:UIComponent ;
		private var _fontSize:Number;
		public function PointLegend()
		{
			super();
			this.clipContent=false; 
		}
		
		public function set colors(param:Array):void{
			this._colors = param;
		}
		public function set names(param:Array):void{
			this._names = param;
		}
		public function set fontSize(param:Number):void{
			this._fontSize = param;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
            super.updateDisplayList(unscaledWidth, unscaledHeight);
    		addChild(_points);
            for(var i:int=0;i<_names.length;i++){
				var label:Label = new Label();
				label.text = _names[i].toString();
				label.x = _fontSize+2;
				label.y = i*(_fontSize+2);
				addChild(label);
				
				_points.graphics.beginFill(_colors[i],1);
				_points.graphics.drawCircle(0, i*(_fontSize+2), _fontSize/2);
				_points.graphics.endFill();
			}
  		}
  		
	}
}