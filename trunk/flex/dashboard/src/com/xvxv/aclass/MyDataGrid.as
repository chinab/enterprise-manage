package com.xvxv.aclass
{
	import flash.display.Sprite;
	
	import mx.controls.DataGrid;

	public class MyDataGrid extends DataGrid
	{
		public function MyDataGrid()
		{
			super();
		}
		
		override protected function drawRowBackground(s:Sprite, rowIndex:int, y:Number, height:Number, color:uint, dataIndex:int):void{
    		if( rowIndex < this.dataProvider.length && rowIndex < this.listItems.length && this.listItems[rowIndex].length>0){
	    		var data:XML =  this.listItems[rowIndex][0].data;
	    		if(data.@red==true)
	    			color = 0xFF6600;
    		}
		    super.drawRowBackground(s, rowIndex, y, height, color, dataIndex);
        }
	}
}