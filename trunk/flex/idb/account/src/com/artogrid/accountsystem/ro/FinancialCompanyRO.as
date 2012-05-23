/**
 * This is a generated sub-class of _FinancialCompanyRO.as and is intended for behavior
 * customization.  This class is only generated when there is no file already present
 * at its target location.  Thus custom behavior that you add here will survive regeneration
 * of the super-class. 
 **/
 
package com.artogrid.accountsystem.ro
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;

public class FinancialCompanyRO extends _Super_FinancialCompanyRO
{
    /**
     * Override super.init() to provide any initialization customization if needed.
     */
    protected override function preInitializeService():void
    {

        super.preInitializeService();
        // Initialization customization goes here
		_serviceControl.setCredentials("Guest","token_default");
    }
	private static var me:FinancialCompanyRO;
	
	public static function getMe():FinancialCompanyRO {
		if (me == null) {
			me=new FinancialCompanyRO();
			me.addEventListener(FaultEvent.FAULT, function(event:FaultEvent):void {
				Alert.show(event.fault.faultString + '\n' + event.fault.faultDetail)
			});
			me.showBusyCursor=true;
		}
		return me;
	}
               
}

}
