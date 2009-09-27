package com.xvxv.aclass{
	import mx.collections.XMLListCollection;
	
	public class XmlTools{
		public static function treeToList(treeXml:XML):XMLListCollection{
			var xmlListCollection:XMLListCollection = new XMLListCollection();
			var copyTreeXml:XML = treeXml.copy();
			
			function forEach(xml:XML):void{
				xmlListCollection.addItem(xml.copy().setChildren(""));
				var childXml:XML ;
				var size:int = xml.children().length();
				if(xml.elements()!=null&&size>0){
					for each(childXml in xml.elements()){
						forEach(childXml)
					}
				}
			}
			
			forEach(copyTreeXml);
			return xmlListCollection;
		}
		
		public static function getXmlItemByIndex(xml:XML,index:int):XML{
			var i:int = 0;
			var resultXml:XML=xml;
			function foreach(currentXml:XML):void{
				if(i==index) resultXml = currentXml;
				i=i+1;
				var xmlList:XMLList = currentXml.children();
				for each(var nextXml:XML in xmlList){
					foreach(nextXml);
				}
			}
			foreach(xml);
			return resultXml;
		}
		
		public static function getReverseXmlList(xmlList:XMLList):XMLList{
			var resultXmlList:XMLList = new XMLList(xmlList.length());
			for(var i:int=0;i<xmlList.length();i++){
				resultXmlList[i] = xmlList[xmlList.length()-i-1];
			}
			return resultXmlList;
		}
	}
}