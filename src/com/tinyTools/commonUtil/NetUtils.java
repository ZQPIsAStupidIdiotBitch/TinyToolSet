package com.tinyTools.commonUtil;

import com.tinyTools.entity.DownloadInfo;
import com.tinyTools.tools.FileDownloader;

public class NetUtils {

	public static final void download(String url) {
		DownloadInfo bean = new DownloadInfo(url);
		LogUtils.info(bean.toString());
		new Thread(new FileDownloader(bean)).start();
	}

	public static final void download(String url, int threadNum) {
		DownloadInfo bean = new DownloadInfo(url, threadNum);
		LogUtils.info(bean.toString());
		new Thread(new FileDownloader(bean)).start();
	}

	public static final void download(String url, String fileName,
			String filePath, int threadNum) {
		DownloadInfo bean = new DownloadInfo(url, fileName, filePath, threadNum);
		LogUtils.info(bean.toString());
		new Thread(new FileDownloader(bean)).start();
	}
}
