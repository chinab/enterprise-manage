package com.juduowang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 日期工具类 用于转换String到Date和Timestamps
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 */
public class DateUtil {
	private static Log logger = LogFactory.getLog(DateUtil.class);
	private static String defaultDatePattern = null;
	private static String timePattern = "HH:mm";

	/**
	 * 根据本地化的资源包<code>date.format</code>的定义， 获得日期默认格式(yyyy-MM-dd)
	 * 
	 * @return UI显示日期的格式
	 */
	public static String getDatePattern() {
		Locale locale = LocaleContextHolder.getLocale();
		try {
			defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale).getString("date.format");
		} catch (MissingResourceException mse) {
			defaultDatePattern = "yyyy-MM-dd";
		}

		return defaultDatePattern;
	}

	public static String getDateTimePattern() {
		return DateUtil.getDatePattern() + " HH:mm:ss.S";
	}

	/**
	 * 用于转换Oracle格式的日期转换
	 * 
	 * @param aDate
	 *            数据库格式的日期
	 * @return 格式化为UI显示的日期
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 返回当前时间:yyyy-MM-dd HH:mm
	 */
	public static String getTime(Date theTime) {
		return convertDateToString(theTime, timePattern);
	}

	/**
	 * 返回当前日期，格式为：yyyy-MM-dd
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * 日期转换为字符串
	 */
	public static final String convertDateToString(Date aDate) {
		return convertDateToString(aDate, getDatePattern());
	}

	/**
	 * 根据给定的日期/时间格式，转换日期
	 * 
	 * @param date
	 * @param format
	 *            日期格式:EEEE是星期, MM月,dd日,yyyy是年(ASCII 字符)
	 * @return
	 */
	public static String convertDateToString(Date aDate, String format) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (format == null || format.equals("")) {
			format = getDatePattern();
		}

		if (aDate == null) {
			logger.error("日期为空!");
		} else {
			df = new SimpleDateFormat(format);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 字符串转换为日期
	 */
	public static Date convertStringToDate(String strDate) throws ParseException {
		Date aDate = null;

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("格式为: " + getDatePattern());
			}

			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			logger.error("不能把 '" + strDate + "' 转换为日期，抛出异常" + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return aDate;
	}

	/**
	 * 根据给定的日期格式，转换日期
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static final Date convertStringToDate(String format, String strDate) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(format);

		if (logger.isDebugEnabled()) {
			logger.debug("转换日期： '" + strDate + "' 的格式为 '" + format + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			logger.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * 通用日期计算
	 * 
	 * @param date
	 * @param dateType
	 *            计算的是年(Calendar.YEAR)、月(Calendar.MONTH)、日(Calendar.DATE)类型
	 * @param amount
	 * @return
	 */
	public static Date calcDate(Date date, int dateType, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(dateType, amount);

		return c.getTime();
	}

	/**
	 * 对日进行加减计算
	 * 
	 * @param date
	 *            需要计算的日期
	 * @param amount
	 *            加减的数量
	 * @return 计算后时间
	 */
	public static Date calcDateByDay(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, amount);

		return c.getTime();
	}

	/**
	 * 对月进行加减计算
	 * 
	 * @param date
	 *            需要计算的日期
	 * @param amount
	 *            加减的数量
	 * @return 计算后时间
	 */
	public static Date calcDateByMonth(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, amount);

		return c.getTime();
	}

	/**
	 * 对年进行加减计算
	 * 
	 * @param date
	 *            需要计算的日期
	 * @param amount
	 *            加减的数量
	 * @return 计算后时间
	 */
	public static Date calcDateByYear(Date date, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, amount);

		return c.getTime();
	}

	/**
	 * 获得月初时间
	 * 
	 * @param date当前日期
	 * @return 月初时间
	 */
	public static Date getStartMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);

		return c.getTime();
	}

	/**
	 * 获得月末时间
	 * 
	 * @param date当前日期
	 * @return 月末时间
	 */
	public static Date getEndMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);

		return c.getTime();
	}

	/**
	 * 获得年初时间
	 * 
	 * @param date当前日期
	 * @return 年初时间
	 */
	public static Date getStartYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);

		return c.getTime();
	}

	/**
	 * 获得年末时间
	 * 
	 * @param date当前日期
	 * @return 年末时间
	 */
	public static Date getEndYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);

		return c.getTime();
	}

	/**
	 * 获得周的第一天，周的第一天为星期天
	 * @param date
	 * @return
	 */
	public static Date getStartWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int f = Calendar.SUNDAY;
		int amount = f - dayOfWeek;

		return calcDateByDay(date, amount);
	}

	/**
	 * 获得周的最后一天，周的最后一天为星期六
	 * @param date
	 * @return
	 */
	public static Date getEndWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int f = Calendar.SATURDAY;
		int amount = f - dayOfWeek;

		return calcDateByDay(date, amount);
	}

	/**
	 * 获得季的第一天，季的第一天为星期天
	 * @param date
	 * @return
	 */
	public static Date getStartSeason(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int m = c.get(Calendar.MONTH);
		if (m <= 3) {
			c.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);
		} else if (m <= 6) {
			c.set(c.get(Calendar.YEAR), Calendar.APRIL, 1);
		} else if (m <= 9) {
			c.set(c.get(Calendar.YEAR), Calendar.JULY, 1);
		} else if (m <= 12) {
			c.set(c.get(Calendar.YEAR), Calendar.OCTOBER, 1);
		}

		return c.getTime();
	}

	/**
	 * 获得季的最后一天，季的最后一天为星期六
	 * @param date
	 * @return
	 */
	public static Date getEndSeason(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int m = c.get(Calendar.MONTH) + 1;
		if (m <= 3) {
			c.set(c.get(Calendar.YEAR), Calendar.MARCH, 31);
		} else if (m <= 6) {
			c.set(c.get(Calendar.YEAR), Calendar.JUNE, 30);
		} else if (m <= 9) {
			c.set(c.get(Calendar.YEAR), Calendar.SEPTEMBER, 30);
		} else if (m <= 12) {
			c.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
		}

		return c.getTime();
	}

	/**
	 * 计算时间差
	 * 
	 * @param from
	 * @param to
	 * @param type
	 *            结果单位使用Constants中时间类型的常量定义
	 * @return
	 */
	public static long getDistance(Date from, Date to, int type) {
		Calendar calFrom = new GregorianCalendar();
		Calendar calTo = new GregorianCalendar();
		calFrom.setTime(from);
		calTo.setTime(to);
		long fromMill = calFrom.getTimeInMillis();
		long toMill = calTo.getTimeInMillis();
		if (type == Constants.YEAR) {
			int fromYear = calFrom.get(Calendar.YEAR);
			int toYear = calTo.get(Calendar.YEAR);
			return toYear - fromYear;
		} else if (type == Constants.MONTH) {
			int fromYear = calFrom.get(Calendar.YEAR);
			int toYear = calTo.get(Calendar.YEAR);
			int year = toYear - fromYear;
			int fromMonth = calFrom.get(Calendar.MONTH);
			int toMonth = calTo.get(Calendar.MONTH);
			return 12 * year + toMonth - fromMonth;
		} else if (type == Constants.DAY) {
			return (toMill - fromMill) / (3600 * 24 * 1000);
		} else if (type == Constants.HOUR) {
			return (toMill - fromMill) / (3600 * 1000);
		} else if (type == Constants.MINUTE) {
			return (toMill - fromMill) / (60 * 1000);
		} else if (type == Constants.SECOND) {
			return (toMill - fromMill) / 1000;
		} else if (type == Constants.MIllISECOND)
			return toMill - fromMill;
		else if (type == Constants.WEEK) {
			return (toMill - fromMill) / (7 * 24 * 3600 * 1000);
		} else if (type == Constants.SEASON) {
			long m = DateUtil.getDistance(from, to, Constants.MONTH);
			return m / 3;
		} else if (type == Constants.HALFYEAR) {
			long m = DateUtil.getDistance(from, to, Constants.MONTH);
			return m / 6;
		} else {//默认为天
			return (toMill - fromMill) / (3600 * 24 * 1000);
		}
	}

	/***
	 * 根据日期判断是第几季度
	 * @param date
	 * @return
	 */
	public static int getNumberSeason(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int currentMonth = cal.get(Calendar.MONTH)+ 1;
		int currentSeason = currentMonth % 3 == 0 ? currentMonth / 3 : currentMonth / 3 + 1;
		return currentSeason;
	}

	public int getDay(String strDate) {
		Calendar cal = parseDateTime(strDate);
		return cal.get(Calendar.DATE);
	}

	public int getMonth(String strDate) {
		Calendar cal = parseDateTime(strDate);

		return cal.get(Calendar.MONTH) + 1;
	}

	public int getWeekDay(String strDate) {
		Calendar cal = parseDateTime(strDate);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public String getWeekDayName(String strDate) {
		String mName[] = { "日", "一", "二", "三", "四", "五", "六" };
		int iWeek = getWeekDay(strDate);
		iWeek = iWeek - 1;
		return "星期" + mName[iWeek];
	}

	public int getYear(String strDate) {
		Calendar cal = parseDateTime(strDate);
		return cal.get(Calendar.YEAR) + 1900;
	}

	public static Calendar parseDateTime(String baseDate) {
		Calendar cal = null;
		cal = new GregorianCalendar();
		int yy = Integer.parseInt(baseDate.substring(0, 4));
		int mm = Integer.parseInt(baseDate.substring(5, 7)) - 1;
		int dd = Integer.parseInt(baseDate.substring(8, 10));
		int hh = 0;
		int mi = 0;
		int ss = 0;
		if (baseDate.length() > 12) {
			hh = Integer.parseInt(baseDate.substring(11, 13));
			mi = Integer.parseInt(baseDate.substring(14, 16));
			ss = Integer.parseInt(baseDate.substring(17, 19));
		}
		cal.set(yy, mm, dd, hh, mi, ss);
		return cal;
	}

	public int DateDiff(String strDateBegin, String strDateEnd, int iType) {
		Calendar calBegin = parseDateTime(strDateBegin);
		Calendar calEnd = parseDateTime(strDateEnd);
		long lBegin = calBegin.getTimeInMillis();
		long lEnd = calEnd.getTimeInMillis();
		int ss = (int) ((lBegin - lEnd) / 1000L);
		int min = ss / 60;
		int hour = min / 60;
		int day = hour / 24;
		if (iType == 0)
			return hour;
		if (iType == 1)
			return min;
		if (iType == 2)
			return day;
		else
			return -1;
	}

	/*****************************************
	 * @功能     判断某年是否为闰年
	 * @return  boolean
	 * @throws ParseException
	 ****************************************/
	public boolean isLeapYear(int yearNum) {
		boolean isLeep = false;
		/**判断是否为闰年，赋值给一标识符flag*/
		if ((yearNum % 4 == 0) && (yearNum % 100 != 0)) {
			isLeep = true;
		} else if (yearNum % 400 == 0) {
			isLeep = true;
		} else {
			isLeep = false;
		}
		return isLeep;
	}

	/*****************************************
	* @功能     计算当前日期某年的第几周
	* @return  interger
	* @throws ParseException
	****************************************/
	public int getWeekNumOfYear() {
		Calendar calendar = Calendar.getInstance();
		int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
		return iWeekNum;
	}

	/*****************************************
	 * @功能     计算指定日期某年的第几周
	 * @return  interger
	 * @throws ParseException
	 ****************************************/
	public int getWeekNumOfYearDay(String strDate) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = format.parse(strDate);
		calendar.setTime(curDate);
		int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
		return iWeekNum;
	}

	/*****************************************
	 * @功能     计算某年某周的开始日期
	 * @return  interger
	 * @throws ParseException
	 ****************************************/
	public String getYearWeekFirstDay(int yearNum, int weekNum) throws ParseException {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, yearNum);
		cal.set(Calendar.WEEK_OF_YEAR, weekNum);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		//分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
		String tempDay = Integer.toString(cal.get(Calendar.DATE));
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return SetDateFormat(tempDate, "yyyy-MM-dd");

	}

	/*****************************************
	 * @功能     计算某年某周的结束日期
	 * @return  interger
	 * @throws ParseException
	 ****************************************/
	public String getYearWeekEndDay(int yearNum, int weekNum) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, yearNum);
		cal.set(Calendar.WEEK_OF_YEAR, weekNum + 1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		//分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
		String tempDay = Integer.toString(cal.get(Calendar.DATE));
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return SetDateFormat(tempDate, "yyyy-MM-dd");
	}

	/*****************************************
	 * @功能     计算某年某月的开始日期
	 * @return  interger
	 * @throws ParseException
	 ****************************************/
	public String getYearMonthFirstDay(int yearNum, int monthNum) throws ParseException {

		//分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(monthNum);
		String tempDay = "1";
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return SetDateFormat(tempDate, "yyyy-MM-dd");

	}

	/*****************************************
	 * @功能     计算某年某月的结束日期
	 * @return  interger
	 * @throws ParseException
	 ****************************************/
	public String getYearMonthEndDay(int yearNum, int monthNum) throws ParseException {

		//分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(monthNum);
		String tempDay = "31";
		if (tempMonth.equals("1") || tempMonth.equals("3") || tempMonth.equals("5") || tempMonth.equals("7")
				|| tempMonth.equals("8") || tempMonth.equals("10") || tempMonth.equals("12")) {
			tempDay = "31";
		}
		if (tempMonth.equals("4") || tempMonth.equals("6") || tempMonth.equals("9") || tempMonth.equals("11")) {
			tempDay = "30";
		}
		if (tempMonth.equals("2")) {
			if (isLeapYear(yearNum)) {
				tempDay = "29";
			} else {
				tempDay = "28";
			}
		}
		//System.out.println("tempDay:" + tempDay);
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return SetDateFormat(tempDate, "yyyy-MM-dd");
	}

	/**
	 * @see     取得指定时间的给定格式()
	 * @return String
	 * @throws ParseException
	 */
	public String SetDateFormat(String myDate, String strFormat) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(sdf.parse(myDate));

		return sDate;
	}
	
	public static String getDateStr(String FormatString) {
		return getDateStr(FormatString, new Date());
	}

	public static String getDateStr(String FormatString, Date date) {
		return new SimpleDateFormat(FormatString).format(date);
	}

	/**
	 * 月份时进行加减
	 * 
	 * @param date
	 * @param size
	 * @return
	 */
	public static Date getUpOrDownMonth(Date date, int size) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		int dateDay = calendar.get(Calendar.YEAR);
		int m = month;
		int y = year;
		int d = dateDay;
		int ySize;
		int mSize;
		if (size < 0) {
			size = size * -1;
			ySize = size / 12;
			mSize = size % 12;
			if (mSize > m) {
				y = y - 1 - ySize;
				m = 12 + m - mSize;
			} else {
				y = y - ySize;
				m = m - mSize;
			}
		} else if (size > 0) {
			ySize = size / 12;
			mSize = size % 12;
			if (mSize > (11 - m)) {
				y = y + 1 + ySize;
				m = mSize + m - 12;
			} else {
				y = y + ySize;
				m = m + mSize;
			}
		} else {
			return calendar.getTime();
		}

		calendar.set(y, m, getDay(y, m, d));
		return calendar.getTime();
	}

	/**
	 * 一个月的所有日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date[] getMonthDates(String dateStr) {
		try {
			int daySize = getMonthDaySize(dateStr);
			Date date = DateUtil.convertStringToDate(dateStr);
			Date[] dates = new Date[daySize];
			for (int i = 0; i < daySize; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.DATE, i + 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				dates[i] = calendar.getTime();
			}
			return dates;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 一年有所有月未
	 * 
	 * @param year
	 * @return
	 */
	public static Date[] getEndDates(int year) {
		Date[] dates = new Date[12];
		for (int i = 0; i < 12; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, i, getDay(year, i, 31), 0, 0, 0);
			dates[i] = calendar.getTime();
		}
		return dates;
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeap(int year) {
		return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ? true
				: false;
	}

	/**
	 * 由年月日得到一个日值
	 * 
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 */
	private static int getDay(int y, int m, int d) {
		int maxD;
		switch (m + 1) {
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
			if (isLeap(y))
				maxD = 29;
			else
				maxD = 28;
			break;
		default:
			maxD = 1;
			break;
		}
		return (d > maxD) ? maxD : d;
	}

	/**
	 * 一天的开始
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 是否在同一天
	 * 
	 * @param dateOne
	 * @param dateTow
	 * @return
	 */
	public static boolean isSameDay(Date dateOne, Date dateTow) {
		Calendar calendarOne = Calendar.getInstance();
		calendarOne.setTime(dateOne);
		Calendar calendarTow = Calendar.getInstance();
		calendarTow.setTime(dateTow);
		if (calendarOne.get(Calendar.YEAR) != calendarTow.get(Calendar.YEAR)
				|| calendarOne.get(Calendar.MONTH) != calendarTow
						.get(Calendar.MONTH)
				|| calendarOne.get(Calendar.DATE) != calendarTow
						.get(Calendar.DATE)) {
			return false;
		}
		return true;
	}

	/**
	 * 是否在同一月
	 * 
	 * @param dateOne
	 * @param dateTow
	 * @return
	 */
	public static boolean isSameMonth(Date dateOne, Date dateTow) {
		Calendar calendarOne = Calendar.getInstance();
		calendarOne.setTime(dateOne);
		Calendar calendarTow = Calendar.getInstance();
		calendarTow.setTime(dateTow);
		if (calendarOne.get(Calendar.YEAR) != calendarTow.get(Calendar.YEAR)
				|| calendarOne.get(Calendar.MONTH) != calendarTow
						.get(Calendar.MONTH)) {
			return false;
		}
		return true;
	}

	/**
	 * 是否同一年
	 * 
	 * @param dateOne
	 * @param dateTow
	 * @return
	 */
	public static boolean isSameYear(Date dateOne, Date dateTow) {
		Calendar calendarOne = Calendar.getInstance();
		calendarOne.setTime(dateOne);
		Calendar calendarTow = Calendar.getInstance();
		calendarTow.setTime(dateTow);
		if (calendarOne.get(Calendar.YEAR) != calendarTow.get(Calendar.YEAR)) {
			return false;
		}
		return true;
	}

	/****
	 * 根据输入的字符串格式为（yyyy-MM-dd）查询该月的天数
	 * 
	 * @param monthString
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthDaySize(String monthString) {
		try {
			int days;
			Date date = DateUtil.convertStringToDate(monthString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			switch (month + 1) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				days = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				days = 30;
				break;
			case 2:
				if (isLeap(year))
					days = 29;
				else
					days = 28;
				break;
			default:
				days = 1;
				break;
			}
			return days;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * 日期上进行加减
	 * 
	 * @param date
	 * @param size
	 * @return
	 */
	public static Date getUpOrDownDay(Date date, int size) {
		return new Date(date.getTime() + size * 1000 * 60 * 60 * 24);
	}

	/**
	 * 由字符串得到一个日期
	 * 
	 * @param FormatString
	 * @param dateStr
	 * @return
	 */
	public static Date getDateByStr(String FormatString, String dateStr) {
		Date date = null;
		try {
			date = new SimpleDateFormat(FormatString).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
