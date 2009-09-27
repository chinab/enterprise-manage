package com.erp.reponser
{
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	
	public class DeleteReponser  implements IResponder
	{
		private var testWebservice:hessionTest = null;
		
		public function DeleteReponser(testWebservice:hessionTest)
		{
			this.testWebservice = testWebservice;
		}
		public function result(data:Object):void {
			testWebservice.loadData();
        }
           
        public function fault(data:Object):void {
            Alert.show("error");
        }
	}
}