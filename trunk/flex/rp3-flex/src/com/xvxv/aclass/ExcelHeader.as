package com.xvxv.aclass
{
	import mx.collections.ArrayCollection;
	
	public class ExcelHeader
	{
		private var setXml:XML;
		private var totalRow:int;
		public var columnIds:ArrayCollection = new ArrayCollection();
		public var headerStr:String;
		
		public function ExcelHeader(setXml:XML,tableName:String,type:String){
			this.setXml = setXml;
			function getLeaveSize():int{
				var leaveSizes:ArrayCollection = new ArrayCollection();
				function foreach(upRow:XMLList,upLeaveSize:int):void{
					var xml:XML;
					var str:String = "";
					for each(xml in upRow){
						var name:String = xml.name().toString();
				    	if(name=="group"){
				    		foreach(xml.elements(),upLeaveSize+1);
				    	}else if(name=="column"){
				    		leaveSizes.addItem(upLeaveSize+1);
				    	}
					}
				}
				foreach(setXml.elements(),0);
				var maxSize:int = leaveSizes[0];
				var size:int;
				for each(size in leaveSizes){
					maxSize = maxSize>size?maxSize:size;
				}
				return maxSize;
			}
			totalRow = getLeaveSize();
			headerStr = type=="pdf"?getRowDataPdf(tableName,type):getRowData(tableName,type);
		}
		
		private function getRowData(tableName:String,type:String):String{
			var i:int;
			function foreach(upRow:XMLList,rowSize:int):String{
				var hasNext:Boolean = false;
				var nextUpXml:XML =<downXml/>;
				var xml:XML;
				var str:String = "<tr bgcolor=\"#DFB6E7\">";
				for each(xml in upRow){
					var name:String = xml.name().toString();
			    	if(name=="group"){
			    		str+="<td colspan=\""+getSizeOfColumn(xml)+"\" align=\"center\" " + 
			    				"style=\"font-size:"+(xml.@fontSize.toString()!=""?xml.@fontSize:12)+"px\">"+
			    				xml.@headerText+
			    				"</td>";
			    		var downXml:XML;
			    		for each(downXml in xml.elements()){
			    			nextUpXml.appendChild(downXml);
			    		}
			    		hasNext=true;
			    	}else if(name=="column"){
			    		str+="<td rowspan=\""+(totalRow-rowSize)+"\" align=\"center\" " + 
			    				"width=\""+(xml.@width.toString()!=""?xml.@width:"100") +"\" "+ 
			    				"style=\"font-size:"+(xml.@fontSize.toString()!=""?xml.@fontSize:12)+"px;font-align:"+xml.@textAlign+"\" " + 
			    				"id=\""+(xml.@dataField.toString()!=""?xml.@dataField:"")+"\" " + 
			    				">"+
			    				xml.@headerText+
			    				"</td>";
			    		columnIds.addItem(xml.@dataField.toString()!=""?xml.@dataField:"");
			    	}
				}
				str+="</tr>";
				if(hasNext){
					str += foreach(nextUpXml.elements(),rowSize+1);
				}
				return str;
			}
			return "<tr bgcolor=\"#6699FF\"><td colspan=\""+getSizeOfColumn(setXml)+"\" align=\"center\" style=\"font-size:14px;color:#FFFFFF;font-weight:bold;font-align:left\">"+tableName+"</td></tr>"+foreach(setXml.elements(),0);
		}
		
		private function getRowDataPdf(tableName:String,type:String):String{
			var i:int;
			var columns:XML = <columns/>;
			var xml:XML;
			function foreach(upRow:XMLList,rowSize:int):String{
				var hasNext:Boolean = false;
				var nextUpXml:XML =<downXml/>;
				var str:String = "<tr bgcolor=\"#FFDFFF\">";
				for each(xml in upRow){
					var name:String = xml.name().toString();
			    	if(name=="group"){
			    		str+="<td colspan=\""+getSizeOfColumn(xml)+"\" align=\"center\" " + 
			    				"width=\""+(xml.@width.toString()!=""?xml.@width:"100") +"\" "+ 
			    				"style=\"font-size:"+(xml.@fontSize.toString()!=""?xml.@fontSize:12)+"px\">"+
			    				xml.@headerText+
			    				"</td>";
			    		var downXml:XML;
			    		for each(downXml in xml.elements()){
			    			nextUpXml.appendChild(downXml);
			    		}
			    	}else if(name=="column"){
			    		if(totalRow-rowSize>1){
			    			str+="<td>&nbsp;</td>";
			    			nextUpXml.appendChild(xml);
			    		}else{
				    		columns.appendChild(xml);
				    		columnIds.addItem(xml.@dataField.toString()!=""?xml.@dataField:"");
			    		}
			    	}
				}
				str+="</tr>";
				if(totalRow-rowSize>0){
					str += foreach(nextUpXml.elements(),rowSize+1);
				}
				return str;
			}
			var tr:String=foreach(setXml.elements(),0)+"<tr bgcolor=\"#FFDFFF\">";
			for each(xml in columns.elements()){
				tr+="<td align=\"center\" " + 
    				"style=\"font-size:"+(xml.@fontSize.toString()!=""?xml.@fontSize:12)+"px;font-align:"+xml.@textAlign+"\" " + 
    				"id=\""+(xml.@dataField.toString()!=""?xml.@dataField:"")+"\" " + 
    				">"+
    				xml.@headerText+
    				"</td>";
			}
			tr+="</tr>";
			return "<tr bgcolor=\"#6699FF\"><td colspan=\""+getSizeOfColumn(setXml)+"\" align=\"center\" style=\"font-size:14px;color:#FFFFFF;font-weight:bold;font-align:left\">"+tableName+"</td></tr>"+tr;
		}
		
		private function getSizeOfColumn(xml:XML):int{
			var columnSize:int = 0;
			function foreach(upRow:XMLList):void{
				var nextUpXml:XML = new XML("downXml");
				var xml:XML;
				var str:String = "";
				for each(xml in upRow){
					var name:String = xml.name().toString();
			    	if(name=="group"){
			    		foreach(xml.elements());
			    	}else if(name=="column"){
			    		columnSize++;
			    	}
				}
			}
			foreach(xml.elements());
			return columnSize;
		}
		
	}
}