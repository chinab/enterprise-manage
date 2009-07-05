Ext.onReady(function(){
	var so = new SWFObject("/boot/test/main.swf", "mymovie", "100%", "100%", "7", "#336699");
	
	so.addParam("wmode", "opaque");
	so.write("welcome_portal_swf_div");
});