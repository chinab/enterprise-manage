/**
 * This is a generated class and is not intended for modification.  To customize behavior
 * of this service wrapper you may modify the generated sub-class of this class - AccountRO.as.
 */
package com.artogrid.accountsystem.ro
{
import com.adobe.fiber.core.model_internal;
import com.adobe.fiber.services.wrapper.RemoteObjectServiceWrapper;
import com.adobe.fiber.valueobjects.IValueObject;
import com.adobe.serializers.utility.TypeUtility;
import com.artogrid.accountsystem.vo.AccountDTO;
import com.artogrid.accountsystem.vo.PaginatorObjUtil;
import mx.collections.ArrayCollection;
import mx.collections.ListCollectionView;
import mx.data.DataManager;
import mx.data.IManaged;
import mx.data.ManagedAssociation;
import mx.data.ManagedOperation;
import mx.data.ManagedQuery;
import mx.data.RPCDataManager;
import mx.data.errors.DataServiceError;
import mx.rpc.AbstractOperation;
import mx.rpc.AsyncToken;
import mx.rpc.remoting.Operation;
import mx.rpc.remoting.RemoteObject;

import mx.collections.ItemResponder;
import com.adobe.fiber.valueobjects.AvailablePropertyIterator;

[ExcludeClass]
internal class _Super_AccountRO extends com.adobe.fiber.services.wrapper.RemoteObjectServiceWrapper
{
    private var _accountDTORPCDataManager : mx.data.RPCDataManager;
    private var managersArray : Array = new Array();

    public const DATA_MANAGER_ACCOUNTDTO : String = "AccountDTO";

    public function getDataManager(dataManagerName:String) : mx.data.RPCDataManager
    {
        switch (dataManagerName)
        {
             case (DATA_MANAGER_ACCOUNTDTO):
                return _accountDTORPCDataManager;
            default:
                return null;
        }
    }

    /**
     * Commit all of the pending changes for this DataService, as well as all of the pending changes of all DataServices
     * sharing the same DataStore.  By default, a DataService shares the same DataStore with other DataServices if they have 
     * managed association properties and share the same set of channels. 
     *
     * @see mx.data.DataManager
     * @see mx.data.DataStore
     *
     * @param itemsOrCollections:Array This is an optional parameter which defaults to null when
     *  you want to commit all pending changes.  If you want to commit a subset of the pending
     *  changes use this argument to specify a list of managed ListCollectionView instances
     *  and/or managed items.  ListCollectionView objects are most typically ArrayCollections
     *  you have provided to your fill method.  The items appropriate for this method are
     *  any managed version of the item.  These are any items you retrieve from getItem, createItem
     *  or using the getItemAt method from a managed collection.  Only changes for the
     *  items defined by any of the values in this array will be committed.
     *
     * @param cascadeCommit if true, also commit changes made to any associated
     *  items supplied in this list.
     *
     *  @return AsyncToken that is returned in <code>call</code> property of
     *  either the <code>ResultEvent.RESULT</code> or in the
     *  <code>FaultEvent.FAULT</code>.
     *  Custom data can be attached to this object and inspected later
     *  during the event handling phase.  If no changes have been made
     *  to the relevant items, null is returned instead of an AsyncToken.
     */
    public function commit(itemsOrCollections:Array=null, cascadeCommit:Boolean=false):mx.rpc.AsyncToken
    {
        return _accountDTORPCDataManager.dataStore.commit(itemsOrCollections, cascadeCommit);
    }

    /**
     * Reverts all pending (uncommitted) changes for this DataService, as well as all of the pending changes of all DataServics
     * sharing the same DataStore.  By default, a DataService shares the same DataStore with other DataServices if they have 
     * managed association properties and share the same set of channels. 
     *
     * In case you specify a value for itemsOrCollections:Array parameter, only pending (uncommitted) changes for the specified 
     * managed items or collections will be reverted.
     *
     * @see mx.data.DataManager
     * @see mx.data.DataStore
     * 
     * @param itemsOrCollections:Array This is an optional parameter which defaults to null 
     * when you want to revert all pending (uncommitted) changes for all DataServices
     * managed by this DataStore. If you want to revert a subset of the pending changes use 
     * this argument to specify a array of managed items or collections
     *
     * @return true if any changes were reverted.
     * @throws DataServiceError if the passed in array contains non-managed items or collections
     *  
     */
    public function revertChanges(itemsOrCollections:Array=null):Boolean
    {
        if (itemsOrCollections == null)
        {
            // Revert all changes
            return _accountDTORPCDataManager.dataStore.revertChanges();
        }
        else
        {
            // Revert passed in items
            var anyChangeItemReverted:Boolean = false;

            // Iterate over array and revert managed item or collection as the case may be
            for each (var changeItem:Object in itemsOrCollections)
            {
                if (changeItem is com.adobe.fiber.valueobjects.IValueObject)
                {
                    var dataMgr:mx.data.DataManager = getDataManager(changeItem._model.getEntityName());
                    anyChangeItemReverted ||= dataMgr.revertChanges(mx.data.IManaged(changeItem))
                }
                else if (changeItem is mx.collections.ListCollectionView)
                {
                    anyChangeItemReverted ||= _accountDTORPCDataManager.dataStore.revertChangesForCollection(mx.collections.ListCollectionView(changeItem));
                }
                else
                {
                    throw new mx.data.errors.DataServiceError("revertChanges called on array that contains non-managed items or collections");
                }
            }
            return anyChangeItemReverted;
        }
    }

    // Constructor
    public function _Super_AccountRO()
    {
        // initialize service control
        _serviceControl = new mx.rpc.remoting.RemoteObject();

        // initialize RemoteClass alias for all entities returned by functions of this service
        com.artogrid.accountsystem.vo.AccountDTO._initRemoteClassAlias();
        com.artogrid.accountsystem.vo.PaginatorObjUtil._initRemoteClassAlias();

        var operations:Object = new Object();
        var operation:mx.rpc.remoting.Operation;

        operation = new mx.rpc.remoting.Operation(null, "addRolesToAccount");
        operation.resultType = Boolean;
        operations["addRolesToAccount"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "addRoleToAccount");
        operation.resultType = Boolean;
        operations["addRoleToAccount"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "deleteAccount");
        operation.resultType = Boolean;
        operations["deleteAccount"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "deleteAccountById");
        operation.resultType = Boolean;
        operations["deleteAccountById"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "deleteAccounts");
        operation.resultType = Boolean;
        operations["deleteAccounts"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAccountById");
        operation.resultType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["getAccountById"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAccountByUsername");
        operation.resultType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["getAccountByUsername"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAccountsByDepartmentCode");
        operation.resultElementType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["getAccountsByDepartmentCode"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAccountsByDepartmentId");
        operation.resultElementType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["getAccountsByDepartmentId"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAccountsByRoleId");
        operation.resultElementType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["getAccountsByRoleId"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAccountsPagerByDepartmentCode");
        operation.resultType = com.artogrid.accountsystem.vo.PaginatorObjUtil;
        operations["getAccountsPagerByDepartmentCode"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAccountsPagerByDepartmentId");
        operation.resultType = com.artogrid.accountsystem.vo.PaginatorObjUtil;
        operations["getAccountsPagerByDepartmentId"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAccountsPagerByRoleId");
        operation.resultType = com.artogrid.accountsystem.vo.PaginatorObjUtil;
        operations["getAccountsPagerByRoleId"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAllAccounts");
        operation.resultElementType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["getAllAccounts"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "getAllAccountsPager");
        operation.resultElementType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["getAllAccountsPager"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "removeRoleFormAccount");
        operation.resultType = Boolean;
        operations["removeRoleFormAccount"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "removeRolesFormAccount");
        operation.resultType = Boolean;
        operations["removeRolesFormAccount"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "saveAccount");
        operation.resultType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["saveAccount"] = operation;
        operation = new mx.rpc.remoting.Operation(null, "updateAccount");
        operation.resultType = com.artogrid.accountsystem.vo.AccountDTO;
        operations["updateAccount"] = operation;

        _serviceControl.operations = operations;
        _serviceControl.convertResultHandler = com.adobe.serializers.utility.TypeUtility.convertResultHandler;
        var managedAssociation : mx.data.ManagedAssociation;
        var managedAssocsArray : Array;
        // initialize AccountDTO data manager
        _accountDTORPCDataManager = new mx.data.RPCDataManager();
        managersArray.push(_accountDTORPCDataManager);

        managedAssocsArray = new Array();

        _accountDTORPCDataManager.destination = "accountDTORPCDataManager";
        _accountDTORPCDataManager.service = _serviceControl;        
        _accountDTORPCDataManager.identities =  "id";      
        _accountDTORPCDataManager.itemClass = com.artogrid.accountsystem.vo.AccountDTO; 



        var dmOperation : mx.data.ManagedOperation;
        var dmQuery : mx.data.ManagedQuery;

        dmQuery = new mx.data.ManagedQuery("getAllAccountsPager");
        dmQuery.propertySpecifier = "createTime,modifyBy,status,isForbidden,createBy,password,message,id,accountCode,username,address,email,accountType,companyId,displayName,telephone,modifyTime,mobile";
        dmQuery.pagingEnabled = true;
        dmQuery.positionalPagingParameters = true;
        dmQuery.pageSize = 10;
        dmQuery.parameters = "arg0,arg1";
        _accountDTORPCDataManager.addManagedOperation(dmQuery);

        _serviceControl.managers = managersArray;

         preInitializeService();
         model_internal::initialize();
    }
    
    //init initialization routine here, child class to override
    protected function preInitializeService():void
    {
        destination = "accountRO";
      
    }
    

    /**
      * This method is a generated wrapper used to call the 'addRolesToAccount' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function addRolesToAccount(arg0:ArrayCollection, arg1:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("addRolesToAccount");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0,arg1) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'addRoleToAccount' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function addRoleToAccount(arg0:String, arg1:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("addRoleToAccount");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0,arg1) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'deleteAccount' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function deleteAccount(arg0:com.artogrid.accountsystem.vo.AccountDTO) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("deleteAccount");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'deleteAccountById' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function deleteAccountById(arg0:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("deleteAccountById");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'deleteAccounts' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function deleteAccounts(arg0:ArrayCollection) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("deleteAccounts");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAccountById' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAccountById(arg0:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAccountById");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAccountByUsername' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAccountByUsername(arg0:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAccountByUsername");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAccountsByDepartmentCode' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAccountsByDepartmentCode(arg0:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAccountsByDepartmentCode");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAccountsByDepartmentId' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAccountsByDepartmentId(arg0:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAccountsByDepartmentId");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAccountsByRoleId' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAccountsByRoleId(arg0:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAccountsByRoleId");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAccountsPagerByDepartmentCode' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAccountsPagerByDepartmentCode(arg0:String, arg1:int, arg2:int) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAccountsPagerByDepartmentCode");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0,arg1,arg2) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAccountsPagerByDepartmentId' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAccountsPagerByDepartmentId(arg0:String, arg1:int, arg2:int) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAccountsPagerByDepartmentId");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0,arg1,arg2) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAccountsPagerByRoleId' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAccountsPagerByRoleId(arg0:String, arg1:int, arg2:int) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAccountsPagerByRoleId");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0,arg1,arg2) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAllAccounts' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAllAccounts() : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAllAccounts");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send() ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'getAllAccountsPager' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function getAllAccountsPager() : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("getAllAccountsPager");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send() ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'removeRoleFormAccount' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function removeRoleFormAccount(arg0:String, arg1:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("removeRoleFormAccount");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0,arg1) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'removeRolesFormAccount' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function removeRolesFormAccount(arg0:ArrayCollection, arg1:String) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("removeRolesFormAccount");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0,arg1) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'saveAccount' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function saveAccount(arg0:com.artogrid.accountsystem.vo.AccountDTO) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("saveAccount");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
    /**
      * This method is a generated wrapper used to call the 'updateAccount' operation. It returns an mx.rpc.AsyncToken whose
      * result property will be populated with the result of the operation when the server response is received.
      * To use this result from MXML code, define a CallResponder component and assign its token property to this method's return value.
      * You can then bind to CallResponder.lastResult or listen for the CallResponder.result or fault events.
      *
      * @see mx.rpc.AsyncToken
      * @see mx.rpc.CallResponder 
      *
      * @return an mx.rpc.AsyncToken whose result property will be populated with the result of the operation when the server response is received.
      */
    public function updateAccount(arg0:com.artogrid.accountsystem.vo.AccountDTO) : mx.rpc.AsyncToken
    {
        var _internal_operation:mx.rpc.AbstractOperation = _serviceControl.getOperation("updateAccount");
        var _internal_token:mx.rpc.AsyncToken = _internal_operation.send(arg0) ;
        return _internal_token;
    }
     
}

}
