/**
 * This is a generated class and is not intended for modification.  To customize behavior
 * of this service wrapper you may modify the generated sub-class of this class - CompanyRO.as.
 */
package com.artogrid.accountsystem.ro
{
import com.adobe.fiber.core.model_internal;
import com.adobe.fiber.services.wrapper.RemoteObjectServiceWrapper;
import com.adobe.serializers.utility.TypeUtility;
import com.artogrid.accountsystem.vo.CompanyDTO;
import mx.rpc.AbstractOperation;
import mx.rpc.AsyncToken;
import mx.rpc.remoting.Operation;
import mx.rpc.remoting.RemoteObject;

import mx.collections.ItemResponder;
import com.adobe.fiber.valueobjects.AvailablePropertyIterator;

[ExcludeClass]
internal class _Super_CompanyRO extends com.adobe.fiber.services.wrapper.RemoteObjectServiceWrapper
{

    // Constructor
    public function _Super_CompanyRO()
    {
        // initialize service control
        _serviceControl = new mx.rpc.remoting.RemoteObject();

        // initialize RemoteClass alias for all entities returned by functions of this service
        com.artogrid.accountsystem.vo.CompanyDTO._initRemoteClassAlias();

        var operations:Object = new Object();
        var operation:mx.rpc.remoting.Operation;

        operation = new mx.rpc.remoting.Operation(null, "saveCompany");
        operation.resultType = com.artogrid.accountsystem.vo.CompanyDTO;
        operations["saveCompany"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAllCompanys");
        operation.resultElementType = com.artogrid.accountsystem.vo.CompanyDTO;
        operations["getAllCompanys"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "updateCompany");
        operation.resultType = com.artogrid.accountsystem.vo.CompanyDTO;
        operations["updateCompany"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "deleteCompanyById");
        operation.resultType = Boolean;
        operations["deleteCompanyById"] = operation;

        _serviceControl.operations = operations;
        _serviceControl.convertResultHandler = com.adobe.serializers.utility.TypeUtility.convertResultHandler;


         preInitializeService();
         model_internal::initialize();
    }
    
    //init initialization routine here, child class to override
    protected function preInitializeService():void
    {
        destination = "companyRO";
      
    }
    

    /**
      * This method is a generated wrapper used to call the 'saveCompany' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function saveCompany(arg0:com.artogrid.accountsystem.vo.CompanyDTO) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("saveCompany");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAllCompanys' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAllCompanys() : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAllCompanys");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send() ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'updateCompany' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function updateCompany(arg0:com.artogrid.accountsystem.vo.CompanyDTO) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("updateCompany");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'deleteCompanyById' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function deleteCompanyById(arg0:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("deleteCompanyById");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
}

}
