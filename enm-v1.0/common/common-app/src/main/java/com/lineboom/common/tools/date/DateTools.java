package com.lineboom.common.tools.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTools {
	public static String getDateStr(String FormatString){
		return new SimpleDateFormat(FormatString).format(new Date());
	}
}
