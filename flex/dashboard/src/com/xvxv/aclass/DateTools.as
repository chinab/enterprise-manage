package com.xvxv.aclass
{
	import mx.controls.Alert;
	
	public class DateTools
	{
		public function DateTools()
		{
		}
		public static function getUpOrDownMonth(date:Date,size:int=1):Date{
			var month:Number = date.getMonth();
			var year:Number = date.getFullYear();
			var dateday:Number = date.getDate();
			var m:int=(int)(month);
			var y:int=(int)(year);
			var d:int=(int)(dateday);
			var ySize:int;
			var mSize:int;
			if(size<0){
				size = size*-1;
				ySize=size/12;
				mSize=size%12;
				if(mSize>m){
					y=y-1-ySize;
					m=12+m-mSize;
				}else{
					y=y-ySize;
					m=m-mSize;
				}
			}else if(size>0){
				ySize=size/12;
				mSize=size%12;
				if(mSize>(11-m)){
					y=y+1+ySize;
					m=mSize+m-12;
				}else{
					y=y+ySize;
					m=m+mSize;
				}
			}else{
				return date;
			}
			
			date = setYMD(date,y,m,d);
			
			return date;
		}
		
		public static function isLeap(y:int):Boolean{
			return (y%4==0&&y%100!=0)||y%400==0?true:false;
		}
		
		public static function setYMD(date:Date,y:int,m:int,d:int):Date{
			var maxD:int;
			switch (m+1) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					maxD = 31;
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					maxD = 30;
					break;
				case 2:
					if(isLeap(y))maxD = 29;
					else maxD = 28;
					break;
				default:
					maxD = 1;
					break;
			}
			date.setDate((d>maxD)?maxD:d);
			date.setMonth(m);
			date.setFullYear(y);
			return date;
		}
		
		public static function getUpOrDownDay(date:Date,size:int=1):Date{
			return new Date(date.getTime()+size*1000*60*60*24);
		}
		public static function getUpOrDownYear(date:Date,size:int=1):Date{
			date.setFullYear(date.getFullYear());
			return date;
		}
		
		public static function getYString(date:Date):String{
			return date.getFullYear().toString();
		}
		public static function getYMString(date:Date):String{
			var y:Number=date.getFullYear();
			var m:Number=date.getMonth();
			m=m+1;
			if(m<=9){
				return y.toString()+"-0"+m.toString();
			}else{
				return y.toString()+"-"+m.toString();
			}
		}
		public static function getYMDString(date:Date):String{
			var y:Number=date.getFullYear();
			var m:Number=date.getMonth();
			var d:Number=date.getDate();
			m=m+1;
			var dstr:String = d<=9?"0"+d:d.toString();
			var mstr:String = m<=9?"0"+m:m.toString();
			return y.toString()+"-"+mstr+"-"+dstr;
		}
		public static function getCnYString(date:Date):String{
			return date.getFullYear().toString()+"年";
		}
		public static function getCnYMString(date:Date):String{
			var y:Number=date.getFullYear();
			var m:Number=date.getMonth();
			m=m+1;
			if(m<=9){
				return y.toString()+"年0"+m.toString()+"月";
			}else{
				return y.toString()+"年"+m.toString()+"月";
			}
		}
		public static function getCnYMDString(date:Date):String{
			var y:Number=date.getFullYear();
			var m:Number=date.getMonth();
			var d:Number=date.getDate();
			m=m+1;
			var dstr:String = d<=9?"0"+d:d.toString();
			var mstr:String = m<=9?"0"+m:m.toString();
			return y.toString()+"年"+mstr+"月"+dstr+"日";
		}
	}
}