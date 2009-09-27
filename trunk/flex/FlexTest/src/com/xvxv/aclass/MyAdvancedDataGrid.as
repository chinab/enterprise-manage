package com.xvxv.aclass{
	import flash.display.Sprite;
	
	import mx.controls.AdvancedDataGrid;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;

	public class MyAdvancedDataGrid extends AdvancedDataGrid{
		private var _rowColorFunction:Function;
		private var _columnColorFunction:Function;

		public function MyAdvancedDataGrid(){
			super();
		}
		
		public function set rowColorFunction(f:Function):void{
			this._rowColorFunction = f;
		}
		
		public function get rowColorFunction():Function{
			return this._rowColorFunction;
		}
		
		public function set columnColorFunction(f:Function):void{
			this._columnColorFunction = f;
		}
		
		public function get columnColorFunction():Function{
			return this._columnColorFunction;
		}
		
		override protected function drawRowBackground(s:Sprite, rowIndex:int, y:Number, height:Number, color:uint, dataIndex:int):void{
	    	if( this.rowColorFunction != null ){
	    		if( rowIndex < this.dataProvider.length && rowIndex < this.listItems.length && this.listItems[rowIndex].length>0){
//		    		var data:XML = XmlTools.getXmlItemByIndex(this.dataProvider.source.source,dataIndex);
		    		var data:XML =  this.listItems[rowIndex][0].data;
		    		color = this.rowColorFunction.call(this,data,color);
	    		}else{
	    			color = this.rowColorFunction.call(this,null,color);
	    		}
	    	}
		    super.drawRowBackground(s, rowIndex, y, height, color, dataIndex);
        }
	    
	    override protected function drawColumnBackground(s:Sprite, columnIndex:int, color:uint, column:AdvancedDataGridColumn):void{
	    	s.alpha = 0.4;
	    	color = this.columnColorFunction.call(this,columnIndex,color);
	    	super.drawColumnBackground(s,columnIndex,color,column);
	    }
	}
}