/**
 * This is a generated class and is not intended for modification.  To customize behavior
 * of this value object you may modify the generated sub-class of this class - PaginatorObjUtil.as.
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
public class _Super_PaginatorObjUtil extends flash.events.EventDispatcher implements com.adobe.fiber.valueobjects.IValueObject
{
    model_internal static function initRemoteClassAliasSingle(cz:Class) : void
    {
        flash.net.registerClassAlias("com.artogrid.service.utils.generic.PaginatorObjUtil", cz);
    }

    model_internal static function initRemoteClassAliasAllRelated() : void
    {
    }

    model_internal var _dminternal_model : _PaginatorObjUtilEntityMetadata;
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
    private var _internal_pageCount : int;
    private var _internal_usePage : Boolean;
    private var _internal_usePrevious : Boolean;
    private var _internal_pageNo : int;
    private var _internal_pageSize : int;
    private var _internal_endNum : int;
    private var _internal_totalRecords : int;
    private var _internal_startNum : int;
    private var _internal_useBehind : Boolean;
    private var _internal_records : ArrayCollection;
    private var _internal_totalPages : int;

    private static var emptyArray:Array = new Array();


    /**
     * derived property cache initialization
     */
    model_internal var _cacheInitialized_isValid:Boolean = false;

    model_internal var _changeWatcherArray:Array = new Array();

    public function _Super_PaginatorObjUtil()
    {
        _model = new _PaginatorObjUtilEntityMetadata(this);

        // Bind to own data or source properties for cache invalidation triggering

    }

    /**
     * data/source property getters
     */

    [Bindable(event="propertyChange")]
    public function get pageCount() : int
    {
        return _internal_pageCount;
    }

    [Bindable(event="propertyChange")]
    public function get usePage() : Boolean
    {
        return _internal_usePage;
    }

    [Bindable(event="propertyChange")]
    public function get usePrevious() : Boolean
    {
        return _internal_usePrevious;
    }

    [Bindable(event="propertyChange")]
    public function get pageNo() : int
    {
        return _internal_pageNo;
    }

    [Bindable(event="propertyChange")]
    public function get pageSize() : int
    {
        return _internal_pageSize;
    }

    [Bindable(event="propertyChange")]
    public function get endNum() : int
    {
        return _internal_endNum;
    }

    [Bindable(event="propertyChange")]
    public function get totalRecords() : int
    {
        return _internal_totalRecords;
    }

    [Bindable(event="propertyChange")]
    public function get startNum() : int
    {
        return _internal_startNum;
    }

    [Bindable(event="propertyChange")]
    public function get useBehind() : Boolean
    {
        return _internal_useBehind;
    }

    [Bindable(event="propertyChange")]
    public function get records() : ArrayCollection
    {
        return _internal_records;
    }

    [Bindable(event="propertyChange")]
    public function get totalPages() : int
    {
        return _internal_totalPages;
    }

    public function clearAssociations() : void
    {
    }

    /**
     * data/source property setters
     */

    public function set pageCount(value:int) : void
    {
        var oldValue:int = _internal_pageCount;
        if (oldValue !== value)
        {
            _internal_pageCount = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "pageCount", oldValue, _internal_pageCount));
        }
    }

    public function set usePage(value:Boolean) : void
    {
        var oldValue:Boolean = _internal_usePage;
        if (oldValue !== value)
        {
            _internal_usePage = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "usePage", oldValue, _internal_usePage));
        }
    }

    public function set usePrevious(value:Boolean) : void
    {
        var oldValue:Boolean = _internal_usePrevious;
        if (oldValue !== value)
        {
            _internal_usePrevious = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "usePrevious", oldValue, _internal_usePrevious));
        }
    }

    public function set pageNo(value:int) : void
    {
        var oldValue:int = _internal_pageNo;
        if (oldValue !== value)
        {
            _internal_pageNo = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "pageNo", oldValue, _internal_pageNo));
        }
    }

    public function set pageSize(value:int) : void
    {
        var oldValue:int = _internal_pageSize;
        if (oldValue !== value)
        {
            _internal_pageSize = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "pageSize", oldValue, _internal_pageSize));
        }
    }

    public function set endNum(value:int) : void
    {
        var oldValue:int = _internal_endNum;
        if (oldValue !== value)
        {
            _internal_endNum = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "endNum", oldValue, _internal_endNum));
        }
    }

    public function set totalRecords(value:int) : void
    {
        var oldValue:int = _internal_totalRecords;
        if (oldValue !== value)
        {
            _internal_totalRecords = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "totalRecords", oldValue, _internal_totalRecords));
        }
    }

    public function set startNum(value:int) : void
    {
        var oldValue:int = _internal_startNum;
        if (oldValue !== value)
        {
            _internal_startNum = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "startNum", oldValue, _internal_startNum));
        }
    }

    public function set useBehind(value:Boolean) : void
    {
        var oldValue:Boolean = _internal_useBehind;
        if (oldValue !== value)
        {
            _internal_useBehind = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "useBehind", oldValue, _internal_useBehind));
        }
    }

    public function set records(value:*) : void
    {
        var oldValue:ArrayCollection = _internal_records;
        if (oldValue !== value)
        {
            if (value is ArrayCollection)
            {
                _internal_records = value;
            }
            else if (value is Array)
            {
                _internal_records = new ArrayCollection(value);
            }
            else if (value == null)
            {
                _internal_records = null;
            }
            else
            {
                throw new Error("value of records must be a collection");
            }
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "records", oldValue, _internal_records));
        }
    }

    public function set totalPages(value:int) : void
    {
        var oldValue:int = _internal_totalPages;
        if (oldValue !== value)
        {
            _internal_totalPages = value;
            this.dispatchEvent(mx.events.PropertyChangeEvent.createUpdateEvent(this, "totalPages", oldValue, _internal_totalPages));
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
    public function get _model() : _PaginatorObjUtilEntityMetadata
    {
        return model_internal::_dminternal_model;
    }

    public function set _model(value : _PaginatorObjUtilEntityMetadata) : void
    {
        var oldValue : _PaginatorObjUtilEntityMetadata = model_internal::_dminternal_model;
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
