package com.zhr.cat.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	private static String defaultDateFormatString;

	private static ThreadLocal<SimpleDateFormat> defaultDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>();

	private static SimpleDateFormat getDefaultDateFormat() {
		SimpleDateFormat df = defaultDateFormatThreadLocal.get();
		if (df == null) {
			df = new SimpleDateFormat(defaultDateFormatString, Locale.CHINA);
			defaultDateFormatThreadLocal.set(df);
		}
		return df;
	}

	static {
		defaultDateFormatString = "yyyy-MM-dd";
	}

	/**
	 * 将字符串根据指定格式转换为日期
	 *
	 * @param dateString
	 *            日期字符串
	 * @param format
	 *            指定的格式
	 * @return
	 */
	public static Date toDate(String dateString, String format) {
		try {
			if (format == null || defaultDateFormatString.equals(format)) {
				return getDefaultDateFormat().parse(dateString);
			} else {
				return new SimpleDateFormat(format, Locale.CHINA).parse(dateString);
			}
		} catch (ParseException e) {
			throw new ClassCastException("无法将字符串" + dateString + "转换为Date类型");
		}
	}

	/**
	 * 将字符串根据指定格式转换为日期
	 *
	 * @param dateString
	 * @param format
	 * @param nvl
	 *            转换失败时的返回值
	 * @return
	 */
	public static Date toDate(String dateString, String format, Date nvl) {
		try {
			if (format == null || defaultDateFormatString.equals(format)) {
				return getDefaultDateFormat().parse(dateString);
			} else {
				return new SimpleDateFormat(format, Locale.CHINA).parse(dateString);
			}
		} catch (ParseException e) {
			return nvl;
		}
	}

	/**
	 * 将日期根据指定格式转换为字符串
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String toString(Date date, String format) {
		if (format == null || defaultDateFormatString.equals(format)) {
			return getDefaultDateFormat().format(date);
		} else {
			return new SimpleDateFormat(format, Locale.CHINA).format(date);
		}
	}

	/**
	 * 将日期根据指定格式转换为字符串
	 *
	 * @param date
	 * @param format
	 * @param nvl
	 *            转换失败时的返回值
	 * @return
	 */
	public static String toString(Date date, String format, String nvl) {
		try {
			return toString(date, format);
		} catch (Exception err) {
			return nvl;
		}
	}

	/**
	 * 将日期字符串转换为指定格式
	 * 
	 * @param dateStr
	 * @param strformat
	 * @param targetFormat
	 * @return
	 */
	public static String dateFormat(String dateStr, String strformat, String targetFormat) {
		Date date = toDate(dateStr, strformat);
		String formatDate = DateUtil.toString(date, targetFormat);
		return formatDate;
	}
}
