package com.xvxv.aclass
{	
	import flash.display.DisplayObject;
	import flash.display.Loader;
	import flash.display.MovieClip;
	import flash.events.*;
	import flash.net.URLRequest;
	
	import mx.core.UIComponent;
	
	/**
	 * http://livedocs.adobe.com/flex/3/html/help.html?content=Working_with_MovieClips_7.html
	 * 
	 * dispatch Event.COMPLETE when swf load complete.
	 * dispatch HTTPStatusEvent.HTTP_STATUS when network is error.
	 * dispatch IOErrorEvent.IO_ERROR when load swf error.
	 */
	public class ExternalSwfLoader extends UIComponent {
		
		private var loader : Loader;
		
		private var visibleAfterLoaded : Boolean;
		
		private var progressHandle : Function;
		
		private var loadSwfComplete : Boolean = false;;
		
		public function ExternalSwfLoader(progressHandle:Function = null, visibleAfterLoaded:Boolean = true) {
			super();
			init(progressHandle, visibleAfterLoaded);
		}
		
		protected function init(progressHandle:Function = null, visibleAfterLoaded:Boolean = true) : void {
			this.visibleAfterLoaded = visibleAfterLoaded;
			this.progressHandle = progressHandle;
			unLoad();
		}
		
		public function get content() : DisplayObject {
			if (loadSwfComplete) {
				return loader.content;
			}
			return null;
		} 
		
		public function get movieClip() : MovieClip {
			if (this.content) {
				return this.content as MovieClip;
			}
			return null;
		}
		
		public function loadSwf(externalSwfUrl:String, progressHandle:Function = null, visibleAfterLoaded:Boolean = true) : void {
			init(progressHandle, visibleAfterLoaded);
			
			//"http://www.[yourdomain].com/externalSwf.swf"
			var request:URLRequest = new URLRequest(externalSwfUrl);
			loader = new Loader();
			addListeners(loader.contentLoaderInfo);
			loader.load(request);
			
			if (visibleAfterLoaded) {
				addChild(loader);
			}
		}
		
		private function addListeners(dispatcher:IEventDispatcher) :void {
            dispatcher.addEventListener(Event.COMPLETE, completeHandler);
            dispatcher.addEventListener(HTTPStatusEvent.HTTP_STATUS, httpStatusHandler);
            dispatcher.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
            if (progressHandle != null) {
				dispatcher.addEventListener(ProgressEvent.PROGRESS, progressHandle);
            }
		}

		/**
		 *	load external swf successful.
		 */
		private function completeHandler(event:Event) : void {
			this.loadSwfComplete = true;
			this.width = loader.content.width;
			this.height = loader.content.height;
			dispatchEvent(new Event(Event.COMPLETE));
		}
		
		private function httpStatusHandler(event:Event) : void {
			dispatchEvent(new Event(HTTPStatusEvent.HTTP_STATUS));
		}
		
		private function ioErrorHandler(event:Event) : void {
			dispatchEvent(new Event(IOErrorEvent.IO_ERROR));
		}
		
		public function unLoad() : void {
			if (loadSwfComplete) {
				try {
					loader.unload();
					if (visibleAfterLoaded) {
						removeChild(loader);
					}
					loader = null;
					this.parent.removeChild(this);
				} catch (e:Error) {
					
				}
				loadSwfComplete = false;
			}
		}

	}
}