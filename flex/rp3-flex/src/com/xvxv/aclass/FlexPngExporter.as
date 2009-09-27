package com.xvxv.aclass
{
	import flash.display.BitmapData;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.navigateToURL;
	

	public class FlexPngExporter
	{
		import mx.graphics.codec.PNGEncoder;
		import mx.core.UIComponent;

		public function FlexPngExporter()
		{
		}

		public static function export(target:UIComponent,width:int,height:int):void
		{
			var bitmapData:BitmapData=new BitmapData(width, height);
			bitmapData.draw(target);
			var request:URLRequest=new URLRequest("http://192.168.32.31:8080/ahboot/exportImageFlexReportExport.action");
//			var request:URLRequest=new URLRequest("/ahboot/exportImageFlexReportExport.action");
			request.contentType='applicatoin/octet-stream';
			request.data=(new PNGEncoder()).encode(bitmapData);
			request.method=URLRequestMethod.POST;
			navigateToURL(request, "_self");
		}
	}
}