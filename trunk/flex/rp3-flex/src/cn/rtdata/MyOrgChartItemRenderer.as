package cn.rtdata
{
	import flash.events.MouseEvent;
	
	import ilog.orgchart.OrgChartItem;
	import ilog.orgchart.OrgChartItemRenderer;

	public class MyOrgChartItemRenderer extends OrgChartItemRenderer
	{
		public function MyOrgChartItemRenderer(){
			super();
		}
	    override protected  function applyData(data:OrgChartItem):void {             
	    	super.applyData(data);
		    if (image != null) {
		    	image.width = 20 ;
		    }
	    }
	}
}