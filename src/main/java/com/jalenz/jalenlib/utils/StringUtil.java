package com.jalenz.jalenlib.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil
{

    //Convert String to Int
    public static int StringToInt(String str) {
        int i = 0;
        if (str == null || str.trim().length() == 0) {
            str = "0";
        }
        try {
            i = Integer.valueOf(str);
        } catch (Exception e) {
            i = 0;
            e.printStackTrace();
        }
        return i;
    }   

	/**
	 * 时间处理
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeDay(String time)
	{
		if (TextUtils.isEmpty(time) || !time.contains("T"))
		{
			return time;
		}
		return time.substring(0, time.indexOf("T"));
	}
	/**
	 * 只取月份和日期
	 * 
	 * @param time
	 * @return
	 */
	public static String getDay(String time)
	{
		if (TextUtils.isEmpty(time) || !time.contains("T"))
		{
			return time;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			Date date = df.parse(time);
			return new SimpleDateFormat("MM-dd").format(date);
		}
		catch (ParseException e)
		{
			return null;
		}

		//		return time.substring(5, time.indexOf("T"));

	}
	
	/**
	 * 只取月份和日期 .时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getDayTime(String time)
	{
		if (TextUtils.isEmpty(time) || !time.contains("T"))
		{
			return time;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			Date date = df.parse(time);
			return new SimpleDateFormat("MM-dd HH:mm").format(date);
		}
		catch (ParseException e)
		{
			return null;
		}

		//		return time.substring(5, time.indexOf("T"));

	}
	
	/**
	 * 只取时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getTime(String time)
	{
		if (TextUtils.isEmpty(time) || !time.contains("T"))
		{
			return time;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			Date date = df.parse(time);
			return new SimpleDateFormat("HH:mm").format(date);
		}
		catch (ParseException e)
		{
			return null;
		}

		//		return time.substring(5, time.indexOf("T"));

	}
	/**
	 * 只取年份
	 * 
	 * @param time
	 * @return
	 */
	public static String getYear(String time)
	{
		if (TextUtils.isEmpty(time) || !time.contains("T"))
		{
			return time;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			Date date = df.parse(time);
			return new SimpleDateFormat("yyyy").format(date);
		}
		catch (ParseException e)
		{
			return null;
		}

		//		return time.substring(5, time.indexOf("T"));

	}

	/**
	 * 等到精确到分的时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeMinute(String time)
	{
		if (TextUtils.isEmpty(time))
		{
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			Date date = df.parse(time);
			return new SimpleDateFormat("yyyy.MM.dd    HH:mm").format(date);
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	/**
	 * 将json时间字符串转换为Date
	 * 
	 * @param input
	 * @return
	 */
	public static Date JsonTimeToDate(String input)
	{

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (input.contains("T"))
			input = input.replace('T', ' ');
		if (input.contains("Z"))
			input = input.replace("Z", "+0000");
		try
		{
			Date date = df.parse(input);
			return date;
		}
		catch (ParseException e)
		{
			Log.e("parse error", "JsonTimeToDate转换时间错误：" + input);
			return null;
		}
	}

	/**
	 * 得到剩余秒数
	 * 
	 * @param time
	 * @return
	 */
	public static long getRemainTime(String time)
	{
		Date date = JsonTimeToDate(time);
		long currentTime = System.currentTimeMillis();
		long endTime = date.getTime();
		long remainTime = (endTime - currentTime) / 1000;
		if (remainTime < 0)
		{
			remainTime = 0;
		}
		return remainTime;
	}
	/**
	 * 根据服务器时间得到剩余秒数
	 * 
	 * @param currentTimeStr
	 * @return
	 */
	public static long getRemainTime(String currentTimeStr, String endTimeStr)
	{
		Date currentDate = JsonTimeToDate(currentTimeStr);
		Date endDate = JsonTimeToDate(endTimeStr);
		long currentTime = currentDate.getTime();
		long endTime = endDate.getTime();
		long remainTime = (endTime - currentTime) / 1000;
		if (remainTime < 0)
		{
			remainTime = 0;
		}
		return remainTime;
	}

	/**
	 * 获取相对时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getAbsolutelyTime(String time)
	{
		Date date = JsonTimeToDate(time);
		long currentTime = System.currentTimeMillis();
		long publishTime = date.getTime();
		long lastTime = (currentTime - publishTime) / 1000;
		if (lastTime >= 60)
		{
			int year = (int) (lastTime / (12 * 30 * 24 * 3600));
			if (year != 0)
			{
				return year + "年前";
			}
			int month = (int) (lastTime / (30 * 24 * 3600));
			if (month != 0)
			{
				return month + "月前";
			}
			int day = (int) (lastTime / (24 * 3600));
			if (day != 0)
			{
				return day + "天前";
			}
			int hour = (int) (lastTime / 3600);
			if (hour != 0)
			{
				return hour + "小时前";
			}
			int minute = (int) (lastTime / 60);
			if (minute != 0)
			{
				return minute + "分钟前";
			}
		}
		return "刚刚";
	}

	
	
	/**
	 *messaegeTime
	 * 传入日期
	 * 根据条件返回
	 * 当天  时：分
	 * 今年  月- 时：分
	 * 往年 年-月-日 时：分
	 * @param time
	 * @return
	 */
	public static String messaegeTime(String time)
	{
		Date date = JsonTimeToDate(time);
		long currentTime = System.currentTimeMillis();//获取当前时间
		long publishTime = date.getTime();//返回的数据
		long lastTime = (currentTime - publishTime) / 1000;
		
		if (lastTime >60*60*24){
//			int year = (int) (lastTime / (12 * 30 * 24 * 3600));
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			String oldyear=getYear(time);
			String newyaer=year+"";
			if (!oldyear.equals(newyaer))
			{
				return getTimeMinute(time);
			}else{
				return getDayTime(time);
			}
			
		}
		else{
			return getTime(time);	
		}
		
	}

    /**
     * 判断手机号码
     */
    public static boolean isMobileNO(String mobiles)
    {
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[6-8])|(14[5,7]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobiles);
        return matcher.matches();
    }
	
	/**
	 * 判断手机号码
	 */
	public static boolean isMobileNO11d(String mobiles)
	{
		Pattern pattern = Pattern.compile("^\\d{11}$");
		Matcher matcher = pattern.matcher(mobiles);
		return matcher.matches();
	}

	/**
	 * 判断是否是邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email)
	{
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * 判断网址
	 */
	public static boolean isURL(String url)
	{

		Pattern httpPtn = Pattern.compile("(http://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");
		return httpPtn.matcher(url).matches();
	}

	   /**
     * 字符串是否为空
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)
    {
        if (str == null || str.length() == 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * EditText 是否为空
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text != null && text.length() > 0) {
            return false;
        }
        return true;
    }
	
	/**
	 * 列表是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean isEmptyList(List<T> list)
	{
		if (list == null || list.size() == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 转换为两位数字
	 * 
	 * @param num
	 * @return
	 */
	public static String toTwoNum(long num)
	{
		DecimalFormat df = new DecimalFormat("00");
		return df.format(num);
	}

	/**
	 * 保留两位小数
	 * 
	 * @param num
	 * @return
	 */
	public static String toTwoDec(Object num)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(num);
	}

	/**
	 * 保留一位小数
	 * 
	 * @param num
	 * @return
	 */
	public static String toOneDec(Object num)
	{
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(num);
	}

	/**
	 * 得到城市短名称
	 * 
	 * @param cityname
	 * @return
	 */
	public static String getShortCityName(String cityname)
	{
		if (TextUtils.isEmpty(cityname))
		{
			return cityname;
		}
		else
		{
			return cityname.replace("市", "").replace("地区", "").replace("自治州", "").replace("自治区", "");
		}
	}
	
	/**
	 * 去重复
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static List removeDeuplicate(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element)){
                newList.add(element);
            }
        }
        return newList;
    }

	
	   /**
     * 提供精确的小数位四舍五入处理。
     * 
     * @param v 需要四舍五入的数字
     * @return 四舍五入后的结果
     */

    public static String round(Double v) {
    	DecimalFormat   fnum  =   new  DecimalFormat("##0.00");    
  	  String   dd=fnum.format(v);      
        return dd;
    }
    
    
	
}
