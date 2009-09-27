package com.xvxv.aclass
{
	import mx.core.IFactory;
	
	public class CellColorIFactory implements IFactory
	{
		private var redText:String;
		public function CellColorIFactory(redText:String){
			super();
			this.redText = redText;
		}
		
		public function newInstance():*{
	       	return new CellColorItemRenderer(redText);
		}
	}
}