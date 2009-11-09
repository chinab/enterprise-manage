package components.bean
{
	public class TaskBean
	{
		private var _name:String;
		private var _assignment:String;
		
		public function set name(v:String):void{
			_name = v;
		}
		
		public function get name():String{
			return _name;
		}
		
		public function set assignment(v:String):void{
			_assignment= v;
		}
		
		public function get assignment():String{
			return _assignment;
		}
		
		public function TaskBean(nv:String,av:String){
			this._name = nv;
			this._assignment = av;
		}

	}
}