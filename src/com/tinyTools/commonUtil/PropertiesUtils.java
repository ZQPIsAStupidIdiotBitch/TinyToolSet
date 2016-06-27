package com.tinyTools.commonUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

	public static final String getValue(String fileName, String key) {
		String rst = "";

		String path = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath()
				+ fileName;

		Properties config = new Properties();
		try {
			config.load(new FileInputStream(path));
			rst = config.getProperty(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rst;
	}

}
