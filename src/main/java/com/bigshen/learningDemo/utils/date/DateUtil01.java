/*
 * @Copyright: 2018-2019 taojiji All rights reserved.
 */
package com.bigshen.learningDemo.utils.date;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil01 {

	public static final ThreadLocal<SimpleDateFormat> timeRangeDateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		};
	};

	public static final String[] WEEK_ENUM = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	public static final ThreadLocal<SimpleDateFormat> CHS_DATE = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy年MM月dd日"));
	public static final ThreadLocal<SimpleDateFormat> SIM_DATE = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
	public static final ThreadLocal<SimpleDateFormat> SIM_TIME = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));
	public static final ThreadLocal<SimpleDateFormat> SIM_DATE_TIME = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	private static Date defaultDate = null;

	public static final String MONGO_DATE = getDateDay();

	/**
	 * 判断时间是否属于该时间段
	 * 
	 * @param nowTime
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean isBlongToAPeriod(Date nowTime, Date beginTime, Date endTime) {
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.compareTo(begin) >= 0 && date.compareTo(end) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 字符串转换成日期
	 *
	 * @param str
	 * @param sdf
	 * @return
	 */
	public static Date str2Date(String str, SimpleDateFormat sdf) {
		if (null == str || "".equals(str)) {
			return null;
		}
		Date date = null;
		try {
			date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查当前时间是否是在 时间范围内 ，时间范围的格式为 ： 08:30~11:30,13:30~17:30
	 *
	 * @param timeRanges
	 * @return
	 */
	public static boolean isWorkingHours(String timeRanges) {
		boolean workintTime = true;
		String timeStr = timeRangeDateFormat.get().format(new Date());
		if (!StringUtils.isBlank(timeRanges)) { // 设置了 工作时间段
			workintTime = false; // 将 检查结果设置为 False ， 如果当前时间是在 时间范围内，则 置为 True
			String[] timeRange = timeRanges.split(",");
			for (String tr : timeRange) {
				String[] timeGroup = tr.split("~");
				if (timeGroup.length == 2) {
					if (timeGroup[0].compareTo(timeGroup[1]) >= 0) {
						if (timeStr.compareTo(timeGroup[0]) >= 0 || timeStr.compareTo(timeGroup[1]) <= 0) {
							workintTime = true;
						}
					} else {
						if (timeStr.compareTo(timeGroup[0]) >= 0 && timeStr.compareTo(timeGroup[1]) <= 0) {
							workintTime = true;
						}
					}
				}
			}
		}
		return workintTime;
	}

	/**
	 * 获取当天剩余秒数
	 *
	 * @return
	 */
	public static long getRemainSecondsOfToday() {
		LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
	}

	/**
	 * 获取当前日期
	 */
	public static Date getDefaultDate() {
		if (defaultDate == null) {
			defaultDate = new Date();
		}
		defaultDate.setTime(System.currentTimeMillis());
		return defaultDate;
	}

	/**
	 * 字符串转日期.
	 * 
	 * @param str
	 *            String
	 * @return Date
	 * @throws ParseException
	 *             parseException
	 */
	public static Date strDate(String str) throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		date = dateFormat.parse(str);
		return date;
	}

	/**
	 * 格式 yyMMdd.
	 * 
	 * @return yyMMdd
	 */
	public static String getDate() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * 2060908 zjs 格式 yyMMdd.
	 * 
	 * @return yyMMdd
	 */
	public static String getDate(Date date) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(date);
	}

	/**
	 * 格式 yyyyMMdd.
	 * 
	 * @return yyyyMMdd
	 */
	public static String getDateDay() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * 格式 yyyyMMdd.
	 * 
	 * @return yyyyMMdd
	 */
	public static String getDateMonth() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(new Date());
	}

	/**
	 * 格式 yyyyMMddHHmmSS.
	 * 
	 * @return yyyyMMddHHmmSS
	 */
	public static String getDateSecond() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	/**
	 * 格式 yyyy-MM-dd.
	 * 
	 * @return String
	 */
	public static String getSIMDate() {
		return SIM_DATE.get().format(new Date());
	}

	/**
	 * 格式 yyyy-MM-dd.
	 * 
	 * @return String
	 */
	public static String getSimDate(Date date) {
		return SIM_DATE.get().format(date);
	}

	/**
	 * 格式 yyyy-MM-dd HH:mm:ss.
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateTime() {
		return SIM_DATE_TIME.get().format(new Date());
	}

	/**
	 * 20160909 zjs 传Date参数 格式 yyyy-MM-dd HH:mm:ss.
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateTime(Date date) {
		return SIM_DATE_TIME.get().format(date);
	}

	/**
	 * 输出自定义格式的日期字符串.
	 * 
	 * @param formatStr
	 *            String
	 * @return String
	 */
	public static String getFormatedDate(String formatStr) {
		final SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf.format(new Date());
	}

	public static String getTime() {
		return SIM_TIME.get().format(new Date());
	}

	/**
	 * Date转String.
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String getTime(Date date) {
		if (date == null) {
			return "";
		} else {
			return SIM_TIME.get().format(date);
		}
	}

	public static String getCHSDate2String() {
		return CHS_DATE.get().format(new Date());
	}

	/**
	 * yyyy年MM月dd日.
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String getCHSDate2String(Date date) {
		if (date == null) {
			return "";
		} else {
			return CHS_DATE.get().format(date);
		}
	}

	/**
	 * 时间格式.
	 * 
	 * @param date
	 *            yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String getSIMDateTime2String(Date date) {
		if (date == null) {
			return "";
		} else {
			return SIM_DATE_TIME.get().format(date);
		}
	}

	/**
	 * String转Date.
	 * 
	 * @param date
	 *            String
	 * @return Date
	 */
	public static Date parseCHSDate(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return CHS_DATE.get().parse(date);
		} catch (final ParseException ex) {
			return null;
		}
	}

	/**
	 * String转Date.
	 * 
	 * @param date
	 *            String
	 * @return Date
	 */
	public static Date parseSIMDateTime2Date(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return SIM_DATE_TIME.get().parse(date);
		} catch (final ParseException ex) {
			return null;
		}
	}

	/**
	 * String转Date.
	 * 
	 * @param date
	 *            String
	 * @return Date
	 */
	public static Date parseSIMDate2Date(String date) {
		try {
			return SIM_DATE.get().parse(date);
		} catch (final ParseException ex) {
			return null;
		}
	}

	/**
	 * Date转String.
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String getSIMDate2String(Date date) {
		if (date == null) {
			return "";
		} else {
			return SIM_DATE.get().format(date);
		}
	}

	/**
	 * String转Date.
	 * 
	 * @param date
	 *            String
	 * @return Date
	 */
	public static Date parseSIMDate(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		try {
			return SIM_DATE.get().parse(date);
		} catch (final ParseException ex) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		final long t = getDefaultDate().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		final long t = getDefaultDate().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		final long t = getDefaultDate().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 获取当前时间String.
	 * 
	 * @return String
	 */
	public static String getLongCHSDate() {
		final Calendar now = Calendar.getInstance();
		return getCHSDate2String(now.getTime()) + WEEK_ENUM[now.get(Calendar.DAY_OF_WEEK)];
	}

	/**
	 * 获取当前时间String.
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String getLongCHSDate(Date date) {
		if (date == null) {
			return "";
		} else {
			final Calendar now = Calendar.getInstance();
			now.setTime(date);
			return getCHSDate2String(now.getTime()) + WEEK_ENUM[now.get(Calendar.DAY_OF_WEEK)];
		}
	}

	/**
	 * 日期差.
	 * 
	 * @param c1
	 *            Calendar
	 * @param c2
	 *            Calendar
	 * @return long
	 */
	public static long dayDiff(Calendar c1, Calendar c2) {
		final long l1 = c1.getTimeInMillis();
		final long l2 = c2.getTimeInMillis();
		final long diff = l2 - l1;
		final long hourDiff = diff % (1000 * 60 * 60 * 24);
		long dayDiff = diff / (1000 * 60 * 60 * 24);
		if (hourDiff > 0) {
			dayDiff++;
		}
		return dayDiff;
	}

	/**
	 * 天数差
	 * 
	 * @param start
	 *            开始时间（YYYY-MM-dd）
	 * @param end
	 *            结束时间（YYYY-MM-dd）
	 * @throws ParseException
	 * @return days 天数差
	 */
	public static long dayDiff(String start, String end) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final Date fDate = sdf.parse(start);
		final Date oDate = sdf.parse(end);
		final long days = (oDate.getTime() - fDate.getTime()) / (1000 * 3600 * 24);
		return days;
	}

	/**
	 * 毫秒差.
	 * 
	 * @param c1
	 *            Calendar
	 * @param c2
	 *            Calendar
	 * @return long
	 */
	public static long milliDiff(Calendar c1, Calendar c2) {
		final long l1 = c1.getTimeInMillis();
		final long l2 = c2.getTimeInMillis();
		final long diff = l2 - l1;
		return diff;
	}

	/**
	 * 毫秒差
	 */
	public static long milliDiff(Date date1, Date date2) {
		final long l1 = date1.getTime();
		final long l2 = date2.getTime();
		final long diff = l2 - l1;
		return diff;
	}

	/**
	 * 判断日期是否在范围内.
	 * 
	 * @param calendar
	 *            Calendar
	 * @param start
	 *            Calendar
	 * @param end
	 *            Calendar
	 * @return boolean
	 */
	public static boolean calendarBetween(Calendar calendar, Calendar start, Calendar end) {
		if (calendar == null || start == null || end == null) {
			return false;
		}
		if ((calendar.after(start) || calendar.equals(start)) && (calendar.before(end) || calendar.equals(end))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置起始日期.
	 * 
	 * @param cal
	 *            Calendar
	 * @return Calendar
	 */
	public static Calendar setStartDay(Calendar cal) {
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		return cal;
	}

	/**
	 * 设置结束日期.
	 * 
	 * @param cal
	 *            Calendar
	 * @return Calendar
	 */
	public static Calendar setEndDay(Calendar cal) {
		cal.set(11, 23);
		cal.set(12, 59);
		cal.set(13, 59);
		return cal;
	}

	/**
	 * 复制年月日.
	 * 
	 * @param destCal
	 *            Calendar
	 * @param sourceCal
	 *            Calendar
	 */
	public static void copyYearMonthDay(Calendar destCal, Calendar sourceCal) {
		destCal.set(1, sourceCal.get(1));
		destCal.set(2, sourceCal.get(2));
		destCal.set(5, sourceCal.get(5));
	}

	/**
	 * 时间转12小时制时间.
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String formatEnDate(Date date) {
		final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

		return sdf.format(date).replaceAll("上午", "AM").replaceAll("下午", "PM");
	}

	/**
	 * 获取时间对应一年中的周数.
	 * 
	 * @param date
	 *            Date
	 * @param lastWeek
	 *            int
	 * @return String
	 */
	public static String getWeekMember(Date date, int lastWeek) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		final int num = calendar.get(Calendar.WEEK_OF_YEAR);
		return String.valueOf(num - 1 - lastWeek);
	}

	/**
	 * 根据n,将时间转化为之前的月份，如2016-05.
	 * 
	 * @param date
	 *            Date
	 * @param lastMonth
	 *            int
	 * @return String
	 */
	public static String getMonthMember(Date date, int lastMonth) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -lastMonth);
		date = calendar.getTime();
		return getSIMDate2String(calendar.getTime()).substring(0, 7);
	}

	/**
	 * 获取时间对应的月份，如6.
	 * 
	 * @param date
	 *            Date
	 * @param lastMonth
	 *            int
	 * @return int
	 */
	public static int getMonthMemberIndidual(Date date, int lastMonth) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -lastMonth);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 根据n,将时间转化为之前的日期，如2016-06-15.
	 * 
	 * @param date
	 *            Date
	 * @param lastMonthDay
	 *            int
	 * @return String
	 */
	public static String getDayMember(Date date, int lastMonthDay) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -lastMonthDay);
		date = calendar.getTime();
		return getSIMDate2String(date);
	}

	/**
	 * 获取时间对应的年份.
	 * 
	 * @param date
	 *            Date
	 * @param lastYearMember
	 *            int
	 * @return String
	 */
	public static String getYearMember(Date date, int lastYearMember) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -lastYearMember);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 获取时间对应的季度数.
	 * 
	 * @param date
	 *            Date
	 * @param lastQuarterMember
	 *            int
	 * @return String
	 */
	public static String getQuarterMember(Date date, int lastQuarterMember) {
		int quarter = 0;
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		final int month = calendar.get(Calendar.MONTH);
		if (month >= 1 && month <= 3) {
			quarter = 1;
		}
		if (month >= 4 && month <= 6) {
			quarter = 2;
		}
		if (month >= 7 && month <= 9) {
			quarter = 3;
		}
		if (month >= 10 && month <= 12) {
			quarter = 4;
		}
		return String.valueOf(quarter - lastQuarterMember);
	}

	/**
	 * 时间进行相加减.
	 * 
	 * @param date
	 *            Date
	 * @param dateNum
	 * @return Date
	 */
	public static Date dateOperation(Date date, int dateNum) {
		Date dt1 = null;
		if (null != date) {
			final Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.add(Calendar.DAY_OF_YEAR, dateNum);// 日期加减天数
			dt1 = rightNow.getTime();
		}
		return dt1;
	}

	// 时间转换(距离当前时间的时间差)
	public static String ChangeTime(Date one) {
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		two = new Date();
		final long time1 = one.getTime();
		final long time2 = two.getTime();
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		String result = "";
		if (day >= 365) {
			result = day / 365 + "年前";
		} else if (day >= 30) {
			result = day / 30 + "月前";
		} else if (day >= 7) {
			result = day / 7 + "周前";
		} else if (day > 0) {
			result = day + "天前";
		} else if (hour > 0) {
			result = hour + "小时前";
		} else if (min > 0) {
			result = min + "分钟前";
		} else if (sec > 0) {
			result = "刚刚";
		}
		return result;
	}

	/*
	 * 转变为当日零点 00:00:00
	 */
	public static Date changToDayOfStart(Date dt) {
		dt = parse(format(dt, "yyyy-MM-dd 00:00:00"), "yyyy-MM-dd HH:mm:ss");
		return dt;
	}

	/*
	 * 转变为当日结束 23::59:59
	 */
	public static Date changToDayOfEnd(Date dt) {
		dt = parse(format(dt, "yyyy-MM-dd 23:59:59"), "yyyy-MM-dd HH:mm:ss");
		return dt;
	}

	/*
	 * 字符串转换成时间
	 */
	public static Date parse(String date, String pattern) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/*
	 * 字符串转换成时间。格式：yyyy-MM-dd
	 */
	public static Date parse(String date) {
		try {
			final String pattern = "yyyy-MM-dd";
			final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/*
	 * 时间转换成字符串。
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/*
	 * 时间转换成字符串。格式：yyyy-MM-dd
	 */
	public static String format(Date date) {
		if (date == null) {
			return "";
		}
		final String pattern = "yyyy-MM-dd";
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/*
	 * 本周开始时间
	 */
	public static Date getWeekStartTime() {
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int d = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);

		final Date dt = parse(format(cal.getTime(), "yyyy-MM-dd 00:00:00"), "yyyy-MM-dd HH:mm:ss");
		// System.out.println(format(dt,"yyyy-MM-dd HH:mm:ss"));

		return dt;
	}

	/*
	 * 本周结束时间
	 */
	public static Date getWeekEndTime() {
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int d = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - cal.get(Calendar.DAY_OF_WEEK);
		}
		cal.add(Calendar.DAY_OF_WEEK, d);
		cal.add(Calendar.DAY_OF_WEEK, 6);

		final Date dt = parse(format(cal.getTime(), "yyyy-MM-dd 23:59:59"), "yyyy-MM-dd HH:mm:ss");
		// System.out.println(format(dt,"yyyy-MM-dd HH:mm:ss"));

		return dt;
	}

	/*
	 * 本周开始时间
	 */
	public static Date getLastWeekStartTime() {
		Date dt = getWeekStartTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DAY_OF_WEEK, -7);
		dt = cal.getTime();
		return dt;
	}

	/*
	 * 上周结束时间
	 */
	public static Date getLastWeekEndTime() {
		Date dt = getWeekEndTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DAY_OF_WEEK, -7);
		dt = cal.getTime();
		return dt;
	}

	/*
	 * 当月开始时间
	 */
	public static Date getMonthStartTime() {
		Date today = new Date();
		final Date dt = getMonthStartTime(today);
		return dt;
	}

	/*
	 * 指定日期所在月份的开始时间
	 */
	public static Date getMonthStartTime(Date date1) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		// 当月第一天
		cal.set(Calendar.DAY_OF_MONTH, 1);

		final Date dt = parse(format(cal.getTime(), "yyyy-MM-dd 00:00:00"), "yyyy-MM-dd HH:mm:ss");
		// System.out.println(format(dt,"yyyy-MM-dd HH:mm:ss"));

		return dt;

	}

	/*
	 * 当月结束时间
	 */
	public static Date getMonthEndTime() {
		Date today = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		final Date dt = parse(format(cal.getTime(), "yyyy-MM-dd 23:59:59"), "yyyy-MM-dd HH:mm:ss");
		// System.out.println(format(dt,"yyyy-MM-dd HH:mm:ss"));

		return dt;
	}

	public static Date add(Date dt, int day) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DAY_OF_MONTH, day);
		Date dt1 = rightNow.getTime();
		return dt1;
	}

	/**
	 * 获取当前小时数
	 * 
	 * @return
	 */
	public static int getCurrentHour() {
		// 获取当前小时数
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param date
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	/**
	 * 判断当前时间是否是节假日
	 * 
	 * @param str
	 *            2019-05-01~2019-05-04格式，多个之间用","隔开
	 * @return
	 */
	public static boolean isHoliday(String str) {
		boolean isHoliday = false;
		try {
			String[] arr = str.split("~");
			Date startDate = parseSIMDate2Date(arr[0]);
			Date endDate = parseSIMDate2Date(arr[1]);
			if (isBlongToAPeriod(parseSIMDate2Date(getSIMDate2String(new Date())), startDate, endDate)) {
				isHoliday = true;
				return isHoliday;
			}

		} catch (Exception e) {
			throw new RuntimeException("判断当前时间是否是节假日失败");
		}
		return isHoliday;
	}

	/**
	 * 返回当天开始时的时间戳
	 * 
	 * @return
	 */
	public static int getDayStartTimeStamp() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		Date beginOfDate = cal.getTime();
		return (int) (beginOfDate.getTime() / 1000);
	}

	/**
	 * 功能描述：返回小时
	 *
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取某个小时开始时的时间戳
	 * 
	 * @param hour
	 * @return
	 */
	public static int getTimeStampStartOfHour(int hour) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hour, 0, 0);
		Date beginOfDate = cal.getTime();
		return (int) (beginOfDate.getTime() / 1000);
	}

	/**
	 * 获取某个小时结束时的时间戳
	 * 
	 * @return
	 */
	public static int getTimeStampEndOfHour(int hour) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hour + 1, 0, 0);
		Date beginOfDate = cal.getTime();
		return (int) (beginOfDate.getTime() / 1000);
	}

	/**
	 * 获取当前小时剩余多少秒结束
	 * 
	 * @return
	 */
	public static int getRemainSecondsToNextHour() {
		int timeStampEndOfHour = getTimeStampEndOfHour(Calendar.HOUR_OF_DAY);
		return timeStampEndOfHour - (int) (System.currentTimeMillis()) / 1000;
	}

	public static Date getEndOfDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 功能描述：返回分
	 *
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	public static void main(String[] args) {
		boolean holiday = isHoliday("2019-07-01~2019-07-26");
		System.out.println(holiday);
		System.out.println(MONGO_DATE);
	}

}
