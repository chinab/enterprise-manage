package com.xvxv.aclass
{
	import mx.controls.SWFLoader;
	import mx.managers.SystemManager;
	
	public class AmchartsLoader extends SWFLoader
	{
		import mx.controls.SWFLoader;
		private var _swfUrl:String = "";
		private var _path:String = "";
		private var _settingsFile:String = "";
		private var _dataFile:String = "";
		public function AmchartsLoader()
		{
			super();
		}
		
		public function set swfUrl(swfUrl:String):void{
			while(swfUrl.indexOf("&")!=-1) swfUrl = swfUrl.replace("&", "%26");
			this._swfUrl = swfUrl;
		}
		
		public function set path(path:String):void{
			while(path.indexOf("&")!=-1) path = path.replace("&", "%26");
			this._path = path;
		}
		
		public function set settingsFile(settingsFile:String):void{
			while(settingsFile.indexOf("&")!=-1) settingsFile = settingsFile.replace("&", "%26");
			this._settingsFile = settingsFile;
		}
		
		public function set dataFile(dataFile:String):void{
			while(dataFile.indexOf("&")!=-1) dataFile = dataFile.replace("&", "%26");
			this._dataFile = dataFile;
		}

		public function loadChart():void{
//			super.load(_swfUrl);
//			var sm:SystemManager = SystemManager(super.content);
//			sm.application["path"].text  = _path.replace("&", "%26");
//			sm.application["settings_file"].text  = _settingsFile.replace("&", "%26");
//			sm.application["data_file"].text  = _dataFile.replace("&", "%26");
//			load(_swfUrl);
			
			load(_swfUrl+"?path="+_path+"&settings_file="+_settingsFile+"&data_file="+_dataFile);
		}
	}
}