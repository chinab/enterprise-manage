package com.xvxv.aclass
{
	import mx.core.IFactory;
	
	public class MyIFactory implements IFactory
	{
		public function MyIFactory(){
			super();
		}
		
		public function newInstance():*{
	       	return new myItemRenderer();
		}
	}
}