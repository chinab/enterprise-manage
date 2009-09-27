package com.xvxv.aclass
{
	import mx.controls.dataGridClasses.DataGridItemRenderer;
	import mx.controls.dataGridClasses.DataGridListData;

	public class CellColorItemRenderer extends DataGridItemRenderer
	{
		private var redText:String ;
		public function CellColorItemRenderer(redText:String)
		{
			super();
			this.redText = redText;
		}
		override public function set data(value:Object):void
		{
			if(value != null)
			{
				var data:XML = XML(value);
				super.data = value;
				var dataField:String = DataGridListData(listData).dataField;
				while(dataField.indexOf("@")!=-1) 
					dataField = dataField.replace("@","");
				if(data.attribute(dataField).toString() == redText) {
					setStyle("color", 0xFF0000);
				}
				else {
					setStyle("color", 0x000000);
				}
			}
		}
	}
}