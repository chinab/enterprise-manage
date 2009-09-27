package com.erp
{
	public class MyResponer implements IResponder {
		public function MyResponer(resultCallback:Function=null, faultCallback:Function=null) {
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
		
	}
}