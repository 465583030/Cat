package com.zhr.cat.tools;

public class StringUtils {

	public static boolean isEmpty(String string) {
		return string == null || string.trim().length() ==0;
	}
	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}
}
