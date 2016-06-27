package com.tinyTools.tools;

import java.io.File;

import com.tinyTools.commonUtil.StringUtils;

public class FileTools {

	/**
	 * 删除指定类别的文件或目录
	 * 
	 * @param file
	 * @param fileType
	 */
	public static final void delFile(File file, String fileType) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				if (StringUtils.isBlank(fileType)
						|| fileTypeIsMatch(f.getName(), fileType)) {
					delFile(f);
				} else {
					delFile(f, fileType);
				}
			}
		}
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param file
	 */
	public static final void delFile(File file) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				delFile(f);
			}
		}
		file.delete();
	}

	/**
	 * 文件名匹配
	 * 
	 * @param fileName
	 * @param fileType
	 * @return
	 */
	private static final boolean fileTypeIsMatch(String fileName, String fileType) {
		boolean rst = false;

		if (fileType.equals(fileName)) {
			// 完全匹配
			rst = true;
		}

		// 通配符匹配
		int index = fileType.indexOf("*");
		if (index != -1) {
			if (fileName.endsWith(fileType.substring(index + 1, fileType
					.length()))) {
				rst = true;
				if (index > 0
						&& !fileName.startsWith(fileType.substring(0, index))) {
					rst = false;
				}
			}
		}

		return rst;
	}

}
