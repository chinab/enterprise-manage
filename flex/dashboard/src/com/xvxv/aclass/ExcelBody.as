package com.xvxv.aclass
{
	import mx.collections.ArrayCollection;
	
	public class ExcelBody
	{
		public var bodyStr:String;
		public function ExcelBody(dataXml:XML,columnIds:ArrayCollection){
			bodyStr = "";
			function foreach(parent:XML):void{
				var columnId:String;
				bodyStr+="<tr bgcolor=\"#CCFFFF\" style=\"font-size:12px\">";
				for each(columnId in columnIds){
					bodyStr+="<td align=\"center\">"+(parent.attribute(columnId)!=null||parent.attribute(columnId).toString().replace(" ","")!=""?parent.attribute(columnId):"&nbsp;")+"</td>";
				}
				bodyStr+="</tr>";
				var xml:XML;
				for each(xml in parent.elements()){
					foreach(xml);
				}
			}
			foreach(dataXml);
		}

	}
}