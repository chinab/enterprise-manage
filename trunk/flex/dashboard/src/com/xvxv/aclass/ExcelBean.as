package com.xvxv.aclass
{
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.net.navigateToURL;
	import mx.collections.ArrayCollection;
	
	public class ExcelBean
	{
		private var tableType:String;
		private var tableStr:String;
		private var tableName:String;
		public function ExcelBean(setXml:XML,dataXml:XML,valueDate:String,type:String)
		{
			tableType=type;
			tableName = valueDate+setXml.@headerText;
			var excelHeader:ExcelHeader=new ExcelHeader(setXml,tableName,type);
			var columnIds:ArrayCollection = excelHeader.columnIds;
			var excelBody:ExcelBody = new ExcelBody(dataXml,columnIds);
			if("excel"==type){
				tableStr = "<table border=\"1\"  cellpadding=\"0\">";
			}else if("word"==type){
				tableStr = "<table cellpadding=\"0\" cellspacing=\"1\">";
			}else if("pdf"==type){
				tableStr = "<table cellpadding=\"0\" cellspacing=\"1\">";
			}else if("html"==type){
				tableStr = "<table cellpadding=\"0\" cellspacing=\"1\">";
			}
			tableStr+=excelHeader.headerStr;
			tableStr+=excelBody.bodyStr;
			tableStr+="</table>";
		}
		
		public function loadExcel(url:String):void {
			var variables:URLVariables = new URLVariables();
			variables.tableType	= tableType;
			variables.tableStr	= tableStr;
			variables.tableName	= tableName;
			var urlRequest:URLRequest = new URLRequest(url);
			urlRequest.data = variables;
			urlRequest.method = URLRequestMethod.POST;
		    navigateToURL(urlRequest,"_self");
		}
	}
}