/**
 * This is a generated sub-class of _CompanyRO.as and is intended for behavior
 * customization.  This class is only generated when there is no file already present
 * at its target location.  Thus custom behavior that you add here will survive regeneration
 * of the super-class.
 **/

package com.artogrid.accountsystem.ro {
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;

	public class CompanyRO extends _Super_CompanyRO {
		/**
		 * Override super.init() to provide any initialization customization if needed.
		 */
		protected override function preInitializeService():void {

			super.preInitializeService();
			// Initialization customization goes here
		}
		private static var me:CompanyRO;

		public static function getMe():CompanyRO {
			if (me == null) {
				me=new CompanyRO();
				me.addEventListener(FaultEvent.FAULT, function(event:FaultEvent):void {
					Alert.show(event.fault.faultString + '\n' + event.fault.faultDetail)
				});
				me.showBusyCursor=true;
			}
			return me;
		}
	}

}
