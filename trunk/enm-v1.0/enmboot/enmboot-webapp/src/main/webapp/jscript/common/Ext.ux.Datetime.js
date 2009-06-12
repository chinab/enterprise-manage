/**
Ext.DateTimePicker = function(config) {
	Ext.DateTimePicker.superclass.constructor.call(this, config);
	this.value = config && config.value ? config.value : new Date();
	this.addEvents( {
		select : true
	});
	if (this.handler) {
		this.on("select", this.handler, this.scope || this);
	}
}
Ext.extend(Ext.DateTimePicker,Ext.Component,{
	format : 'Y-m-d H:i:s',
	aryWeek : ['日', '一', '二', '三', '四', '五', '六'],
	aryMonth : ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月','九月', '十月', '十一', '十二'],
	setValue : function(value) {
		this.value = value;
		this.update(this.value);
	},
	getValue : function() {
		return this.value;
	},
	onRender : function(container, position) {
		var m = [
				'<table cellspacing="0">',
				'<tr><td class="x-date-left"><a href="#" title="上个月">&#160;</a></td><td class="x-date-middle" align="center"></td><td class="x-date-right"><a href="#" title="下个月">&#160;</a></td></tr>',
				'<tr><td colspan="3"><table class="x-date-inner" cellspacing="0"><thead><tr>'];
		for (var i = 0;i < this.aryWeek.length; i++)
			m.push("<th><span>", this.aryWeek[i],"</span></th>");
		m[m.length] = "</tr></thead><tbody><tr>";
		for (var i = 0;i < 42; i++) {
			if (i % 7 == 0 && i != 0)
				m[m.length] = "</tr><tr>";
			m[m.length] = '<td><a href="#" hidefocus="on" class="x-date-date" tabIndex="1"><em><span></span></em></a></td>';
		}
		m[m.length] = '</tr></tbody></table>';
		m[m.length] = '<tr><td colspan="3" align="center"><em class="x-date-middle" style="cursor:hand"><span class="x-date-date-h"></span></em>:<em class="x-date-middle" style="cursor:hand"><span class="x-date-date-m"></span></em>:<em class="x-date-middle" style="cursor:hand"><span class="x-date-date-s"></span></em></td></tr>';
		m[m.length] = '</td></tr><tr><td colspan="3" class="x-date-bottom" align="center"></td></tr></table><div class="x-date-mp"></div><div class="x-date-mp-h" style="position:absolute;left:0;top:0;display:none;background:white"></div><div class="x-date-mp-m" style="position:absolute;left:0;top:0;display:none;background:white"></div><div class="x-date-mp-s" style="position:absolute;left:0;top:0;display:none;background:white"></div>';
		var el = document.createElement("div");
		el.className = "x-date-picker";
		el.innerHTML = m.join("");
		container.dom.insertBefore(el, position);
		this.el = Ext.get(el);
		this.eventEl = Ext.get(el.firstChild);
		new Ext.util.ClickRepeater(this.el.child("td.x-date-left a"), {
			handler : this.showPrevMonth,
			scope : this,
			preventDefault : true,
			stopDefault : true
		});
		new Ext.util.ClickRepeater(this.el.child("td.x-date-right a"), {
			handler : this.showNextMonth,
			scope : this,
			preventDefault : true,
			stopDefault : true
		});
		this.monthPicker = this.el.down('div.x-date-mp');
		this.monthPicker.enableDisplayMode('block');
		this.hourPicker = this.el.down('div.x-date-mp-h');
		this.hourPicker.enableDisplayMode('block');
		this.minutePicker = this.el.down('div.x-date-mp-m');
		this.minutePicker.enableDisplayMode('block');
		this.secondPicker = this.el.down('div.x-date-mp-s');
		this.secondPicker.enableDisplayMode('block');
		this.eventEl.on("click", this.handleDateClick, this, {
			delegate : "a.x-date-date"
		});
		this.eventEl.on("dblclick", this.handleDblDateClick,this, {
			delegate : "a.x-date-date"
		});
		this.el.unselectable();
		this.cells = this.el.select("table.x-date-inner tbody td");
		this.textNodes = this.el.query("table.x-date-inner tbody span");
		this.selHour = this.el.select("span.x-date-date-h").item(0);
		this.selMinute = this.el.select("span.x-date-date-m").item(0);
		this.selSecond = this.el.select("span.x-date-date-s").item(0);
		this.selHour.on("click", this.showHourPicker, this);
		this.selMinute.on("click", this.showMinutePicker, this);
		this.selSecond.on("click", this.showSecondPicker, this);
		this.mbtn = new Ext.Button(this.el.child("td.x-date-middle", true), {
			text : "&#160;"
		});
		this.mbtn.on('click', this.showMonthPicker, this);
		this.mbtn.el.child(this.mbtn.menuClassTarget).addClass("x-btn-with-menu");
		var okBtn = new Ext.Button(this.el.child("td.x-date-bottom", true), {
			text : '确定',
			handler : this.selectOK,
			scope : this
		});
		if (Ext.isIE)
			this.el.repaint();
		this.update(this.value);
	},
	createMonthPicker : function() {
		if (!this.monthPicker.dom.firstChild) {
			var buf = ['<table border="0" cellspacing="0">'];
			for (var i = 0;i < 6; i++) {
				buf.push(
				'<tr><td class="x-date-mp-month"><a href="#">',
				this.aryMonth[i],
				'</a></td>',
				'<td class="x-date-mp-month x-date-mp-sep"><a href="#">',
				this.aryMonth[i + 6],
				'</a></td>',
				i == 0
					? '<td class="x-date-mp-ybtn" align="center"><a class="x-date-mp-prev"></a></td><td class="x-date-mp-ybtn" align="center"><a class="x-date-mp-next"></a></td></tr>'
					: '<td class="x-date-mp-year"><a href="#"></a></td><td class="x-date-mp-year"><a href="#"></a></td></tr>');
			}
			buf.push(
			'<tr class="x-date-mp-btns"><td colspan="4"><button type="button" class="x-date-mp-ok">',
			'确定', 
			'</button></td></tr>',
			'</table>');
			this.monthPicker.update(buf.join(''));
			this.monthPicker.on('click', this.onMonthClick,this);
			this.monthPicker.on('dblclick',this.onMonthDblClick, this);
			this.mpMonths = this.monthPicker.select('td.x-date-mp-month');
			this.mpYears = this.monthPicker.select('td.x-date-mp-year');
			this.mpMonths.each(function(m, a, i) {
				i += 1;
				if ((i % 2) == 0)
					m.dom.xmonth = 5 + Math.round(i * .5);
				else
					m.dom.xmonth = Math.round((i - 1) * .5);
			});
		}
	},
	showMonthPicker : function() {
		this.createMonthPicker();
		var size = this.el.getSize();
		this.monthPicker.setSize(size);
		this.monthPicker.child('table').setSize(size);
		this.updateMPMonth(this.value.getMonth());
		this.updateMPYear(this.value.getFullYear());
		this.monthPicker.slideIn('t', {
			duration : .2
		});
	},
	updateMPYear : function(y) {
		this.mpyear = y;
		var ys = this.mpYears.elements;
		for (var i = 1;i <= 10; i++) {
			var td = ys[i - 1], y2;
			if ((i % 2) == 0) {
				y2 = y + Math.round(i * .5);
				td.firstChild.innerHTML = y2;
				td.xyear = y2;
			} else {
				y2 = y - (5 - Math.round(i * .5));
				td.firstChild.innerHTML = y2;
				td.xyear = y2;
			}
			this.mpYears.item(i - 1)[y2 == y ? 'addClass' : 'removeClass']('x-date-mp-sel');
		}
	},
	updateMPMonth : function(sm) {
		this.mpMonths.each(function(m, a, i) {
			m[m.dom.xmonth == sm ? 'addClass' : 'removeClass']('x-date-mp-sel');
		});
	},
	onMonthClick : function(e, t) {
		e.stopEvent();
		var el = new Ext.Element(t), pn;
		if (el.is('button.x-date-mp-ok')) {
			this.update(this.value);
			this.hideMonthPicker();
		} else if (pn = el.up('td.x-date-mp-month', 2)) {
			this.mpMonths.removeClass('x-date-mp-sel');
			pn.addClass('x-date-mp-sel');
			this.value.setMonth(pn.dom.xmonth);
		} else if (pn = el.up('td.x-date-mp-year', 2)) {
			this.mpYears.removeClass('x-date-mp-sel');
			pn.addClass('x-date-mp-sel');
			this.value.setYear(pn.dom.xyear);
		} else if (el.is('a.x-date-mp-prev')) {
			this.updateMPYear(this.mpyear - 10);
		} else if (el.is('a.x-date-mp-next')) {
			this.updateMPYear(this.mpyear + 10);
		}
	},
	onMonthDblClick : function(e, t) {
		e.stopEvent();
		var el = new Ext.Element(t), pn;
		if (pn = el.up('td.x-date-mp-month', 2)) {
			this.value.setMonth(pn.dom.xmonth);
			this.update(this.value);
			this.hideMonthPicker();
		} else if (pn = el.up('td.x-date-mp-year', 2)) {
			this.value.setYear(pn.dom.xyear);
			this.update(this.value);
			this.hideMonthPicker();
		}
	},
	hideMonthPicker : function(disableAnim) {
		if (this.monthPicker)
			if (disableAnim === true)
				this.monthPicker.hide();
			else
				this.monthPicker.slideOut('t', {
					duration : .2
				});
	},
	handleDateClick : function(e, t) {
		e.stopEvent();
		if (!Ext.fly(t.parentNode).hasClass("x-date-prevday")
				&& !Ext.fly(t.parentNode).hasClass("x-date-nextday")) {
			this.value.setDate(t.parentNode.day);
			this.update(this.value);
		} else {
			this.value.setYear(t.parentNode.year);
			this.value.setMonth(t.parentNode.month);
			this.value.setDate(t.parentNode.day);
			this.update(this.value);
		}
	},
	handleDblDateClick : function(e, t) {
		e.stopEvent();
		if (!Ext.fly(t.parentNode).hasClass("x-date-prevday")
				&& !Ext.fly(t.parentNode).hasClass("x-date-nextday")) {
			this.value.setDate(t.parentNode.day);
			this.fireEvent("select", this, this.value);
		}
	},
	showPrevMonth : function(e) {
		this.value = this.value.add("mo", -1);
		this.update(this.value);
	},
	showNextMonth : function(e) {
		this.value = this.value.add("mo", 1);
		this.update(this.value);
	},
	showPrevYear : function() {
		this.value = this.value.add("y", -1);
		this.update(this.value);
	},
	showNextYear : function() {
		this.value = this.value.add("y", 1);
		this.update(this.value);
	},
	selectOK : function() {
		this.fireEvent("select", this, this.value);
	},
	createHourPicker : function() {
		if (!this.hourPicker.dom.firstChild) {
			var buf = ['<table border="0" cellspacing="0">'];
			for (var i = 0;i < 6; i++) {
				buf.push(
				'<tr><td class="x-date-mp-month"><a href="#">',
				i * 4,
				'</a></td>',
				'<td class="x-date-mp-month"><a href="#">',
				i * 4 + 1,
				'</a></td>',
				'<td class="x-date-mp-month"><a href="#">',
				i * 4 + 2,
				'</a></td>',
				'<td class="x-date-mp-month"><a href="#">',
				i * 4 + 3, '</a></td></tr>');
			}
			buf.push(
			'<tr class="x-date-mp-btns"><td colspan="4">',
			'</button><button type="button" class="x-date-mp-cancel">',
			'取消', '</button></td></tr>',
			'</table>');
			this.hourPicker.update(buf.join(''));
			this.hourPicker.on('click', this.onHourClick, this);
			this.mpHours = this.hourPicker.select('td.x-date-mp-month');
			this.mpHours.each(function(m, a, i) {
				m.dom.xhour = i;
			});
		}
	},
	showHourPicker : function() {
		this.createHourPicker();
		var size = this.el.getSize();
		this.hourPicker.setSize(size);
		this.hourPicker.child('table').setSize(size);
		this.mpHours.removeClass('x-date-mp-sel');
		this.mpHours.item(this.value.getHours()).addClass('x-date-mp-sel');
		this.hourPicker.slideIn('b', {
			duration : .2
		});
	},
	hideHourPicker : function(disableAnim) {
		if (this.hourPicker)
			if (disableAnim === true)
				this.hourPicker.hide();
			else
				this.hourPicker.slideOut('b', {
					duration : .2
				});
	},
	onHourClick : function(e, t) {
		e.stopEvent();
		var el = new Ext.Element(t), pn;
		if (el.is('button.x-date-mp-cancel')) {
			this.hideHourPicker();
		} else if (pn = el.up('td.x-date-mp-month', 2)) {
			this.mpHours.removeClass('x-date-mp-sel');
			pn.addClass('x-date-mp-sel');
			this.value.setHours(pn.dom.xhour);
			this.update(this.value);
			this.hideHourPicker();
		}
	},
	createMinutePicker : function() {
		if (!this.minutePicker.dom.firstChild) {
			var buf = ['<table border="0" cellspacing="0">'];
			for (var i = 0;i < 8; i++) {
				buf.push(
				'<tr><td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 1,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 2,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 3,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 4 > 59 ? '' : i * 8 + 4,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 5 > 59 ? '' : i * 8 + 5,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 6 > 59 ? '' : i * 8 + 6,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 7 > 59 ? '' : i * 8 + 7,
				'</a></td></tr>');
			}
			buf.push(
			'<tr class="x-date-mp-btns"><td colspan="8">',
			'</button><button type="button" class="x-date-mp-cancel">',
			'取消', '</button></td></tr>',
			'</table>');
			this.minutePicker.update(buf.join(''));
			this.minutePicker.on('click', this.onMinuteClick, this);
			this.mpMinutes = this.minutePicker.select('td.x-date-mp-month');
			this.mpMinutes.each(function(m, a, i) {
				m.dom.xminute = i;
			});
		}
	},
	showMinutePicker : function() {
		this.createMinutePicker();
		var size = this.el.getSize();
		this.minutePicker.setSize(size);
		this.minutePicker.child('table').setSize(size);
		this.mpMinutes.removeClass('x-date-mp-sel');
		this.mpMinutes.item(this.value.getMinutes()).addClass('x-date-mp-sel');
		this.minutePicker.slideIn('b', {
			duration : .2
		});
	},
	hideMinutePicker : function(disableAnim) {
		if (this.minutePicker)
			if (disableAnim === true)
				this.minutePicker.hide();
			else
				this.minutePicker.slideOut('b', {
					duration : .2
				});
	},
	onMinuteClick : function(e, t) {
		e.stopEvent();
		var el = new Ext.Element(t), pn;
		if (el.is('button.x-date-mp-cancel')) {
			this.hideMinutePicker();
		} else if (pn = el.up('td.x-date-mp-month', 2)) {
			this.mpMinutes.removeClass('x-date-mp-sel');
			pn.addClass('x-date-mp-sel');
			this.value.setMinutes(pn.dom.xminute);
			this.update(this.value);
			this.hideMinutePicker();
		}
	},
	createSecondPicker : function() {
		if (!this.secondPicker.dom.firstChild) {
			var buf = ['<table border="0" cellspacing="0">'];
			for (var i = 0;i < 8; i++) {
				buf.push(
				'<tr><td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 1,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 2,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 3,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 4 > 59 ? '' : i * 8 + 4,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 5 > 59 ? '' : i * 8 + 5,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 6 > 59 ? '' : i * 8 + 6,
				'</a></td>',
				'<td class="x-date-mp-month" style="font-size:12px"><a href="#">',
				i * 8 + 7 > 59 ? '' : i * 8 + 7,
				'</a></td></tr>');
			}
			buf.push(
			'<tr class="x-date-mp-btns"><td colspan="8">',
			'</button><button type="button" class="x-date-mp-cancel">',
			'取消', '</button></td></tr>',
			'</table>');
			this.secondPicker.update(buf.join(''));
			this.secondPicker.on('click', this.onSecondClick, this);
			this.mpSeconds = this.secondPicker.select('td.x-date-mp-month');
			this.mpSeconds.each(function(m, a, i) {
				m.dom.xsecond = i;
			});
		}
	},
	showSecondPicker : function() {
		this.createSecondPicker();
		var size = this.el.getSize();
		this.secondPicker.setSize(size);
		this.secondPicker.child('table').setSize(size);
		this.mpSeconds.removeClass('x-date-mp-sel');
		this.mpSeconds.item(this.value.getSeconds()).addClass('x-date-mp-sel');
		this.secondPicker.slideIn('b', {
			duration : .2
		});
	},
	hideSecondPicker : function(disableAnim) {
		if (this.secondPicker)
			if (disableAnim === true)
				this.secondPicker.hide();
			else
				this.secondPicker.slideOut('b', {
					duration : .2
				});
	},
	onSecondClick : function(e, t) {
		e.stopEvent();
		var el = new Ext.Element(t), pn;
		if (el.is('button.x-date-mp-cancel')) {
			this.hideSecondPicker();
		} else if (pn = el.up('td.x-date-mp-month', 2)) {
			this.mpSeconds.removeClass('x-date-mp-sel');
			pn.addClass('x-date-mp-sel');
			this.value.setSeconds(pn.dom.xsecond);
			this.update(this.value);
			this.hideSecondPicker();
		}
	},
	update : function(date) {
		var days = date.getDaysInMonth();
		var startingPos = date.getFirstDateOfMonth().getDay();
		var days = date.getDaysInMonth();
		var activeDay = date.getDate();
		var pm = date.add("mo", -1);
		var pm1 = date.add("mo", 1);
		var prevDays = pm.getDaysInMonth();
		var prevStart = prevDays - startingPos + 1;
		var cells = this.cells.elements;
		var textEls = this.textNodes;
		var i = 0;
		if (startingPos > 0) {
			for (;prevStart <= prevDays; prevStart++, i++) {
				textEls[i].innerHTML = prevStart;
				cells[i].className = "x-date-prevday";
				cells[i].year = pm.getYear();
				cells[i].month = pm.getMonth();
				cells[i].day = prevStart;
			}
		}
		for (var j = 1;j <= days; j++, i++) {
			textEls[i].innerHTML = j;
			cells[i].className = activeDay == j
					? "x-date-selected"
					: "x-date-active";
			cells[i].year = date.getYear();
			cells[i].month = date.getMonth();
			cells[i].day = j;
		}
		var extraDays = 0;
		for (;i < 42; i++) {
			extraDays += 1;
			textEls[i].innerHTML = extraDays;
			cells[i].className = "x-date-nextday";
			cells[i].year = pm1.getYear();
			cells[i].month = pm1.getMonth();
			cells[i].day = extraDays;
		}
		this.mbtn.setText(this.aryMonth[date.getMonth()] + " "
				+ date.getFullYear());
		var h = date.getHours(), m = date.getMinutes(), s = date.getSeconds();
		if (h < 10)
			h = '0' + h;
		if (m < 10)
			m = '0' + m;
		if (s < 10)
			s = '0' + s;
		this.selHour.dom.innerHTML = h;
		this.selMinute.dom.innerHTML = m;
		this.selSecond.dom.innerHTML = s;
		if (!this.internalRender) {
			var main = this.el.dom.firstChild;
			var w = main.offsetWidth;
			this.el.setWidth(w + this.el.getBorderWidth("lr"));
			Ext.fly(main).setWidth(w);
			this.internalRender = true;
			if (Ext.isOpera && !this.secondPass) {
				main.rows[0].cells[1].style.width = (w - (main.rows[0].cells[0].offsetWidth + main.rows[0].cells[2].offsetWidth))+ "px";
				this.secondPass = true;
				this.update.defer(10, this, [date]);
			}
		}
	}
});
Ext.menu.DateTimeItem = function(config) {
	Ext.menu.DateTimeItem.superclass.constructor.call(this,
			new Ext.DateTimePicker(config), config);
	this.picker = this.component;
	this.addEvents( {
		select : true
	});
	this.picker.on("render", function(picker) {
		picker.getEl().swallowEvent("click");
		picker.container.addClass("x-menu-date-item");
	});
	this.picker.on("select", this.onSelect, this);
};
Ext.extend(Ext.menu.DateTimeItem, Ext.menu.Adapter, {
	onSelect : function(picker, date) {
		this.fireEvent("select", this, date, picker);
		Ext.menu.DateTimeItem.superclass.handleClick.call(this);
	}
});
Ext.menu.DateTimeMenu = function(config) {
	Ext.menu.DateTimeMenu.superclass.constructor.call(this, config);
	this.plain = true;
	var di = new Ext.menu.DateTimeItem(config);
	this.add(di);
	this.picker = di.picker;
	this.relayEvents(di, ["select"]);
	this.on('beforeshow', function() {
		if (this.picker) {
			this.picker.hideMonthPicker(true);
			this.picker.hideHourPicker(true);
			this.picker.hideMinutePicker(true);
			this.picker.hideSecondPicker(true);
		}
	}, this);
};
Ext.extend(Ext.menu.DateTimeMenu, Ext.menu.Menu, {
	cls : 'x-date-menu'
});
*/

Ext.DateTimePicker = Ext.extend(Ext.DatePicker,{
	todayText : '确定',
	format : "Y-m-d H:i:s",
	initComponent : function() {
		this.value = this.value ? this.value : new Date();
		this.hour = this.value.getHours();
		this.minute = this.value.getMinutes();
		this.second = this.value.getSeconds();
		Ext.DateTimePicker.superclass.initComponent.call(this);
	},
	onRender : function(container, position) {
		Ext.DateTimePicker.superclass.onRender.call(this,container, position);
		Ext.DomHelper.insertBefore(
		this.el.child("td.x-date-bottom", true).parentNode,
		'<tr><td colspan="3" align="center" class="x-date-time" style="padding:2px;cursor:pointer;"><table cellspacing="0" style="font-size:15pt;color:white;background-color:#15428b;"><tr><td class="x-date-hour">'
				+ String.leftPad(this.hour, 2,'0')
				+ '</td><td>:</td><td class="x-date-minute">'
				+ String.leftPad(this.minute,2, '0')
				+ '</td><td>:</td><td class="x-date-second">'
				+ String.leftPad(this.second,2, '0')
				+ '</td></tr></table></td></tr>');
		Ext.DomHelper.insertAfter(
		this.el.down('div.x-date-mp'),
		'<div class="x-hour-mp"></div><div class="x-minute-mp"></div><div class="x-second-mp"></div>');
		this.el.child("td.x-date-hour").on('click',this.showHourPicker, this);
		this.el.child("td.x-date-minute").on('click',this.showMinutePicker, this);
		this.el.child("td.x-date-second").on('click',this.showSecondPicker, this);
		this.hourPicker = this.el.down('div.x-hour-mp');
		this.hourPicker.enableDisplayMode('block');
		this.minutePicker = this.el.down('div.x-minute-mp');
		this.minutePicker.enableDisplayMode('block');
		this.secondPicker = this.el.down('div.x-second-mp');
		this.secondPicker.enableDisplayMode('block');
	},
	showHourPicker : function(event, target) {
		if (!this.hourPicker.dom.firstChild) {
			var buf = ['<table border="0" cellspacing="0">'];
			for (var i = 0;i < 28; i++) {
				if (i % 7 == 0)
					buf.push('<tr>');
				if (i < 24)
					buf.push('<td class="x-date-mp-month" value="'+ i + '"><a href="#">',
					String.leftPad(i, 2, '0'),
					'</a></td>');
				else
					buf.push('<td><a href="#"></a></td>');
				if (i % 7 == 6)
					buf.push('</tr>');
			}
			buf.push(
			'<tr class="x-date-mp-btns"><td colspan="7"><button type="button" class="x-date-mp-cancel">',
			this.cancelText,
			'</button></td></tr></table>');
			this.hourPicker.update(buf.join(''));
			this.hourPicker.on('click', this.onHourClick, this);
			this.updateHMS(this.hour, this.hourPicker);
		}
		var size = this.el.getSize();
		this.hourPicker.setSize(size);
		this.hourPicker.child('table').setSize(size);
		this.hourPicker.slideIn('b', {
			duration : .2
		});
	},
	onHourClick : function(e, t) {
		e.stopEvent();
		var el = new Ext.Element(t), pn;
		if (pn = el.up('td.x-date-mp-month', 2)) {
			this.hour = parseInt(t.innerHTML, 10);
			this.el.child("td.x-date-hour").dom.innerHTML = t.innerHTML;
			this.updateHMS(this.hour, this.hourPicker);
			this.hourPicker.slideOut('b', {
				duration : .2
			});
		} else if (pn = el.is('button.x-date-mp-cancel')) {
			this.hourPicker.slideOut('b', {
				duration : .2
			});
		}
	},
	updateHMS : function(v, p) {
		var mps = p.select('td.x-date-mp-month').removeClass('x-date-mp-sel');
		mps.each(function(m, a, i) {
			m[parseInt(m.dom.value, 10) == v ? 'addClass' : 'removeClass']('x-date-mp-sel');
		});
	},
	showMinutePicker : function(event, target) {
		if (!this.minutePicker.dom.firstChild) {
			var buf = ['<table border="0" cellspacing="0">'];
			for (var i = 0;i < 64; i++) {
				if (i % 7 == 0)
					buf.push('<tr>');
				if (i < 60)
					buf.push('<td class="x-date-mp-month" value="'+ i + '"><a href="#">',
					String.leftPad(i, 2, '0'),
					'</a></td>');
				else
					buf.push('<td><a href="#"></a></td>');
				if (i % 7 == 6)
					buf.push('</tr>');
			}
			buf.push(
			'<tr class="x-date-mp-btns"><td colspan="7"><button type="button" class="x-date-mp-cancel">',
			this.cancelText,
			'</button></td></tr></table>');
			this.minutePicker.update(buf.join(''));
			this.minutePicker.on('click', this.onMinuteClick,this);
			this.updateHMS(this.minute, this.minutePicker);
		}
		var size = this.el.getSize();
		this.minutePicker.setSize(size);
		this.minutePicker.child('table').setSize(size);
		this.minutePicker.slideIn('b', {
			duration : .2
		});
	},
	onMinuteClick : function(e, t) {
		e.stopEvent();
		var el = new Ext.Element(t), pn;
		if (pn = el.up('td.x-date-mp-month', 2)) {
			this.minute = parseInt(t.innerHTML, 10);
			this.el.child("td.x-date-minute").dom.innerHTML = t.innerHTML;
			this.updateHMS(this.minute, this.minutePicker);
			this.minutePicker.slideOut('b', {
				duration : .2
			});
		} else if (pn = el.is('button.x-date-mp-cancel')) {
			this.minutePicker.slideOut('b', {
				duration : .2
			});
		}
	},
	showSecondPicker : function(event, target) {
		if (!this.secondPicker.dom.firstChild) {
			var buf = ['<table border="0" cellspacing="0">'];
			for (var i = 0;i < 64; i++) {
				if (i % 7 == 0)
					buf.push('<tr>');
				if (i < 60)
					buf.push('<td class="x-date-mp-month" value="'+ i + '"><a href="#">',
					String.leftPad(i, 2, '0'),
					'</a></td>');
				else
					buf.push('<td><a href="#"></a></td>');
				if (i % 7 == 6)
					buf.push('</tr>');
			}
			buf.push('<tr class="x-date-mp-btns"><td colspan="7"><button type="button" class="x-date-mp-cancel">',
			this.cancelText,
			'</button></td></tr></table>');
			this.secondPicker.update(buf.join(''));
			this.secondPicker.on('click', this.onSecondClick,this);
			this.updateHMS(this.second, this.secondPicker);
		}
		var size = this.el.getSize();
		this.secondPicker.setSize(size);
		this.secondPicker.child('table').setSize(size);
		this.secondPicker.slideIn('b', {
			duration : .2
		});
	},
	onSecondClick : function(e, t) {
		e.stopEvent();
		var el = new Ext.Element(t), pn;
		if (pn = el.up('td.x-date-mp-month', 2)) {
			this.second = parseInt(t.innerHTML, 10);
			this.el.child("td.x-date-second").dom.innerHTML = t.innerHTML;
			this.updateHMS(this.second, this.secondPicker);
			this.secondPicker.slideOut('b', {
				duration : .2
			});
		} else if (pn = el.is('button.x-date-mp-cancel')) {
			this.secondPicker.slideOut('b', {
				duration : .2
			});
		}
	},
	handleDateClick : function(e, t) {
		e.stopEvent();
		if (t.dateValue	&& !Ext.fly(t.parentNode).hasClass("x-date-disabled")) {
			this.setValue(new Date(t.dateValue));
			this.fireEvent("select", this, Date.parseDate(this.value.format('Y-m-d ')
			+ String.leftPad(this.hour, 2, '0')
			+ ':'
			+ String.leftPad(this.minute, 2, '0')
			+ ':'
			+ String.leftPad(this.second, 2, '0'), 'Y-m-d H:i:s'));
		}
	},
	selectToday : function() {
		this.fireEvent("select", this, Date.parseDate(this.value.format('Y-m-d ')
		+ String.leftPad(this.hour, 2, '0')
		+ ':'
		+ String.leftPad(this.minute, 2, '0')
		+ ':'
		+ String.leftPad(this.second, 2, '0'),'Y-m-d H:i:s'));
	}
});
Ext.menu.DateTimeMenu = function(config) {
	Ext.menu.DateTimeMenu.superclass.constructor.call(this, config);
	this.plain = true;
	var di = new Ext.menu.DateTimeItem(config);
	this.add(di);
	this.picker = di.picker;
	this.relayEvents(di, ["select"]);
	this.on('beforeshow', function() {
		if (this.picker) {
			this.picker.hideMonthPicker(true);
		}
	}, this);
};
Ext.extend(Ext.menu.DateTimeMenu, Ext.menu.Menu, {cls : 'x-date-menu'});
Ext.menu.DateTimeItem = function(config) {
	Ext.menu.DateTimeItem.superclass.constructor.call(this,new Ext.DateTimePicker(config), config);
	this.picker = this.component;
	this.addEvents('select');
	this.picker.on("render", function(picker) {
		picker.getEl().swallowEvent("click");
		picker.container.addClass("x-menu-date-item");
	});
	this.picker.on("select", this.onSelect, this);
};
Ext.extend(Ext.menu.DateTimeItem, Ext.menu.Adapter, {
	onSelect : function(picker, date) {
		this.fireEvent("select", this, date, picker);
		Ext.menu.DateItem.superclass.handleClick.call(this);
	}
});

Ext.form.DateTimeField = function(config){
	var config = config || {};
	var defConfig = {menu:new Ext.menu.DateTimeMenu()};
	Ext.applyIf(config,defConfig);
	Ext.form.DateTimeField.superclass.constructor.call(this,config);
};
Ext.extend(Ext.form.DateTimeField,Ext.form.DateField);
Ext.reg('datetime', Ext.form.DateTimeField);