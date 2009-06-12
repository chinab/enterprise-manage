/**
Ext.form.BasicForm.prototype.validateDateChron = function(start, end, config) {
		var startdate, enddate, fn;
		start = (start instanceof Ext.form.DateField) ? start : this.findField(start);
		end = (end instanceof Ext.form.DateField) ? end: this.findField(end);
	
		if( !start || !end ) {
			return false;
		}
	
		var opts = {
			allowSameDay: true,
			invalidText: String.format('{0} 大于 {1}', start.hiddenName || start.getEl().id, end.hiddenName || end.getEl().id)
		};
		Ext.apply(opts, config);
		
		fn = function(start, end, opts) {
			// Only compare if both fields were found and after the base validation is successful
			if( !this.isValid() || !(this === start ? end.isValid() : start.isValid()) ) {
				return false;
			}
			startdate = Date.parse(start.getValue().clearTime(true));
			enddate = Date.parse(end.getValue().clearTime(true));
			if( (opts.allowSameDay && startdate > enddate) || (!opts.allowSameDay && startdate >= enddate) ) {
				start.markInvalid(opts.invalidText);
				end.markInvalid(opts.invalidText);
				return false;
			}
			return true;
		};
		
		start.validate = start.validate.createInterceptor(fn.createDelegate(start, [start, end, opts]));
		end.validate = end.validate.createInterceptor(fn.createDelegate(end, [start, end, opts]));
		
		return true;
	};
	
	*/
rtdata={common:{}};
rtdata.common.compareDate = function(formPanel,start,end,allowSameDay){
	var startdate, enddate, fn;
		start = (start instanceof Ext.form.DateField) ? start : formPanel.form.findField(start);
		end = (end instanceof Ext.form.DateField) ? end: formPanel.form.findField(end);
		
		
	
		if( !start || !end ) {
			return false;
		}
		
		if( ! end.isValid() || !start.isValid()) {
			return false;
		}
		if(!start.getValue()||!end.getValue()){
			return true
		}
		/**
		if(start.getValue()==null||end.getValue()==null){
			return true;
		}*/
		
		startdate = Date.parse(start.getValue().clearTime(true));
		enddate = Date.parse(end.getValue().clearTime(true));
		if((allowSameDay && startdate > enddate) || (!allowSameDay && startdate >= enddate)){
			return false;
		}
		return true;
}
rtdata.common.getToday = function(){
	var pad = function(n) {
		return n < 10 ? "0" + n : n
	};
	
	var o = new Date();
	 return  o.getFullYear() + "-" + pad(o.getMonth() + 1) + "-"
		 + pad(o.getDate()) ;
}
rtdata.common.getTodayMonth = function(){
	var pad = function(n) {
		return n < 10 ? "0" + n : n
	};
	
	var o = new Date();
	 return  o.getFullYear() + "-" + pad(o.getMonth() + 1);
}