package util
{
	import flash.events.Event;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.net.sendToURL;
	import flash.utils.ByteArray;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.formatters.DateFormatter;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import view.InputFlowNameView;
	import view.OpenFlowView;
	
	public class Utils
	{
		public function Utils()
		{
		}
		
		private static var i:int = 0;
		
		private static var dateFormatter:DateFormatter = new DateFormatter();
		
		private static var commentColor:String = "#3f5fbf";
	
		private static var tagColor:String = "#3f7f5f";
		
		private static var attributeColor:String = "#7f0055";
		
		private static var equalColor:String = "#000000";
		
		private static var stringColor:String = "#2a00ff";
		
		private static var entityColor:String = "#2a00ff";
		
		private static var dataColor:String = "#3f7f5f";
		
//		private static var rootUrl:String = "http://192.168.32.31:9999/jbpm/";
		private static var rootUrl:String = "/jbpm/";

		public static function deploy(flowData:Object,xmlString:String):void{
			Alert.show("确定发布流程"+flowData.flowName+"？", "确认", Alert.YES|Alert.NO, null, function(dlg_obj:Object):void{
				var variables:URLVariables = new URLVariables();
				variables.pdXmlString = xmlString;
				
				var request:URLRequest = new URLRequest(rootUrl+"deployJbpmDesigner.action");
				request.data = variables;
				request.method=URLRequestMethod.POST;
				sendToURL(request);
				Alert.show("发布流程"+flowData.flowName+"成功","提示");
			}, null, Alert.YES);
		}
		
		public static function saveFlow(flowData:Object,successFunction:Function=null):void{
			var hs:HTTPService = new HTTPService();
			hs.url = rootUrl+"browseFlowJbpmDesigner.action";
			hs.resultFormat = HTTPService.RESULT_FORMAT_XML;
			hs.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
					var xml:XML = XML(event.result);
					var flowXmlList:XMLList = xml.flow;
					var alreadyHas:Boolean = false;
					for each(var flowXml:XML in flowXmlList){
						var tempName:String = String(flowXml.@name).replace(".rfl","");
						if(tempName==flowData.flowName){
							alreadyHas = true;
							break;
						}
					}
					if(alreadyHas){
						Alert.show("流程"+flowData.flowName+"已存在，确认要覆盖？", "确认", Alert.YES|Alert.NO, null, function(dlg_obj:Object):void{
								if (dlg_obj.detail == Alert.YES){
									onSaveFlow(flowData,successFunction);
								}
							}, null, Alert.YES);
					}else{
						var inputFlowNameView:InputFlowNameView = new InputFlowNameView();
						inputFlowNameView.flowNameInputText =	flowData.flowName;
						inputFlowNameView.saveFunction = function(flowName:String):void{
							flowData.flowName = flowName;
							Application.application.flow.label = flowData.flowName + '-流程图';
							onSaveFlow(flowData,successFunction);
						}
						PopUpManager.addPopUp(inputFlowNameView,Application.application.document);
	        			PopUpManager.centerPopUp(inputFlowNameView);
					}
				}
			)
			hs.send();
		}
		
		public static function saveAsFlow(flowData:Object,successFunction:Function=null):void{
			var inputFlowNameView:InputFlowNameView = new InputFlowNameView();
			inputFlowNameView.flowNameInputText =	flowData.flowName;
			inputFlowNameView.saveFunction = function(flowName:String):void{
				flowData.flowName = flowName;
				Application.application.flow.label = flowData.flowName + '-流程图';
				onSaveFlow(flowData,successFunction);
			}
			PopUpManager.addPopUp(inputFlowNameView,Application.application.document);
			PopUpManager.centerPopUp(inputFlowNameView);
		}
		
		private static function onSaveFlow(flowData:Object,successFunction:Function=null):void{
			var b:ByteArray = new ByteArray();
			b.writeObject(flowData);
            var request:URLRequest = new URLRequest(rootUrl+"saveFlowJbpmDesigner.action?flowName="+flowData.flowName);
            request.contentType='applicatoin/octet-stream';
			request.data=b;
			request.method=URLRequestMethod.POST;
			sendToURL(request);
			Alert.show("保存成功","提示");
			if(successFunction!=null){
				successFunction.call(Application.application);
			}
		}
		
		public static function openFlow(flowPath:String):void{
			var variables:URLVariables = new URLVariables();
			variables.flowPath = flowPath;
			
			var request:URLRequest = new URLRequest(rootUrl+"openFlowJbpmDesigner.action");
			request.data = variables;
			request.method=URLRequestMethod.POST;
			
			var loader:URLLoader = new URLLoader();
			loader.dataFormat = URLLoaderDataFormat.BINARY;
			loader.load(request);
			loader.addEventListener(Event.COMPLETE,function(event:Event):void{
					var b:ByteArray = ByteArray(event.target.data);
					var flowData:Object = b.readObject();
					Application.application.activationAll(flowData);
				}
			);
		}
		
		public static function browseFlow():void{
			dateFormatter.formatString = "YYYY-MM-DD";
			var hs:HTTPService = new HTTPService();
			hs.url = rootUrl+"browseFlowJbpmDesigner.action";
			hs.resultFormat = HTTPService.RESULT_FORMAT_XML;
			hs.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
					var xml:XML = XML(event.result);
					var flowXmlList:XMLList = xml.flow;
					for each(var flowXml:XML in flowXmlList){
						flowXml.@lastModified = dateFormatter.format(new Date(Number(flowXml.@lastModified)));
					}
					
					var openFlowView:OpenFlowView = new OpenFlowView();
					openFlowView.flowXmlList = flowXmlList;
					PopUpManager.addPopUp(openFlowView,Application.application.document);
	        		PopUpManager.centerPopUp(openFlowView);
				}
			)
			hs.send();
		}
		
		public static function filter(text:String):String {
			var result:String = text;
//			// 过滤CDATA    //TODO 应该文本化CDATA里面的标签 
//			result = stringReplaceAll("<!\\[CDATA\\[([^(\\]\\])]*)\\]\\]>", "[datatempfont][lesstempsign]![CDATA[[/endtempfont]$1[datatempfont]]][greattempsign][/endtempfont]",result);   
//			// 过滤注释   
//			result = stringReplaceAll("<!--", "[commenttempfont][lesstempsign]!--$1--[greattempsign][/endtempfont]",result);
//			result = stringReplaceAll("-->", "[commenttempfont][lesstempsign]!--$1--[greattempsign][/endtempfont]",result);
//			// 过滤标签   
//			result = stringReplaceAll("<([^(<|>)]+)>", "[tagtempfont][lesstempsign]$1[greattempsign]</font><br/>",result);   
//			// 过滤属性(双引号)    //TODO 单双引号应改为反向引用方式  
//			result = stringReplaceAll("(\\s+)([\\w|:]+)(\\s*)\\=(\\s*)(\"[^\"]*\")", "$1[attributetempfont]$2</font>$3[equaltempfont]=</font>$4[stringtempfont]$5</font>",result);   
//			// 过滤属性(单引号)   
//			result = stringReplaceAll("(\\s+)([\\w|:]+)(\\s*)\\=(\\s*)(\'[^\']*\')", "$1[attributetempfont]$2</font>$3[equaltempfont]=</font>$4[stringtempfont]$5</font>",result);   
//			// 过滤&符号   
//			//result = str_replace("&", "&amp;");   
//			// 过滤dtd实体   
//			result = stringReplaceAll("(&amp;[A-Z|a-z]+;)", "[entitytempfont]$1</font>",result);   
//			// 过滤HtmlUnicode转码   
//			result = stringReplaceAll("(&amp;#[0-9]+;)", "[entitytempfont]$1</font>",result);
//			// 过滤空格   
//			result = stringReplaceAll(" ", "&nbsp;",result);   
//			// 过滤缩进   
//			result = stringReplaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;",result);   
//			// 过滤Winodws换行 
//			result = stringReplaceAll("\r\n", "\n",result);   
//			// 过滤换行   
//			result = stringReplaceAll("\n", "<br/>\n",result);
//			// 下面的替换把上面作的标记换成相应颜色   
//			result = stringReplaceAll("\\[lesstempsign\\]", "&lt;",result);
//			result = stringReplaceAll("\\[greattempsign\\]", "&gt;",result);   
//			result = stringReplaceAll("\\[/endtempfont\\]", "</font>",result);   
//			result = stringReplaceAll("\\[commenttempfont\\]", "<font color=\"" + commentColor + "\">",result);   
//			result = stringReplaceAll("\\[datatempfont\\]", "<font color=\"" + dataColor + "\">",result);   
//			result = stringReplaceAll("\\[tagtempfont\\]", "<font color=\"" + tagColor + "\">",result);   
//			result = stringReplaceAll("\\[attributetempfont\\]", "<font color=\"" + attributeColor + "\">",result);   
//			result = stringReplaceAll("\\[equaltempfont\\]", "<font color=\"" + equalColor + "\">",result);   
//			result = stringReplaceAll("\\[stringtempfont\\]", "<font color=\"" + stringColor + "\">",result);   
//			result = stringReplaceAll("\\[entitytempfont\\]", "<font color=\"" + entityColor + "\">",result);   
//			//return "<font color=\"" + textColor + "\">" + result + "</font>";   
			return result;
		}
		
		public static function stringReplaceAll(regex:String, replacement:String, haystack:String):String{
			var str:String = new String(haystack);
			while(str.search(regex)!=-1){
				var strArr:Array = str.match(regex);
				str = strArr.join(replacement);
			}
			return str;
		}
		
		public static function getCode():int{
			return i++;
		}
		
//		/**
//		 * 产生一个指定范围的随机数
//		*/
//		public static function random(min: int, max: int): int{
//			return int((max - min) * Math.random() + 1 + min);
//		}
//		
//		/**
//		 * 	生成一个随机字符串,长度由参数指定
//		*/
//		public static function randomString(n: int): String{
//			var i: int = 0;
//			var str: String = "";
//			while(i < n){
//				str += random(0, 9);
//				i ++;
//			}
//			return str;
//		}
		
		public static function nodeHTML(node: String): String{
			return "<b style='color:blue;'>" + node + "</b>";
		}
		
		public static function attributeHTML(attribute: String): String{
			return "<i style='color:red;'>" + attribute + "</i>";
		}
		
		public static function space(n: int): String{
			var i: int = 0;
			var str: String = "";
			while(i < n){
				str += "&nbsp;";
				i ++;
			}
			return str;
		}
		
	}
}