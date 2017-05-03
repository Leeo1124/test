package com.leeo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class DateUtils {

	/** 默认时区的日历 */
	private static final Calendar calendar = Calendar.getInstance();
	/** 当前日期 */
	private static final Date date = calendar.getTime();
	/** 全部时区名字 */
	private static final List<String> timeZoneIds = Arrays.asList(TimeZone.getAvailableIDs());
	/** 自定义时区缓存 */
	private static final Map<String,Object> timeZoneCache = new HashMap<String,Object>();
	/** 默认的日期格式 */
	private static final DateFormat DEFAULT_DATE = DateFormat.DATE_LA;
	/** 默认的日期时间格式 */
	private static final DateFormat DEFAULT_DATE_TIME = DateFormat.DATE_TIME_LA;
	/** 默认的时间格式 */
	private static final DateFormat DEFAULT_TIME = DateFormat.TIME_LA;

	public enum DateFormat {
		DEFAULT_YEAR("yyyy"),DEFAULT_MONTH("MM"),DEFAULT_DAY("dd"),
		DATE_LA("yyyy-MM-dd"),DATE_TIME_LA("yyyy-MM-dd HH:mm:ss"),
		TIME_LA("HH:mm:ss"),DATE_TIME_LA_MS("yyyy-MM-dd HH:mm:ss.SSS"),
		DATE_SL("yyyy/MM/dd"),DATE_TIME_SL("yyyy/MM/dd HH:mm:ss"),DATE_TIME_SL_MS("yyyy/MM/dd HH:mm:ss.SSS"),
		DATE_EMP("yyyyMMdd"),DATE_TIME_EMP("yyyyMMdd HH:mm:ss"),
		DATE_CN("yyyy年MM月dd日"),DATE_TIME_CN("yyyy年MM月dd日 HH时mm分ss秒"),TIME_CN("HH时mm分ss秒"),
		DATE_EN("MM/dd/yyyy"),DATE_TIME_EN("MM/dd/yyyy HH:mm:ss"),
		DATE_SHROT("yyMMdd"),DATE_TIME_EMP_M("yyyyMMddHHmm"),
		DATA_TIME_EMP_S("yyyyMMddHHmmss"),DATA_TIME_EMP_MS("yyyyMMddHHmmssSSS"),
		DATE_PERIOD_TIME("y-M-d H-m-s"),DATE_PERIOD_YMD("y-M-d"),DATE_PERIOD_HMS("H-m-s");
		
		private String name;

		private DateFormat(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
	
	private DateUtils() {
	}
	
	/**
	 * 获取年份
	 * 
	 * @return
	 */
	public static int getYear() {
		return getYear(date);
	}
	
	/**
	 * 获取年份
	 * @param date 日期
	 * @return
	 */
	public static int getYear(Date date) {
		return getYear(date,null);
	}

	/**
	 * 获取年份
	 * 
	 * @param date
	 *            日期
	 * @param zone
	 *            时区
	 * @return
	 */
	public static int getYear(Date date, TimeZone zone) {
		return get(date, zone, Calendar.YEAR);
	}
	
	/**
	 * 获取月份
	 * 
	 * @return
	 */
	public static int getMonth() {
		return getMonth(date);
	}

	/**
	 * 获取月份
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static int getMonth(Date date) {
		return getMonth(date, null);
	}

	/**
	 * 获取月份
	 * 
	 * @param date
	 *            日期
	 * @param zone
	 *            时区
	 * @return
	 */
	public static int getMonth(Date date, TimeZone zone) {
		return get(date, zone, Calendar.MONTH) + 1;
	}
	
	/**
	 * 获取日
	 * 
	 * @return
	 */
	public static int getDay() {
		return getDay(date);
	}

	/**
	 * 获取日
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static int getDay(Date date) {
		return getDay(date, null);
	}

	/**
	 * 获取日
	 * 
	 * @param date
	 *            日期
	 * @param zone
	 *            时区
	 * @return
	 */
	public static int getDay(Date date, TimeZone zone) {
		return get(date, zone, Calendar.DATE);
	}
	
	/**
	 * 获取星期
	 * 
	 * @return
	 */
	public static int getWeek() {
		return getWeek(date);
	}

	/**
	 * 获取星期
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static int getWeek(Date date) {
		return getWeek(date, null);
	}

	/**
	 * 获取星期
	 * 
	 * @param date
	 *            日期
	 * @param zone
	 *            时区
	 * @return
	 */
	public static int getWeek(Date date, TimeZone zone) {
		return get(date, zone, Calendar.DAY_OF_WEEK) - 1;
	}
	
	/**
	 * 获取日期中的字段值，如年、月、日、星期等。
	 * 
	 * @param date
	 *            日期
	 * @param zone
	 *            时区
	 * @param field
	 *            日期中的字段，如年、月、日、星期等。
	 * @return
	 */
	public static int get(Date date, TimeZone zone, int field) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (null != zone) {
			c.setTimeZone(zone);
		}

		return c.get(field);
	}
	
	
	/**
	 * 转换日期毫秒数为缺省日期格式字符串
	 * @param date 毫秒数
	 * @return
	 */
	public static String Date2String(long date) {
		return Date2String(date, DEFAULT_DATE_TIME, null);
	}
	
	/**
	 * 转换日期毫秒数为缺省日期格式字符串
	 * @param date 毫秒数
	 * @param zone 时区
	 * @return
	 */
	public static String Date2String(long date, TimeZone zone) {
		return Date2String(date, DEFAULT_DATE_TIME, zone);
	}
	
	/**
	 * 转换日期毫秒数为指定格式字符串
	 * @param date 毫秒数
	 * @param format 日期格式
	 * @return
	 */
	public static String Date2String(long date, DateFormat format) {
		return Date2String(date, format, null);
	}
	
	/**
	 * 转换日期毫秒数为指定格式字符串 
	 * @param date 毫秒数
	 * @param format 日期格式
	 * @param zone 时区
	 * @return
	 */
	public static String Date2String(long date, DateFormat format, TimeZone zone) {
		return Date2String(new Date(date), format, zone);
	}
	
	/**
	 * 获取缺省格式的日期
	 * 
	 * @return
	 */
	public static String getDefaultDate() {
		return Date2String(date, DEFAULT_DATE, null);
	}

	/**
	 * 获取缺省格式的时间
	 * 
	 * @return
	 */
	public static String getDefaultTime() {
		return Date2String(date, DEFAULT_TIME, null);
	}

	/**
	 * 获取缺省格式的日期时间
	 * 
	 * @return
	 */
	public static String getDefaultDateTime() {
		return Date2String(date, DEFAULT_DATE_TIME, null);
	}
	
	/**
	 * 转换日期为指定格式字符串
	 * @param format 日期格式
	 * @return
	 */
	public static String Date2String(DateFormat format) {
		return Date2String(date, format);
	}
	
	/**
	 * 转换日期为指定格式字符串
	 * @param date 日期
	 * @param format 日期格式
	 * @return
	 */
	public static String Date2String(Date date, DateFormat format) {
		return Date2String(date, format,null);
	}

	/**
	 * 转换日期为指定格式字符串
	 * @param format 日期格式
	 * @param zone 时区
	 * @return
	 */
	public static String Date2String(DateFormat format, TimeZone zone) {
		return Date2String(date, format, zone);
	}
	
	/**
	 * 转换日期为指定格式字符串
	 * @param date 日期
	 * @param format 日期格式
	 * @param zone 时区
	 * @return
	 */
	public static String Date2String(Date date, DateFormat format, TimeZone zone) {
		return Date2String(date, format.getName(), zone);
	}
	
	/**
	 * 转换日期为指定格式字符串
	 * @param date 日期
	 * @param format 日期格式 
	 * @param zone 时区
	 * @return
	 */
	public static String Date2String(Date date, String format, TimeZone zone) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (null != zone) {
			sdf.setTimeZone(zone);
		}

		return sdf.format(date);
	}
	
	/**
	 * 转换指定格式字符串为日期
	 * 
	 * @param str
	 *            日期字符串
	 * @return
	 */
	public static Date string2Date(String str) {
		return string2Date(str, null);
	}

	/**
	 * 转换指定格式字符串为日期
	 * 
	 * @param str
	 *            日期字符串
	 * @param timeZone
	 *            时区
	 * @return
	 */
	public static Date string2Date(String str, TimeZone zone) {

		if (str.length() == 6) {
			return string2Date(str, DateFormat.DATE_SHROT, zone);
		}
		if (str.length() == 8) {
			return string2Date(str, DateFormat.DATE_EMP, zone);
		}
		if (str.length() == 10) {
			if (str.indexOf("-") != -1) {
				return string2Date(str, DateFormat.DATE_LA, zone);
			}

			if (str.indexOf("/") != -1) {
				return string2Date(str, DateFormat.DATE_SL, zone);
			}
		}
		if (str.length() == 12) {
			return string2Date(str, DateFormat.DATE_TIME_EMP_M, zone);
		}
		if (str.length() == 14) {
			return string2Date(str, DateFormat.DATA_TIME_EMP_S, zone);
		}
		if (str.length() == 17) {
			return string2Date(str, DateFormat.DATA_TIME_EMP_MS, zone);
		}
		if (str.length() == 19) {
			if (str.indexOf("-") != -1) {
				return string2Date(str, DateFormat.DATE_TIME_LA, zone);
			}
			if (str.indexOf("/") != -1) {
				return string2Date(str, DateFormat.DATE_TIME_EN, zone);
			}
		}
		if (str.length() == 23) {
			if (str.indexOf("-") != -1) {
				return string2Date(str, DateFormat.DATE_TIME_LA_MS, zone);
			}
			if (str.indexOf("/") != -1) {
				return string2Date(str, DateFormat.DATE_TIME_SL_MS, zone);
			}
		}

		return null;
	}

	/**
	 * 转换指定格式字符串为日期
	 * 
	 * @param str
	 *            日期字符串
	 * @param format
	 *            日期格式
	 * @param timeZone
	 *            时区
	 * @return
	 */
	public static Date string2Date(String str, DateFormat format, TimeZone zone) {
		if (str == null) {
			return null;
		}
		SimpleDateFormat fmt = new SimpleDateFormat(format.getName());
		if (zone != null) {
			fmt.setTimeZone(zone);
		}
		Date date = null;
		try {
			date = fmt.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * 根据时区名字取得时区 如果非java已知标准名字，则必须为 GMT[+-]hh:mm 格式
	 * 
	 * @param id
	 */
	public static TimeZone getTimeZone(String id) {
		if (id == null)
			return null;
		if (timeZoneIds.contains(id))
			return TimeZone.getTimeZone(id);
		if (timeZoneCache.containsKey(id))
			return (TimeZone) timeZoneCache.get(id);
		Pattern p = Pattern.compile("^GMT[+-](0[0-9]|1[01]):([0-5][0-9])$");
		Matcher m = p.matcher("id");
		if (!m.matches())
			return null;
		int hh = Integer.parseInt(id.substring(4, 6));
		int mm = Integer.parseInt(id.substring(7));
		int sign = (id.charAt(3) == '-' ? -1 : 1);
		TimeZone tz = new SimpleTimeZone((hh * 60 + mm) * 60000 * sign, id);
		timeZoneCache.put(id, tz);
		
		return tz;
	}
	
	/**
	 * 判断输入的年份是否闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取当前月份月初，格式为：xxxx-yy-zz (eg: 2007-01-01)
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthBegin(Date date) {
		int y = getYear(date);
		int m = Integer.valueOf(getMonth(date));
		String strM = null;
		strM = m >= 10 ? String.valueOf(m) : ("0" + m);
		
		return y + "-" + strM + "-01";
	}
 
	/**
	 * 获取当前月份月底，格式为：xxxx-yy-zz (eg: 2007-12-31)
	 * 
	 * @param date
	 * @return
	 */
	public static String getMonthEnd(Date date) {
		int y = Integer.valueOf(getYear(date));
		int m = Integer.valueOf(getMonth(date));
		String strM = null;
		int days = getMonthDays(y, m);
		strM = m >= 10 ? String.valueOf(m) : ("0" + m);

		return y + "-" + strM + "-" + days;
	}
	
	/**
	 * 获取指定时间段内的天数
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public static long getDays(String beginDate, String endDate) {
		long days = 0;
		if (StringUtils.isNotBlank(beginDate)
				&& StringUtils.isNotBlank(beginDate)) {
			Date begin = string2Date(beginDate);
			Date end = string2Date(endDate);
			days = (end.getTime() - begin.getTime()) / (24 * 3600 * 1000);
		}

		return days;
	}
	
	/**
	 * 获取指定时间段内的所有日期
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public static List<String> getAllDate(String beginDate, String endDate) {
		List<String> dateList = new ArrayList<String>();
		Date begin = string2Date(beginDate);
		long day = getDays(beginDate, endDate);
		for (int i = 0; i <= day; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(begin);
			c.add(Calendar.DATE, i);
			dateList.add(Date2String(c.getTime(), DateFormat.DATE_EMP));
		}

		return dateList;
	}
	
	/**
	 * 比较两个时间先后
	 * 
	 * @param date1
	 *            时间1
	 * @param date2
	 *            时间2
	 * @return 如果时间1等于时间2，返回0，如果时间1小于时间2，返回负值，如果时间1大于时间2，返回正值
	 */
	public static int compare(String date1, String date2) {
		return string2Date(date1).compareTo(string2Date(date2));
	}

	/**
	 * 比较两个时间先后
	 * 
	 * @param date1
	 *            时间1
	 * @param date2
	 *            时间2
	 * @return 如果时间1等于时间2，返回0，如果时间1小于时间2，返回负值，如果时间1大于时间2，返回正值
	 */
	public static int compare(Date date1, Date date2) {
		return date1.compareTo(date2);
	}
	
	/**
	 * 获取指定年指定月份的天数
	 * 
	 * @param y
	 *            年
	 * @param m
	 *            月
	 * @return
	 */
	public static int getMonthDays(int y, int m) {
		int days = 0;
		if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10
				|| m == 12) {
			days = 31;
		}
		if (m == 4 || m == 6 || m == 9 || m == 11) {
			days = 30;
		}
		if (m == 2) {
			boolean leap = isLeapYear(y);
			if (leap) {
				days = 29;
			} else {
				days = 28;
			}
		}

		return days;
	}
	
	/**
	 * 获取指定年份的天数
	 * 
	 * @param year
	 * @return
	 */
	public static int getYearDays(int year) {
		int days = 0;
		for (int i = 1; i <= 12; i++) {
			days += getMonthDays(year, i);
		}

		return days;
	}
	
	/**
	 * 获取指定时间段内的时间差，返回格式：y-M-d(年-月-日)
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 */
	public static String getDatePeriodToYmd(String beginDate, String endDate){
		return getDatePeriod(beginDate,endDate, DateFormat.DATE_PERIOD_YMD.getName());
	}
	
	/**
	 * 获取指定时间段内的时间差，返回格式：H-m-s(时-分-秒)
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public static String getDatePeriodToHms(String beginDate, String endDate) {
		return getDatePeriod(beginDate, endDate, DateFormat.DATE_PERIOD_HMS
				.getName());
	}

	/**
	 * 获取指定时间段内的时间差，返回格式：y-M-d H-m-s(年-月-日 时-分-秒)
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public static String getDatePeriod(String beginDate, String endDate) {
		return getDatePeriod(beginDate, endDate, DateFormat.DATE_PERIOD_TIME
				.getName());
	}

	/**
	 * 获取指定时间段内的时间差
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public static String getDatePeriod(String beginDate, String endDate,
			String format) {
		return DurationFormatUtils.formatPeriod(string2Date(beginDate)
				.getTime(), string2Date(endDate).getTime(), format);
	}
	
	public static void main(String[] args) {
//		String str = Date2String(DateFormat.DEFAULT_TIME,getTimeZone("Asia/Shanghai"));
//		System.out.println(str);
		
//		String d = getDate(TimeZone.getTimeZone("GMT-8"));
//		System.out.println(d);
		
//		List<String> dateList = getAllDate("2014-10-01","2014-10-10");
//		System.out.println(dateList);
//		
//		System.out.println(getDays("2014-10-01","2014-10-10"));
		
//		System.out.println(getWeek());
		
//		System.out.println(getMonthEnd(new Date()));
		
//		System.out.println(getYearDays(2001));
		
//		System.out.println(getDatePeriod("2013-10-25","2014-09-10"));
		
		System.out.println(getYear());
		System.out.println(getMonth());
		System.out.println(getDay());
		System.out.println(getWeek());
		
	}

}
