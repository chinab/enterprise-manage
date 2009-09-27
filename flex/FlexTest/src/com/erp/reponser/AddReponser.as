package com.erp.reponser
{
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	
	public class AddReponser  implements IResponder
	{
		private var testWebservice:hessionTest = null;
		public function AddReponser(testWebservice:hessionTest)
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