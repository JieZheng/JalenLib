/**   
 * 用一句话描述该文件做什么.
 * @title DateUtil.java
 * @package com.sinsoft.android.util
 * @author   
 * @update 2014-6-26 上午9:57:56  
 */
package com.jalenz.jalenlib.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期操作工具类.
 * 
 * @author
 */

public class DateUtil {

	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;
	private static final long ONE_DAY = 86400000L;
	private static final long ONE_WEEK = 604800000L;

	private static final String ONE_SECOND_AGO = "秒前";
	private static final String ONE_MINUTE_AGO = "分钟前";
	private static final String ONE_HOUR_AGO = "小时前";
	private static final String ONE_DAY_AGO = "天前";
	private static final String ONE_MONTH_AGO = "月前";
	private static final String ONE_YEAR_AGO = "年前";

	private final SimpleDateFormat longHourSdf;
	private final SimpleDateFormat longSdf;
	private SimpleDateFormat shortSdf;

	public DateUtil() {
		this.shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		this.longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
		this.longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	public static Calendar str2Calendar(String str) {
		return str2Calendar(str, null);

	}

	public static Calendar str2Calendar(String str, String format) {

		Date date = str2Date(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;

	}

	public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
		return date2Str(c, null);
	}

	public static String date2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}

	public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
		return date2Str(d, null);
	}

	public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(d);
		return s;
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND);
	}

	/**
	 * 获得当前日期的字符串格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		return date2Str(c, format);
	}

	// 格式到秒
	public static String getMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

	}

	// 格式到天
	public static String getDay(long time) {

		return new SimpleDateFormat("yyyy-MM-dd").format(time);

	}

	// 格式到毫秒
	public static String getSMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

	}

    public static String getTime(long timestamp) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp * 1000));
        return date;
    }
    public static String getTime1(long timestamp) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timestamp * 1000));
        return date;
    }
    
    public static String getTime1(String timestamp) {
        return getTime1(StringUtil.StringToInt(timestamp));
    }

    public static String getWeekDay(long timeStamp) {
        String weekDay = new SimpleDateFormat("E").format(new Date(timeStamp * 1000));
        return weekDay;
    }
    
	
	public static String imformat(Date date) {
		long delta = new Date().getTime() - date.getTime();
		int i = 0;
		if (delta < 1L * ONE_MINUTE) {
			long seconds = toSeconds(delta);
			i = 1;
			if (i == 0) {
				return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
			}

		}
		if (delta < 45L * ONE_MINUTE) {
			long minutes = toMinutes(delta);
			return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
		}
		if (delta < 24L * ONE_HOUR) {
			long hours = toHours(delta);
			return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
		}
		if (delta < 48L * ONE_HOUR) {
			return "昨天";
		}
		if (delta < 30L * ONE_DAY) {
			long days = toDays(delta);
			return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
		}
		if (delta < 12L * 4L * ONE_WEEK) {
			long months = toMonths(delta);
			return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
		} else {
			long years = toYears(delta);
			return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
		}
	}

	private static long toSeconds(long date) {
		return date / 1000L;
	}

	private static long toMinutes(long date) {
		return toSeconds(date) / 60L;
	}

	private static long toHours(long date) {
		return toMinutes(date) / 60L;
	}

	private static long toDays(long date) {
		return toHours(date) / 24L;
	}

	private static long toMonths(long date) {
		return toDays(date) / 30L;
	}

	private static long toYears(long date) {
		return toMonths(date) / 365L;
	}

	public String getImFormatDate(String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 HH:mm");
		return sdf2.format(d);
	}

	public static String getImFormatDatBy(String time, String p) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat(p);
		return sdf2.format(d);
	}

	// 获取当月开始和结束时间
	public static String getFirstday_Lastday_Month() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		// 当前月的最后一天
		cal.set(Calendar.DATE, 1);
		cal.roll(Calendar.DATE, -1);
		Date endTime = cal.getTime();
		String endTime1 = datef.format(endTime);
		// 当前月的第一天
		cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
		Date beginTime = cal.getTime();
		String beginTime1 = datef.format(beginTime);
		return beginTime1 + "," + endTime1;

	}

	/**
	 * 某一个月第一天和最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Map<String, String> getFirstday_Lastday_Month(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		Date theDate = calendar.getTime();

		// 上个月第一天
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first).append(
				" 00:00:00");
		day_first = str.toString();

		// 上个月最后一天
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last).append(
				" 23:59:59");
		day_last = endStr.toString();

		Map<String, String> map = new HashMap<String, String>();
		map.put("first", day_first);
		map.put("last", day_last);
		return map;
	}

	// 通过季度和年拼接上传的季度年
	public static String quarter(int quarter, int years) {
		String startq = null;
		switch (quarter) {
		case 1:
			startq = years + "-01-01," + years + "-03-31";
			break;
		case 2:
			startq = years + "-04-01," + years + "-06-30";
			break;
		case 3:
			startq = years + "-07-01," + years + "-09-30";
			break;
		case 4:
			startq = years + "-10-01," + years + "-12-31";
			break;

		default:
			break;
		}
		return startq;
	}

	/**
	 * @Title: currDuarter
	 * @Description: 获取当前季度
	 * @author xiangjin.tian@sotao.com
	 * @date 2014-10-29 下午1:47:00
	 * @return String 返回类型
	 * @throws
	 */
	public static int currDuarter() {
		String m = DateUtil.date2Str(new Date(), "MM");
		if (m.equals("01") || m.equals("02") || m.equals("03")) {
			return 1;
		} else if (m.equals("04") || m.equals("05") || m.equals("06")) {
			return 2;
		} else if (m.equals("07") || m.equals("08") || m.equals("09")) {
			return 3;
		} else if (m.equals("10") || m.equals("11") || m.equals("12")) {
			return 4;
		}
		return 1;
	}

	// 获取当前的开始和结束时间
	public static String getDatestartAndEnd() {

		return DateUtil.date2Str(new Date(), "yyy-MM-dd") + " 00:00:00,"
				+ DateUtil.date2Str(new Date(), "yyy-MM-dd") + " 23:59:59";
	}

	public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
	
	public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }
	
	public int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
	
	// 获取当前周的开始和结束
	public String getCurrentWeekDayStartend() {

		return getCurrentWeekDayStartTime() + "," + getCurrentWeekDayEndTime();
	}

	/**
	 * 获得本周的第一天，周一
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	public String getCurrentWeekDayStartTime() {
		Calendar c = Calendar.getInstance();
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
			c.add(Calendar.DATE, -weekday);
			c.setTime(longSdf.parse(shortSdf.format(c.getTime())));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.date2Str(c.getTime());
	}

	/**
	 * 获得本周的最后一天，周日
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	public String getCurrentWeekDayEndTime() {
		Calendar c = Calendar.getInstance();
		try {
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			c.add(Calendar.DATE, 8 - weekday);
			c.setTime(longSdf.parse(shortSdf.format(c.getTime())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.date2Str(c.getTime());
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	// 获取前一周的开始和结束时间
	public String getqweek() {

		return new DateUtil().getMondayPlus() + ","
				+ new DateUtil().getCurrentMonday();
	}

	// 用来全局控制 上一周，本周，下一周的周数变化
	private static int weeks = 0;

	// 获得当前日期与本周一相差的天数
	private int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return -6;
		} else {
			return 2 - dayOfWeek;
		}
	}

	// 获得上周星期一的日期
	public String getPreviousMonday() {
		weeks--;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期日的日期
	public String getCurrentMonday() {
		weeks = 0;
		int mondayPlus = this.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus - 1);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * @Title: getDateOfLastMonth
	 * @Description: 获取
	 * @author xiangjin.tian@sotao.com
	 * @date 2014-10-29 下午2:41:39
	 * @return String 返回类型
	 * @throws
	 */
	public static String getDateOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		calendar.add(Calendar.MONTH, -1); // 得到前一个月
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return date2Str(str2Date(year + "-" + month + "-" + day, "yyyy-MM-dd"),
				"yyyy-MM-dd");
	}

	/**
	 * @Title: getDateOfLastYearTime
	 * @Description: 获取最近半年时间
	 * @author xiangjin.tian@sotao.com
	 * @date 2014-10-29 下午2:57:51
	 * @return String 返回类型
	 * @throws
	 */
	public static String getDateOfLastYearTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		calendar.add(Calendar.MONTH, -6); // 得到前一个月
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return date2Str(str2Date(year + "-" + month + "-" + day, "yyyy-MM-dd"),
				"yyyy-MM-dd");
	}

	/**
	 * @Title: getDateOfLastYearTime
	 * @Description: 获取前一年的前一天时间
	 * @author xiangjin.tian@sotao.com
	 * @date 2014-10-29 下午2:57:51
	 * @return String 返回类型
	 * @throws
	 */
	public static String getDateOfLastYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1); // 得到前一年
		calendar.add(Calendar.DATE, -1); // 得到前一天
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return date2Str(str2Date(year + "-" + month + "-" + day, "yyyy-MM-dd"),
				"yyyy-MM-dd");
	}

	/**
	 * 
	 * @Title: getCurWeekBeginDate
	 * @Description: 获取当前周第一天
	 * @author xiangjin.tian@sotao.com
	 * @date 2014-10-29 下午3:42:08
	 * @return String 返回类型
	 * @throws
	 */
	public static String getCurWeekBeginDate() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return date2Str((Date) currentDate.getTime().clone(), "yyyy-MM-dd");
	}

	/**
	 * @Title: getCurWeekEndDate
	 * @Description: 获取当前周最后一天
	 * @author xiangjin.tian@sotao.com
	 * @date 2014-10-29 下午3:43:54
	 * @return String 返回类型
	 * @throws
	 */
	public static String getCurWeekEndDate() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return date2Str((Date) currentDate.getTime().clone(), "yyyy-MM-dd");
	}

	/**
	 * 获得本月的开始时间，即2012-01-01 00:00:00
	 * 
	 * @return
	 */
	public static String getCurrentMonthStartTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			now = new DateUtil().shortSdf.parse(new DateUtil().shortSdf
					.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date2Str(now, "yyyy-MM-dd");
	}

	/**
	 * 当前月的结束时间，即2012-01-31 23:59:59
	 * 
	 * @return
	 */
	public static String getCurrentMonthEndTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			now = new DateUtil().longSdf.parse(new DateUtil().shortSdf.format(c
					.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date2Str(now, "yyyy-MM-dd");
	}

	/**
	 * 返回指定年度的所有周。List中包含的是String[2]对象<br>
	 * string[0]本周的开始日期,string[1]是本周的结束日期。<br>
	 * 日期的格式为yyyy-MM-dd。<br>
	 * 每年的第一个周，必须包含星期一且是完整的七天。<br>
	 * 例如：2009年的第一个周开始日期为2009-01-05，结束日期为2009-01-11。 <br>
	 * 星期一在哪一年，那么包含这个星期的周就是哪一年的周。<br>
	 * 例如：2008-12-29是星期一，2009-01-04是星期日，哪么这个周就是2008年度的最后一个周。<br>
	 * 
	 * @param year
	 *            格式 yyyy ，必须大于1900年度 小于9999年
	 * @return
	 */
	public static List<String[]> getWeeksByYear(final int year) {
//		if (year < 1900 || year > 9999) {
//			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
//		}
		// //首先计算当年有多少个周,每年都至少有52个周，个别年度有53个周

		int weeks = getWeekNumByYear(year);
		// System.out.println(year+"共有"+weeks+"个周");
		List<String[]> result = new ArrayList<String[]>(weeks);
		for (int i = 1; i <= weeks; i++) {
			String[] tempWeek = new String[2];
			tempWeek[0] = getYearWeekFirstDay(year, i);
			tempWeek[1] = getYearWeekEndDay(year, i);
			result.add(tempWeek);
		}
		return result;
	}

	/**
	 * 计算指定年度共有多少个周。
	 * 
	 * @param year
	 *            格式 yyyy ，必须大于1900年度 小于9999年
	 * @return
	 */
	public static int getWeekNumByYear(final int year) {
//		if (year < 1900 || year > 9999) {
//			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
//		}
		int result = 52;// 每年至少有52个周 ，最多有53个周。
		String date = getYearWeekFirstDay(year, 53);
		if (date.substring(0, 4).equals(year + "")) { // 判断年度是否相符，如果相符说明有53个周。
			result = 53;
		}
		return result;
	}

	/**
	 * 计算某年某周的开始日期
	 * 
	 * @param yearNum
	 *            格式 yyyy ，必须大于1900年度 小于9999年
	 * @param weekNum
	 *            1到52或者53
	 * @return 日期，格式为yyyy-MM-dd
	 */
	public static String getYearWeekFirstDay(int yearNum, int weekNum) {
//		if (yearNum < 1900 || yearNum > 9999) {
//			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
//		}
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
		// 上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
		cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
		cal.set(Calendar.YEAR, yearNum);
		cal.set(Calendar.WEEK_OF_YEAR, weekNum);

		// 分别取得当前日期的年、月、日
		return date2Str(cal.getTime(),"yyyy-MM-dd");
	}

	/**
	 * 计算某年某周的结束日期
	 * 
	 * @param yearNum
	 *            格式 yyyy ，必须大于1900年度 小于9999年
	 * @param weekNum
	 *            1到52或者53
	 * @return 日期，格式为yyyy-MM-dd
	 */
	public static String getYearWeekEndDay(int yearNum, int weekNum) {
//		if (yearNum < 1900 || yearNum > 9999) {
//			throw new NullPointerException("年度必须大于等于1900年小于等于9999年");
//		}
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 每周从周一开始
		// 上面两句代码配合，才能实现，每年度的第一个周，是包含第一个星期一的那个周。
		cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
		cal.set(Calendar.YEAR, yearNum);
		cal.set(Calendar.WEEK_OF_YEAR, weekNum);
		return date2Str(cal.getTime(),"yyyy-MM-dd");
	}
	/**
	 * @Title: getCuurWeek
	 * @Description: 获取当前是第几周
	 * @author xiangjin.tian@sotao.com  
	 * @date 2014-11-11 上午11:20:03
	 * @return int  返回类型
	 * @throws
	 */
	public static int getCuurWeek(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	   
    /**
     * @Title: timestamp
     * @Description: 获取时间戳 （秒级）
     * @author xiangjin.tian@sotao.com  
     * @date 2015-3-23 下午12:32:10
     * @param date
     * @return long  返回类型
     * @throws
     */
    public static long getTimestamp(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String timestamp = String.valueOf(cal.getTimeInMillis());
        return Long.valueOf(timestamp.substring(0, timestamp.length()-3));
    }

    /**
     * @Title: timestamp
     * @Description: 获取时间戳 （毫秒级）
     * @author xiangjin.tian@sotao.com  
     * @date 2015-3-23 下午12:32:10
     * @param date
     * @return long  返回类型
     * @throws
     */
    public static long getTimestampMillis(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String timestamp = String.valueOf(cal.getTimeInMillis());
        return Long.valueOf(timestamp);
    }
    
    /**
     * @Title: gethalfOfyear
     * @Description: 获取前三个月时间
     * @author xiangjin.tian@sotao.com  
     * @date 2015-3-23 下午12:41:37
     * @return String  返回类型
     * @throws
     */
    public static Date getThreeMonthsTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); // 得到前一天
        calendar.add(Calendar.MONTH, -3); // 得到前一个月
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        return str2Date(year + "-" + month + "-" + day, "yyyy-MM-dd");
    }
    
    /**
     * 字符串转换到时间格式
     * @param dateStr 需要转换的字符串
     * @return dateFormatStr 需要转换的字符串的时间格式
     * @param formatStr 需要格式的目标字符串  举例 yyyyMMdd
     * @return String 返回转换后的时间字符串
     * @throws ParseException 转换异常
     */
    public static String reformatDateString(String dateStr,String dateFormatStr,String formatStr){
        String resultStr = "";
        DateFormat sdf=new SimpleDateFormat(dateFormatStr);
        Date date=null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        try {
            SimpleDateFormat s=new SimpleDateFormat(formatStr);
            resultStr = s.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return resultStr;
    }

}
