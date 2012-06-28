/**
 * This is a generated class and is not intended for modification.  To customize behavior
 * of this value object you may modify the generated sub-class of this class - LoginInfoDTO.as.
 */

package com.artogrid.accountsystem.vo
{
import com.adobe.fiber.services.IFiberManagingService;
import com.adobe.fiber.valueobjects.IValueObject;
import com.artogrid.accountsystem.vo.AccountDTO;
import com.artogrid.accountsystem.vo.DepartmentDTO;
import com.artogrid.accountsystem.vo.LoginStatusDTO;
import com.artogrid.accountsystem.vo.RoleDTO;
import flash.events.EventDispatcher;
import mx.collections.ArrayCollection;
import mx.events.PropertyChangeEvent;

import flash.net.registerClassAlias;
import flash.net.getClassByAlias;
import com.adobe.fiber.core.model_internal;
import com.adobe.fiber.valueobjects.IPropertyIterator;
import com.adobe.fiber.valueobjects.AvailablePropertyIterator;

use namespace model_internal;

[ExcludeClass]
public class _Super_LoginInfoDTO extends flash.events.EventDispatcher implements com.adobe.fiber.valueobjects.IValueObject
{
    model_internal static function initRemoteClassAliasSingle(cz:Class) : void
    {
        flash.net.registerClassAlias("com.artogrid.service.m1.vo.idblogin.LoginInfoDTO", cz);
    }

    model_internal static function initRemoteClassAliasAllRelated() : void
    {
        com.artogrid.accountsystem.vo.LoginStatusDTO.initRemoteClassAliasSingleChild();
        com.artogrid.accountsystem.vo.RoleDTO.initRemoteClassAliasSingleChild();
        com.artogrid.accountsystem.vo.DepartmentDTO.initRemoteClassAliasSingleChild();
        com.artogrid.accountsystem.vo.AccountDTO.initRemoteClassAliasSingleChild();
    }

    model_internal var _dminternal_model : _LoginInfoDTOEntityMetadata;
    model_internal var _changedObjects:mx.collections.ArrayCollection = new ArrayCollection();

    public function getChangedObjects() : Array
    {
        _changedObjects.addItemAt(this,0);
        return _changedObjects.source;
    }

    public function clearChangedObjects() : void
    {
        _changedObjects.removeAll();
    }

    /**
     * properties
     */
    private var _internal_message : String;
    private var _internal_loginStatus : com.artogrid.accountsystem.vo.LoginStatusDTO;
    private var _internal_roles : ArrayCollection;
    model_internal var _internal_roles_leaf:com.artogrid.accountsystem.vo.RoleDTO;
    private var _internal_machineId : String;
    private var _internal_departments : ArrayCollection;
    model_internal var _internal_departments_leaf:com.artogrid.accountsystem.vo.DepartmentDTO;
    private var _internal_account : com.artogrid.accountsystem.vo.AccountDTO;

    private static var emptyArray:Array = new Array();


    /**
     * derived property cache initialization
     */
    model_internal var _cacheInitialized_isValid:Boolean = false;

    model_internal var _changeWatcherArray:Array = new Array();

    public function _Super_LoginInfoDTO()
    {
        _model = new _LoginInfoDTOEntityMetadata(this);

        // Bind to own data or source properties for cache invalidation triggering

    }

    /**
     * data/source property getters
     */

    [Bindable(event="propertyChange")]
    public function get message() : String
    {
        return _internal_message;
    }

    [Bindable(event="propertyChange")]
    public function get loginStatus() : com.artogrid.accountsystem.vo.LoginStatusDTO
    {
        return _internal_loginStatus;
    }

    [Bindable(event="propertyChange")]
    public function get roles() : ArrayCollection
    {
        return _internal_roles;
    }

    [Bindable(event="propertyChange")]
    public function get machineId() : String
    {
        return _internal_machineId;
    }

    [Bindable(event="propertyChange")]
    public function get departments() : ArrayCollection
    {
        return _internal_departments;
    }

    [Bindable(event="propertyChange")]
    public function get account() : com.artogrid.accountsystem.vo.AccountDTO
    {
        return _internal_account;
    }

    public function clearAssociations() : void
    {
    }

    /**
     * data/source property setters
     */

    public function set message(value:String) : void
    {
        var oldValue:String = _internal_message;
        if (oldValue !== value)
        {
            _internal_message = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "message", oldValue, _internal_message));
        }
    }

    public function set loginStatus(value:com.artogrid.accountsystem.vo.LoginStatusDTO) : void
    {
        var oldValue:com.artogrid.accountsystem.vo.LoginStatusDTO = _internal_loginStatus;
        if (oldValue !== value)
        {
            _internal_loginStatus = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "loginStatus", oldValue, _internal_loginStatus));
        }
    }

    public function set roles(value:*) : void
    {
        var oldValue:ArrayCollection = _internal_roles;
        if (oldValue !== value)
        {
            if (value is ArrayCollection)
            {
                _internal_roles = value;
            }
            else if (value is Array)
            {
                _internal_roles = new ArrayCollection(value);
            }
            else if (value == null)
            {
                _internal_roles = null;
            }
            else
            {
                throw new Error("value of roles must be a collection");
            }
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "roles", oldValue, _internal_roles));
        }
    }

    public function set machineId(value:String) : void
    {
        var oldValue:String = _internal_machineId;
        if (oldValue !== value)
        {
            _internal_machineId = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "machineId", oldValue, _internal_machineId));
        }
    }

    public function set departments(value:*) : void
    {
        var oldValue:ArrayCollection = _internal_departments;
        if (oldValue !== value)
        {
            if (value is ArrayCollection)
            {
                _internal_departments = value;
            }
            else if (value is Array)
            {
                _internal_departments = new ArrayCollection(value);
            }
            else if (value == null)
            {
                _internal_departments = null;
            }
            else
            {
                throw new Error("value of departments must be a collection");
            }
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "departments", oldValue, _internal_departments));
        }
    }

    public function set account(value:com.artogrid.accountsystem.vo.AccountDTO) : void
    {
        var oldValue:com.artogrid.accountsystem.vo.AccountDTO = _internal_account;
        if (oldValue !== value)
        {
            _internal_account = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "account", oldValue, _internal_account));
        }
    }

    /**
     * Data/source property setter listeners
     *
     * Each data property whose value affects other properties or the validity of the entity
     * needs to invalidate all previously calculated artifacts. These include:
     *  - any derived properties or constraints that reference the given data property.
     *  - any availability guards (variant expressions) that reference the given data property.
     *  - any style validations, message tokens or guards that reference the given data property.
     *  - the validity of the property (and the containing entity) if the given data property has a length restriction.
     *  - the validity of the property (and the containing entity) if the given data property is required.
     */


    /**
     * valid related derived properties
     */
    model_internal var _isValid : Boolean;
    model_internal var _invalidConstraints:Array = new Array();
    model_internal var _validationFailureMessages:Array = new Array();

    /**
     * derived property calculators
     */
    

    /**
     * isValid calculator
     */
    model_internal function calculateIsValid():Boolean
    {
        var violatedConsts:Array = new Array();
        var validationFailureMessages:Array = new Array();

        var propertyValidity:Boolean = true;

        model_internal::_cacheInitialized_isValid = true;
        model_internal::invalidConstraints_der = violatedConsts;
        model_internal::validationFailureMessages_der = validationFailureMessages;
        return violatedConsts.length == 0 && propertyValidity;
    }

    /**
     * derived property setters
     */

    model_internal function set isValid_der(value:Boolean) : void
    {
        var oldValue:Boolean = model_internal::_isValid;
        if (oldValue !== value)
        {
            model_internal::_isValid = value;
            _model.model_internal::fireChangeEvent("isValid", oldValue, model_internal::_isValid);
        }
    }

    /**
     * derived property getters
     */

    [Transient]
    [Bindable(event="propertyChange")]
    public function get _model() : _LoginInfoDTOEntityMetadata
    {
        return model_internal::_dminternal_model;
    }

    public function set _model(value : _LoginInfoDTOEntityMetadata) : void
    {
        var oldValue : _LoginInfoDTOEntityMetadata = model_internal::_dminternal_model;
        if (oldValue !== value)
        {
            model_internal::_dminternal_model = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "_model", oldValue, model_internal::_dminternal_model));
        }
    }

    /**
     * methods
     */


    /**
     *  services
     */
    private var _managingService:com.adobe.fiber.services.IFiberManagingService;

    public function set managingService(managingService:com.adobe.fiber.services.IFiberManagingService):void
    {
        _managingService = managingService;
    }

    model_internal function set invalidConstraints_der(value:Array) : void
    {
        var oldValue:Array = model_internal::_invalidConstraints;
        // avoid firing the event when old and new value are different empty arrays
        if (oldValue !== value && (oldValue.length > 0 || value.length > 0))
        {
            model_internal::_invalidConstraints = value;
            _model.model_internal::fireChangeEvent("invalidConstraints", oldValue, model_internal::_invalidConstraints);
        }
    }

    model_internal function set validationFailureMessages_der(value:Array) : void
    {
        var oldValue:Array = model_internal::_validationFailureMessages;
        // avoid firing the event when old and new value are different empty arrays
        if (oldValue !== value && (oldValue.length > 0 || value.length > 0))
        {
            model_internal::_validationFailureMessages = value;
            _model.model_internal::fireChangeEvent("validationFailureMessages", oldValue, model_internal::_validationFailureMessages);
        }
    }


}

}
