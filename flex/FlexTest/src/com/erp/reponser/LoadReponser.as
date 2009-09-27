package com.erp.reponser
{
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	import mx.rpc.events.ResultEvent;
	
	public class LoadReponser  implements IResponder
	{
		public var employeeDG:DataGrid;
		public function LoadReponser(employeeDG:DataGrid)
		{
			this.employeeDG = employeeDG;
		}
		public function result(data:Object):void {
            var event:ResultEvent = data as ResultEvent;
			var array:Array = event.result as Array;
			employeeDG.dataProvider = array;
        }
           
        public function fault(data:Object):void {
            Alert.show("error");
        }
	}
}