/*
* Base class for pod content.
*/

package com.esria.samples.dashboard.view
{
import com.xvxv.aclass.DateTools;

import mx.containers.VBox;
import mx.controls.Alert;
import mx.events.FlexEvent;
import mx.events.IndexChangedEvent;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.http.HTTPService;
import mx.utils.ObjectProxy;

public class PodContentBase extends VBox
{
	[Bindable]
	public var properties:XML; // Properties are from pods.xml.
	protected var httpService:HTTPService = new HTTPService();
	protected var date:Date = new Date();
	protected var title:String = "";
	protected var dateScope:String = "";
	protected var params:Object = {};
	
	function PodContentBase()
	{
		super();
		percentWidth = 100;
		percentHeight = 100;
		addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
	}
	
	protected function onCreationComplete(e:FlexEvent):void
	{
		title = properties.@title;
		dateScope = properties.@dateScope;
		httpService.url = properties.@dataSource;
		
		var initDate:Number = Number(properties.@initDate);
		
		httpService.resultFormat = "e4x";
		httpService.addEventListener(FaultEvent.FAULT, onFaultHttpService);
		httpService.addEventListener(ResultEvent.RESULT, onResultHttpService);
		if(dateScope==null || dateScope=="")
			httpService.send();
		else{
			if(dateScope=="month"){
				date = DateTools.getUpOrDownMonth(date,initDate);
				Pod(parent).title = DateTools.getCnYMString(date) + title;
			}else if(dateScope=="day"){
				date = DateTools.getUpOrDownDay(date,initDate);
				Pod(parent).title = DateTools.getCnYMDString(date) + title;
			}else if(dateScope=="year"){
				date = DateTools.getUpOrDownYear(date,initDate);
				Pod(parent).title = DateTools.getCnYString(date) + title;
			}
			httpService.send({time:date.getTime()});
		}
	}
	
	protected function onFaultHttpService(e:FaultEvent):void
	{
		Alert.show("加载资源'"+properties.@dataSource+"'失败！查看是否已登录,或登录信息过期.");
	}
	
	// abstract.
	protected function onResultHttpService(e:ResultEvent):void	{}
	
	// Converts XML attributes in an XMLList to an Array.
	protected function xmlListToObjectArray(xmlList:XMLList):Array
	{
		var a:Array = new Array();
		for each(var xml:XML in xmlList)
		{
			var attributes:XMLList = xml.attributes();
			var o:Object = new Object();
			for each (var attribute:XML in attributes)
			{
				var nodeName:String = attribute.name().toString();
				var value:*;
				if (nodeName == "date")
				{
					var date:Date = new Date();
					date.setTime(Number(attribute.toString()));
					value = date;
				}
				else
				{
					value = attribute.toString();
				}
					
				o[nodeName] = value;
			}
			
			o.chlidNodes = xml.children();
			
			a.push(new ObjectProxy(o));
		}
		
		return a;
	}
	
	// Dispatches an event when the ViewStack index changes, which triggers a state save.
	// ViewStacks are only in ChartContent and FormContent.
	protected function dispatchViewStackChange(newIndex:Number):void
	{
		dispatchEvent(new IndexChangedEvent(IndexChangedEvent.CHANGE, true, false, null, -1, newIndex));
	}
}
}