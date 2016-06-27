package com.tinyTools.commonUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static final boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static final boolean isNotEmpty(String str) {
		return str != null && str.length() > 0;
	}

	public static final boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static final boolean isNotBlank(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 判断字符串都是整数
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
