package com.artogrid.accountsystem.controls {
	import mx.controls.Alert;


	public class MyAlert {
		[Embed(source="assets/question.png")]
		public static var QuestionPng:Class;

		[Embed(source="assets/alert.png")]
		public static var AlertPng:Class;

		[Embed(source="assets/success.png")]
		public static var SuccessPng:Class;

		public static function showAlert(text:String):void {
			Alert.show(text, "提示", 4, null, null, AlertPng);
		}

		public static function showConfirm(text:String, closeHandler:Function):void {
			Alert.show(text, "确认", Alert.YES | Alert.NO, null, closeHandler, QuestionPng, Alert.NO);
		}

		public static function showSuccess(text:String):void {
			Alert.show(text, "提示", 4, null, null, SuccessPng);
		}
	}
}