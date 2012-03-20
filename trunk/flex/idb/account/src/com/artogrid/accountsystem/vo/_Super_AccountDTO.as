/**
 * This is a generated class and is not intended for modification.  To customize behavior
 * of this value object you may modify the generated sub-class of this class - AccountDTO.as.
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

[Managed]
[ExcludeClass]
public class _Super_AccountDTO extends flash.events.EventDispatcher implements com.adobe.fiber.valueobjects.IValueObject
{
    model_internal static function initRemoteClassAliasSingle(cz:Class) : void
    {
        flash.net.registerClassAlias("com.artogrid.service.m1.vo.idblogin.AccountDTO", cz);
    }

    model_internal static function initRemoteClassAliasAllRelated() : void
    {
    }

    model_internal var _dminternal_model : _AccountDTOEntityMetadata;
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
    private var _internal_createTime : Date;
    private var _internal_modifyBy : String;
    private var _internal_status : String;
    private var _internal_isForbidden : String;
    private var _internal_createBy : String;
    private var _internal_password : String;
    private var _internal_message : String;
    private var _internal_id : String;
    private var _internal_accountCode : String;
    private var _internal_username : String;
    private var _internal_address : String;
    private var _internal_email : String;
    private var _internal_accountType : String;
    private var _internal_companyId : String;
    private var _internal_displayName : String;
    private var _internal_telephone : String;
    private var _internal_modifyTime : Date;
    private var _internal_mobile : String;

    private static var emptyArray:Array = new Array();


    /**
     * derived property cache initialization
     */
    model_internal var _cacheInitialized_isValid:Boolean = false;

    model_internal var _changeWatcherArray:Array = new Array();

    public function _Super_AccountDTO()
    {
        _model = new _AccountDTOEntityMetadata(this);

        // Bind to own data or source properties for cache invalidation triggering

    }

    /**
     * data/source property getters
     */

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
    public function get status() : String
    {
        return _internal_status;
    }

    [Bindable(event="propertyChange")]
    public function get isForbidden() : String
    {
        return _internal_isForbidden;
    }

    [Bindable(event="propertyChange")]
    public function get createBy() : String
    {
        return _internal_createBy;
    }

    [Bindable(event="propertyChange")]
    public function get password() : String
    {
        return _internal_password;
    }

    [Bindable(event="propertyChange")]
    public function get message() : String
    {
        return _internal_message;
    }

    [Bindable(event="propertyChange")]
    public function get id() : String
    {
        return _internal_id;
    }

    [Bindable(event="propertyChange")]
    public function get accountCode() : String
    {
        return _internal_accountCode;
    }

    [Bindable(event="propertyChange")]
    public function get username() : String
    {
        return _internal_username;
    }

    [Bindable(event="propertyChange")]
    public function get address() : String
    {
        return _internal_address;
    }

    [Bindable(event="propertyChange")]
    public function get email() : String
    {
        return _internal_email;
    }

    [Bindable(event="propertyChange")]
    public function get accountType() : String
    {
        return _internal_accountType;
    }

    [Bindable(event="propertyChange")]
    public function get companyId() : String
    {
        return _internal_companyId;
    }

    [Bindable(event="propertyChange")]
    public function get displayName() : String
    {
        return _internal_displayName;
    }

    [Bindable(event="propertyChange")]
    public function get telephone() : String
    {
        return _internal_telephone;
    }

    [Bindable(event="propertyChange")]
    public function get modifyTime() : Date
    {
        return _internal_modifyTime;
    }

    [Bindable(event="propertyChange")]
    public function get mobile() : String
    {
        return _internal_mobile;
    }

    public function clearAssociations() : void
    {
    }

    /**
     * data/source property setters
     */

    public function set createTime(value:Date) : void
    {
        var oldValue:Date = _internal_createTime;
        if (oldValue !== value)
        {
            _internal_createTime = value;
        }
    }

    public function set modifyBy(value:String) : void
    {
        var oldValue:String = _internal_modifyBy;
        if (oldValue !== value)
        {
            _internal_modifyBy = value;
        }
    }

    public function set status(value:String) : void
    {
        var oldValue:String = _internal_status;
        if (oldValue !== value)
        {
            _internal_status = value;
        }
    }

    public function set isForbidden(value:String) : void
    {
        var oldValue:String = _internal_isForbidden;
        if (oldValue !== value)
        {
            _internal_isForbidden = value;
        }
    }

    public function set createBy(value:String) : void
    {
        var oldValue:String = _internal_createBy;
        if (oldValue !== value)
        {
            _internal_createBy = value;
        }
    }

    public function set password(value:String) : void
    {
        var oldValue:String = _internal_password;
        if (oldValue !== value)
        {
            _internal_password = value;
        }
    }

    public function set message(value:String) : void
    {
        var oldValue:String = _internal_message;
        if (oldValue !== value)
        {
            _internal_message = value;
        }
    }

    public function set id(value:String) : void
    {
        var oldValue:String = _internal_id;
        if (oldValue !== value)
        {
            _internal_id = value;
        }
    }

    public function set accountCode(value:String) : void
    {
        var oldValue:String = _internal_accountCode;
        if (oldValue !== value)
        {
            _internal_accountCode = value;
        }
    }

    public function set username(value:String) : void
    {
        var oldValue:String = _internal_username;
        if (oldValue !== value)
        {
            _internal_username = value;
        }
    }

    public function set address(value:String) : void
    {
        var oldValue:String = _internal_address;
        if (oldValue !== value)
        {
            _internal_address = value;
        }
    }

    public function set email(value:String) : void
    {
        var oldValue:String = _internal_email;
        if (oldValue !== value)
        {
            _internal_email = value;
        }
    }

    public function set accountType(value:String) : void
    {
        var oldValue:String = _internal_accountType;
        if (oldValue !== value)
        {
            _internal_accountType = value;
        }
    }

    public function set companyId(value:String) : void
    {
        var oldValue:String = _internal_companyId;
        if (oldValue !== value)
        {
            _internal_companyId = value;
        }
    }

    public function set displayName(value:String) : void
    {
        var oldValue:String = _internal_displayName;
        if (oldValue !== value)
        {
            _internal_displayName = value;
        }
    }

    public function set telephone(value:String) : void
    {
        var oldValue:String = _internal_telephone;
        if (oldValue !== value)
        {
            _internal_telephone = value;
        }
    }

    public function set modifyTime(value:Date) : void
    {
        var oldValue:Date = _internal_modifyTime;
        if (oldValue !== value)
        {
            _internal_modifyTime = value;
        }
    }

    public function set mobile(value:String) : void
    {
        var oldValue:String = _internal_mobile;
        if (oldValue !== value)
        {
            _internal_mobile = value;
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
    public function get _model() : _AccountDTOEntityMetadata
    {
        return model_internal::_dminternal_model;
    }

    public function set _model(value : _AccountDTOEntityMetadata) : void
    {
        var oldValue : _AccountDTOEntityMetadata = model_internal::_dminternal_model;
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
