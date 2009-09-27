package com.xvxv.aclass
{
	import mx.core.IFactory;
	
	public class MyIFactoryOfHeader implements IFactory
	{
		private var _loadFun:Function=null;
		public function MyIFactoryOfHeader(loadFun:Function){
			super();
			this._loadFun = loadFun;
		}
		
		public function newInstance():*{
	       	var resultRenderer:myHeaderRenderer = new myHeaderRenderer();
	       	resultRenderer.loadFun = _loadFun;
	       	return resultRenderer;
		}
	}
}