package com.hengzhang.springboot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil 工具类
 * @author zhangh
 * @date 2018年8月27日下午6:13:20
 */
public class DateUtil {
	/**
	 * 获取当前时间的long
	 *
	 * @return
	 */
	public static long getTodayLong() {
		Date date = new Date();
		return date.getTime();
	}

	/**
	 * 获取当前时间的long
	 *
	 * @return
	 */
	public static long getTodayLong(String type) {
		Date date = new Date();
		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd");
		matter.format(date);
		return date.getTime();
	}

	/**
	 * 按YYYY-MM-DD HH:MM:SS格式返回今天的日期.
	 *
	 * @return String
	 */
	public static String getToday() {
		return getStringDay(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 按YYYY-MM-DD格式返回今天的日期.
	 *
	 * @return String
	 */
	public static String getToday2() {
		return getStringDay(new Date(), "yyyy-MM-dd");
	}

	/**
     * 将字符串日期转换为type格式的日期类型
     * @param dateTimeStr
     * @param type 日期格式
     * @return
     * 2014年11月10日
     */
    public static Date getDateByStringOnType(String dateTimeStr, String type) {
    	Date date = null;
    	SimpleDateFormat ft = new SimpleDateFormat(type);
    	try {
			date = ft.parse(dateTimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return date;
    }

    /**
     * 将字符串日期转换为type格式的日期类型
     * @param dateTimeStr
     * @return
     * 
     */
    public static Date getDateByStringOnType(String dateTimeStr) {
    	Date date = null;
    	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
    		date = ft.parse(dateTimeStr);
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	return date;
    }

	/**
	 * Date转String
	 *
	 * @return String
	 */
	public static String getStringDay(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 规定日期格式
		return formatter.format(date); // 将Date转换为符合格式的String
	}

	/**
	 * 按指定格式返回日期.
	 *
	 * @return String
	 */
	public static String getStringDay(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format); // 规定日期格式
		return formatter.format(date); // 将Date转换为符合格式的String
	}

	/**
	 * 按指定格式返回日期.
	 *
	 * @return Date
	 */
	public static Date getDateDay(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format); // 规定日期格式
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 按指定格式返回日期.
	 *
	 * @return Date
	 */
	public static Date getDateDay(Long date) {
		return new Date(date);
	}

	/**
     * 根据秒数，格式化输出时间（mm:ss/HH:mm:ss）
     *
     * @param seconds
     * @return
     */
    public static String secondsToTime(int seconds) {
        String showTime = "";
        int second = seconds % 60;
        int minute = seconds / 60;
        int hour = 0;
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        if (hour != 0) {
            if (hour < 10) {
                showTime += "0" + hour + ":";
            } else {
                showTime += hour + ":";
            }
        }
        if (minute < 10) {
            showTime += "0" + minute + ":";
        } else {
            showTime += minute + ":";
        }
        if (second < 10) {
            showTime += "0" + second;
        } else {
            showTime += second;
        }

        return showTime;
    }

    /**
     * 增加\减少seconds秒后的日期
     *
     * @param endDate
     * @return
     */
    public static Date getAddSecondDay(Date date, int seconds) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

	/**
	 * 计算给定的两个时间相差的秒数:end - start
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getDiffNum(String start, String end) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d_start = ft.parse(start);
			Date d_end = ft.parse(end);
			return getDiffNum(d_start, d_end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	/**
	 * 计算给定的两个时间相差的秒数:end - start
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getDiffNum(Date start, Date end) {
		long quot = 0;
		quot = end.getTime() - start.getTime();
		quot = quot / 1000;
		return Math.abs(quot);
	}

	/**
	 * java.util.Date --> java.time.LocalDate
	 * @param date
	 * @return
	 */
	public static LocalDate dateToLocalDate(Date date) {
	    Instant instant = date.toInstant();
	    ZoneId zone = ZoneId.systemDefault();
	    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
	    return localDateTime.toLocalDate();
	}

	/**
	 * java.time.LocalDate --> java.util.Date
	 * @param localDate
	 * @return
	 */
	public static Date localDateToDate(LocalDate localDate){
		ZoneId defaultZoneId = ZoneId.systemDefault();
		return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
	}

	/**
	 * 
	 * @see 对日期时间进行加减运算操作
	 * @param date
	 * @param type
	 *            y = year m = month d = date h = hour m = minute s = second
	 * @param num
	 *            正数加法 负数减法
	 * @return
	 */
	public static Date getDateSub(Date date, String type, int num) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		if (type.equals("y")) {
			calender.add(Calendar.YEAR, num);
		}
		if (type.equals("M")) {
			calender.add(Calendar.MONTH, num);
		}
		if (type.equals("d")) {
			calender.add(Calendar.DATE, num);
		}
		if (type.equals("h")) {
			calender.add(Calendar.HOUR, num);
		}
		if (type.equals("m")) {
			calender.add(Calendar.MINUTE, num);
		}
		if (type.equals("s")) {
			calender.add(Calendar.SECOND, num);
		}
		return calender.getTime();
	}

	/**
	 * 
	 * @see 对日期时间进行加减运算操作
	 * @param date
	 * @param type
	 *            y = year m = month d = date h = hour m = minute s = second
	 * @param num
	 *            正数加法 负数减法
	 * @param resultFormat
	 *            结果日期的格式
	 * @return
	 */
	public static String getDateSub(String date, String type, int num, String resultFormat) {
		Date day = getDateByStringOnType(date);
		return getStringDay(getDateSub(day, type, num), resultFormat);
	}

	/**比较两个日期的大小
	 * @param date1
	 * @param date2
	 * @return 0 相等 + 大于 - 小于
	 */
	public static int compareDate(String date1, String date2) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(ft.parse(date1));
			c2.setTime(ft.parse(date2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c1.compareTo(c2);
	}

	/**
	 * 返回指定的日期格式
	 *
	 * @param type
	 * @return
	 */
	public static String getDayFormat(String dateStr, String type) {
		SimpleDateFormat formatter = new SimpleDateFormat(type); // 规定日期格式
		Date date = getDateByString(dateStr); // 将符合格式的String转换为Date
		String today = formatter.format(date); // 将Date转换为符合格式的String
		return today;
	}

	public static Date getDateByString(String dateTimeStr) {
		Date date = null;
		SimpleDateFormat ft = null;
		if(dateTimeStr.indexOf(" ") > -1){
			ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else{
			ft = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			date = ft.parse(dateTimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;

	}
}
