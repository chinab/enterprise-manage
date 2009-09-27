package com.erp.pojo {
	import com.erp.pojo.Employee;
	
	import hessian.client.HessianService;
	
	import mx.rpc.AsyncToken;
	
	
	public class EmployeeService extends BaseService {
		
		private static var _employeeService : HessianService;
		
		protected static function getEmployeeService():HessianService {
			if (!_employeeService) {
				_employeeService = new HessianService("http://192.168.32.28:8080/java-st/hessian/employeeService");
			}
			return _employeeService;
		}
		
		public function EmployeeService(resultCallback:Function=null, faultCallback:Function=null) {
			super(resultCallback, faultCallback);
		}
		
		public function getUserName(id:int):void {
			var token:AsyncToken = getEmployeeService().get.send(id);
			token.addResponder(this);
		}
		
		public function saveUser(employee : Employee):void {
			var token:AsyncToken = getEmployeeService().save.send(employee);
			token.addResponder(this);
		}
	}
}