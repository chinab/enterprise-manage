///////////////////////////////////////////////////////////////////////////////
//
//  Copyright (C) 2007-2008 ILOG, S.A.
//  All Rights Reserved.
//  The following is ILOG Sample Code.  Any usage of the ILOG Sample Code is  
//  subject to the terms and conditions of the ILOG End User License   
//  Agreement applicable to this ILOG software product. 
//
///////////////////////////////////////////////////////////////////////////////

package
{
  import flash.events.Event;
  import flash.events.EventDispatcher;
  import flash.events.IOErrorEvent;
  import flash.events.ProgressEvent;
  import flash.net.URLLoader;
  import flash.net.URLRequest;
  
  import mx.resources.IResourceManager;
  
  /**
   * A URLLoader wrapper that will try to load the 
   * file specified looking in directories named 
   * from the locales in the localeChain list from the 
   * resource manager.
   * If the "en_US" locale is not in the list, it will be 
   * automatically added.
   */  
  public class DataLoader extends EventDispatcher {
    
    private var _loader:URLLoader = new URLLoader();
    private var _localeChain:Array;
    private var _index:int = 0;
    private var _filename:String;
    private var _prefix:String;
    
    public var data:Object;
    
    public function DataLoader(resourceManager:IResourceManager, prefix:String = null) {
      _prefix = prefix;
      _localeChain = resourceManager.localeChain.concat();
      
      //add fallback on en_US
      if (_localeChain.indexOf("en_US") == -1) {
        _localeChain.push("en_US");
      }
      // when running from sample explorer, we need extra path
      if(_prefix != null) {
        var len:int = _localeChain.length;
        for(var i:int = 0; i < len; i++) {
          var s:String = _localeChain[i];
          _localeChain.push(_prefix + "/data/" + s);
        }
      }
      _loader.addEventListener(ProgressEvent.PROGRESS, progressHandler);
      _loader.addEventListener(Event.COMPLETE, completeHandler);
      _loader.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
    }
    
    public function load(filename:String):void {
      _filename = filename;
      _index = 0;
      loadImpl();      
    }      
    
    private function loadImpl():void {
      var locale:String = _localeChain[_index];
      _loader.load(new URLRequest(locale + "/" + _filename)); 
    }
    
    private function progressHandler(event:Event):void {
      dispatchEvent(event);
    }
    
    private function completeHandler(event:Event):void {
      data = _loader.data;
      dispatchEvent(event);     
    }
    
    private function errorHandler(event:IOErrorEvent):void {
      _index++;
       
      if (_index < _localeChain.length) {
        // not in the locale directory, try the next locale.
        loadImpl();
      } else {
        // not found in any locale, dispatch the event for the 
        // sample to handle the error. 
        dispatchEvent(event);
      }
    }                    
  }
}
