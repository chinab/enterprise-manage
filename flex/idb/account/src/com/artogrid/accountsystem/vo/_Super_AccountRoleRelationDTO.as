/**
 * This is a generated class and is not intended for modification.  To customize behavior
 * of this value object you may modify the generated sub-class of this class - AccountRoleRelationDTO.as.
 */

package com.artogrid.accountsystem.vo
{
import com.adobe.fiber.services.IFiberManagingService;
import com.adobe.fiber.valueobjects.IValueObject;
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
public class _Super_AccountRoleRelationDTO extends flash.events.EventDispatcher implements com.adobe.fiber.valueobjects.IValueObject
{
    model_internal static function initRemoteClassAliasSingle(cz:Class) : void
    {
        flash.net.registerClassAlias("com.artogrid.service.m1.vo.idblogin.AccountRoleRelationDTO", cz);
    }

    model_internal static function initRemoteClassAliasAllRelated() : void
    {
    }

    model_internal var _dminternal_model : _AccountRoleRelationDTOEntityMetadata;
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
    private var _internal_id : String;
    private var _internal_createTime : Date;
    private var _internal_modifyBy : String;
    private var _internal_accountId : String;
    private var _internal_status : String;
    private var _internal_departmentId : String;
    private var _internal_departmentCode : String;
    private var _internal_modifyTime : Date;
    private var _internal_createBy : String;
    private var _internal_roleId : String;

    private static var emptyArray:Array = new Array();


    /**
     * derived property cache initialization
     */
    model_internal var _cacheInitialized_isValid:Boolean = false;

    model_internal var _changeWatcherArray:Array = new Array();

    public function _Super_AccountRoleRelationDTO()
    {
        _model = new _AccountRoleRelationDTOEntityMetadata(this);

        // Bind to own data or source properties for cache invalidation triggering

    }

    /**
     * data/source property getters
     */

    [Bindable(event="propertyChange")]
    public function get id() : String
    {
        return _internal_id;
    }

    [Bindable(event="propertyChange")]
    public function get createTime() : Date
    {
        return _internal_createTime;
    }

    [Bindable(event="propertyChange")]
    public function get modifyBy() : String
    {
        return _internal_modifyBy;
    }

    [Bindable(event="propertyChange")]
    public function get accountId() : String
    {
        return _internal_accountId;
    }

    [Bindable(event="propertyChange")]
    public function get status() : String
    {
        return _internal_status;
    }

    [Bindable(event="propertyChange")]
    public function get departmentId() : String
    {
        return _internal_departmentId;
    }

    [Bindable(event="propertyChange")]
    public function get departmentCode() : String
    {
        return _internal_departmentCode;
    }

    [Bindable(event="propertyChange")]
    public function get modifyTime() : Date
    {
        return _internal_modifyTime;
    }

    [Bindable(event="propertyChange")]
    public function get createBy() : String
    {
        return _internal_createBy;
    }

    [Bindable(event="propertyChange")]
    public function get roleId() : String
    {
        return _internal_roleId;
    }

    public function clearAssociations() : void
    {
    }

    /**
     * data/source property setters
     */

    public function set id(value:String) : void
    {
        var oldValue:String = _internal_id;
        if (oldValue !== value)
        {
            _internal_id = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "id", oldValue, _internal_id));
        }
    }

    public function set createTime(value:Date) : void
    {
        var oldValue:Date = _internal_createTime;
        if (oldValue !== value)
        {
            _internal_createTime = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "createTime", oldValue, _internal_createTime));
        }
    }

    public function set modifyBy(value:String) : void
    {
        var oldValue:String = _internal_modifyBy;
        if (oldValue !== value)
        {
            _internal_modifyBy = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "modifyBy", oldValue, _internal_modifyBy));
        }
    }

    public function set accountId(value:String) : void
    {
        var oldValue:String = _internal_accountId;
        if (oldValue !== value)
        {
            _internal_accountId = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "accountId", oldValue, _internal_accountId));
        }
    }

    public function set status(value:String) : void
    {
        var oldValue:String = _internal_status;
        if (oldValue !== value)
        {
            _internal_status = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "status", oldValue, _internal_status));
        }
    }

    public function set departmentId(value:String) : void
    {
        var oldValue:String = _internal_departmentId;
        if (oldValue !== value)
        {
            _internal_departmentId = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "departmentId", oldValue, _internal_departmentId));
        }
    }

    public function set departmentCode(value:String) : void
    {
        var oldValue:String = _internal_departmentCode;
        if (oldValue !== value)
        {
            _internal_departmentCode = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "departmentCode", oldValue, _internal_departmentCode));
        }
    }

    public function set modifyTime(value:Date) : void
    {
        var oldValue:Date = _internal_modifyTime;
        if (oldValue !== value)
        {
            _internal_modifyTime = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "modifyTime", oldValue, _internal_modifyTime));
        }
    }

    public function set createBy(value:String) : void
    {
        var oldValue:String = _internal_createBy;
        if (oldValue !== value)
        {
            _internal_createBy = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "createBy", oldValue, _internal_createBy));
        }
    }

    public function set roleId(value:String) : void
    {
        var oldValue:String = _internal_roleId;
        if (oldValue !== value)
        {
            _internal_roleId = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "roleId", oldValue, _internal_roleId));
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
    public function get _model() : _AccountRoleRelationDTOEntityMetadata
    {
        return model_internal::_dminternal_model;
    }

    public function set _model(value : _AccountRoleRelationDTOEntityMetadata) : void
    {
        var oldValue : _AccountRoleRelationDTOEntityMetadata = model_internal::_dminternal_model;
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
