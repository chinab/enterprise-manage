package cn.rtdata
{
	public class Condition
	{
		private var unitId:String;
		private var indexId:String;
		
		public function Condition(unitId:String,indexId:String){
			this.unitId = unitId;
			this.indexId = indexId;
		}
		
		public function getUnitId():String{
			return unitId;
		}
		public function getIndexId():String{
			return indexId;
		}
		public function setUnitId(unitId:String):void{
			this.unitId = unitId;
		}
		public function setIndexId(indexId:String):void{
			this.indexId = indexId;
		}
	}
}