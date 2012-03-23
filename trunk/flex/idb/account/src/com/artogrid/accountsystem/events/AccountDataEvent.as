package com.artogrid.accountsystem.events {
	import flash.events.Event;

	public class AccountDataEvent extends Event {
		public static const ADD_NEW_ACCOUNT_TO_LIST:String = "add_new_account_to_list";
		public static const DATA_GRID_ITEM_CHANGE:String = "DATA_GRID_ITEM_CHANGE";

		private var _data:Object;

		public function AccountDataEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false) {
			super(type, bubbles, cancelable);
		}

		public function get data():Object {
			return _data;
		}

		public function set data(value:Object):void {
			_data=value;
		}

	}
}