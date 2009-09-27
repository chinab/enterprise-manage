package com.erp.pojo {
	
	import mx.rpc.IResponder;
	import mx.rpc.events.ResultEvent;

	public class BaseService implements IResponder {
		
		protected var resultCallbackFunction : Function;
		
		protected var faultCallbackFunction : Function;
		
		public function BaseService(resultCallback:Function=null, faultCallback:Function=null) {
			if (resultCallback == null) {
				resultCallbackFunction = defaultFunction;
			} else {
				resultCallbackFunction = resultCallback;
			}
			if (faultCallback == null) {
				faultCallbackFunction = defaultFunction;
			} else {
				faultCallbackFunction = faultCallback;
			}
		}
		
		public function result(data:Object):void {
			var event:ResultEvent = data as ResultEvent;
			resultCallbackFunction(event.result);
		}
		
		public function fault(data:Object):void {
			var event:ResultEvent = data as ResultEvent;
			faultCallbackFunction(event.result);
		}
		
		public function defaultFunction(data:Object):void {
		}
	}
}