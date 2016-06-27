package com.tinyTools.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.tinyTools.commonUtil.LogUtils;

public class DownloadFileBlockRunner implements Runnable {
	/** 缓冲大小 */
	private static final int BUFF_LENGTH = 1024 * 8;

	// 下载文件url
	private String url;
	// 下载文件起始位置
	private long startPos;
	// 下载文件结束位置
	private long endPos;
	// 线程id
	private int threadId;

	// 下载是否完成
	private boolean isDownloadOver = false;

	private FileBlockSaver block;

	public DownloadFileBlockRunner(String url, String name, long startPos,
			long endPos, int threadId) throws IOException {
		super();
		this.url = url;
		this.startPos = startPos;
		this.endPos = endPos;
		this.threadId = threadId;
		// 分块下载写入文件内容
		this.block = new FileBlockSaver(name, startPos);
	}

	@Override
	public void run() {
		while (this.endPos > this.startPos && !this.isDownloadOver) {
			URL url;
			try {
				url = new URL(this.url);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();

				// 设置连接超时时间为10s
				conn.setConnectTimeout(10000);
				// 设置读取数据超时时间为10s
				// conn.setReadTimeout(10000);

				setHeader(conn);

				String property = "bytes=" + this.startPos + "-";
				conn.setRequestProperty("RANGE", property);

				// 输出log信息
				LogUtils.info("开始 [" + this.threadId + "]:" + property
						+ this.endPos);

				// 获取文件输入流，读取文件内容
				InputStream is = conn.getInputStream();

				byte[] buff = new byte[BUFF_LENGTH];
				int length = -1;
				LogUtils.info("#start#Thread: " + this.threadId
						+ ", startPos: " + this.startPos + ", endPos: "
						+ this.endPos);

				while ((length = is.read(buff)) > 0
						&& this.startPos < this.endPos && !this.isDownloadOver) {
					// 写入文件内容，返回最后写入的长度
					this.startPos += this.block.write(buff, 0, length);
				}

				LogUtils.info("#over#Thread: " + this.threadId + ", startPos: "
						+ this.startPos + ", endPos: " + this.endPos);
				LogUtils.info("Thread " + this.threadId + " is execute over!");
				this.isDownloadOver = true;
                
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
                    if (this.block != null) {
                    	this.block.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
		}
		
		if (this.endPos <= this.startPos && !this.isDownloadOver) {
			LogUtils.info("Thread " + threadId + ", not need download file !");
			this.isDownloadOver = true;
		}
	}

	/**
	 * 打印下载文件头部信息
	 * 
	 * @param conn
	 */
	public static void printHeader(URLConnection conn) {
		int i = 1;
		while (true) {
			String headerKey = conn.getHeaderFieldKey(i);
			i++;
			if (headerKey != null) {
				LogUtils.info(headerKey + ":" + conn.getHeaderField(i));
			} else {
				break;
			}
		}
	}

	/**
	 * 设置头信息
	 * 
	 * @param conn
	 */
	public static void setHeader(URLConnection conn) {
		conn
				.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		conn
				.setRequestProperty("Accept-Language",
						"en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "utf-8");
		conn.setRequestProperty("Accept-Charset",
				"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("connnection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since",
				"Fri, 02 Jan 2009 17:00:05 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-conntrol", "max-age=0");
		conn.setRequestProperty("Referer", "http://www.baidu.com");
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getStartPos() {
		return startPos;
	}

	public void setStartPos(long startPos) {
		this.startPos = startPos;
	}

	public long getEndPos() {
		return endPos;
	}

	public void setEndPos(long endPos) {
		this.endPos = endPos;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public boolean isDownloadOver() {
		return isDownloadOver;
	}

	public void setDownloadOver(boolean isDownloadOver) {
		this.isDownloadOver = isDownloadOver;
	}

	public FileBlockSaver getItemFile() {
		return block;
	}

	public void setItemFile(FileBlockSaver itemFile) {
		this.block = itemFile;
	}

}
