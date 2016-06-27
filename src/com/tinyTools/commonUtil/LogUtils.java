package com.tinyTools.commonUtil;

public class LogUtils {
	public static final void info(String msg) {
		System.out.print("\r\n>" + msg);
	}

	public static final void error(String msg) {
		System.err.print("\r\n>" + msg);
	}
}
