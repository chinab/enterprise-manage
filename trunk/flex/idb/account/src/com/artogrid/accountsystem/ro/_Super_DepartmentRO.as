/**
 * This is a generated class and is not intended for modification.  To customize behavior
 * of this service wrapper you may modify the generated sub-class of this class - DepartmentRO.as.
 */
package com.artogrid.accountsystem.ro
{
import com.adobe.fiber.core.model_internal;
import com.adobe.fiber.services.wrapper.RemoteObjectServiceWrapper;
import com.adobe.serializers.utility.TypeUtility;
import com.artogrid.accountsystem.vo.DepartmentDTO;
import mx.rpc.AbstractOperation;
import mx.rpc.AsyncToken;
import mx.rpc.remoting.Operation;
import mx.rpc.remoting.RemoteObject;

import mx.collections.ItemResponder;
import com.adobe.fiber.valueobjects.AvailablePropertyIterator;

[ExcludeClass]
internal class _Super_DepartmentRO extends com.adobe.fiber.services.wrapper.RemoteObjectServiceWrapper
{

    // Constructor
    public function _Super_DepartmentRO()
    {
        // initialize service control
        _serviceControl = new mx.rpc.remoting.RemoteObject();

        // initialize RemoteClass alias for all entities returned by functions of this service
        com.artogrid.accountsystem.vo.DepartmentDTO._initRemoteClassAlias();

        var operations:Object = new Object();
        var operation:mx.rpc.remoting.Operation;

        operation = new mx.rpc.remoting.Operation(null, "saveDepartment");
        operation.resultType = com.artogrid.accountsystem.vo.DepartmentDTO;
        operations["saveDepartment"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "deleteDepartmentById");
        operation.resultType = Boolean;
        operations["deleteDepartmentById"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "updateDepartment");
        operation.resultType = com.artogrid.accountsystem.vo.DepartmentDTO;
        operations["updateDepartment"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAllDepartments");
        operation.resultElementType = com.artogrid.accountsystem.vo.DepartmentDTO;
        operations["getAllDepartments"] = operation;

        _serviceControl.operations = operations;
        _serviceControl.convertResultHandler = com.adobe.serializers.utility.TypeUtility.convertResultHandler;


         preInitializeService();
         model_internal::initialize();
    }
    
    //init initialization routine here, child class to override
    protected function preInitializeService():void
    {
        destination = "departmentRO";
      
    }
    

    /**
      * This method is a generated wrapper used to call the 'saveDepartment' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function saveDepartment(arg0:com.artogrid.accountsystem.vo.DepartmentDTO) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("saveDepartment");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'deleteDepartmentById' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function deleteDepartmentById(arg0:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("deleteDepartmentById");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'updateDepartment' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function updateDepartment(arg0:com.artogrid.accountsystem.vo.DepartmentDTO) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("updateDepartment");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAllDepartments' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAllDepartments() : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAllDepartments");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send() ;
        return _internal_token;
    }
     
}

}
