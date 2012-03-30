package com.artogrid.accountsystem.bl {
	import com.artogrid.accountsystem.ro.CompanyRO;
	import com.artogrid.accountsystem.ro.FinancialCompanyRO;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.rpc.CallResponder;
	import mx.utils.ObjectUtil;

	public class InitData {
		public function InitData() {
		}

		[Bindable]
		public static var getAllCompanysResult:CallResponder=new CallResponder();
		[Bindable]
		public static var getAllFinancialCompanysResult:CallResponder=new CallResponder();

		[Bindable]
		public static var useTypes:ArrayCollection=new ArrayCollection([{name: "Broker", value: "1"}, {name: "Customer", value: "2"}]);

		public static const enable:String="1";

		public static const disable:String="2";

		public static function init():void {
			getAllCompanysResult.token=CompanyRO.getMe().getAllCompanys();
			getAllFinancialCompanysResult.token=FinancialCompanyRO.getMe().getAllFinancialCompanys();
		}

		public static function getIndexFromList(list:IList, name:String, value:String):int {
			for (var i:int=0; i < list.length; i++) {
				var item:Object=list.getItemAt(i);
				if (item[name] == value) {
					return i;
				}
			}
			return -1;
		}

		public static function getOtherValueFromListByName(list:IList, name:String, value:String, otherName:String):String {
			for each (var item:Object in list) {
				if (item[name] == value) {
					return item[otherName];
				}
			}
			return "";
		}
		
		/**
		 * 对象内部属性的复制
		 *
		 * @param dest
		 * @param orig
		 *
		 */
		public static function copyProperties(dest:Object, orig:Object, fileds:Array):void {
			for each (var filed:String in fileds) {
				try {
					dest[filed]=orig[filed];
				} catch (e:Error) {
					//								trace(e.message);
				}
			}
		}
	}
}