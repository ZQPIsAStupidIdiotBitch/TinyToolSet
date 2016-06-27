package com.tinyTools.tools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.tinyTools.commonUtil.LogUtils;
import com.tinyTools.entity.DownloadInfo;

public class FileDownloader implements Runnable {
	// 下载文件信息
	private DownloadInfo downloadInfo;

	// 一组开始下载位置
	private long[] startPos;
	// 一组结束下载位置
	private long[] endPos;

	// 休眠时间
	private static final int SLEEP_SECONDS = 500;
	// 子线程下载
	private DownloadFileBlockRunner[] blocks;
	// 文件长度
	private int length;
	// 是否首次下载
	private boolean first = true;
	// 是否停止下载
	private boolean stop = false;
	// 临时文件信息
	private File tempFile;

	public FileDownloader(DownloadInfo downloadInfo) {
		this.downloadInfo = downloadInfo;
		String tempPath = this.downloadInfo.getFilePath() + File.separator
				+ downloadInfo.getFileName() + ".position";
		this.tempFile = new File(tempPath);
		// 如果存在读入点位置的文件
		if (this.tempFile.exists()) {
			this.first = false;
			// 就直接读取内容
			try {
				readPosInfo();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 数组的长度就要分成多少段的数量
			this.startPos = new long[downloadInfo.getSplitter()];
			this.endPos = new long[downloadInfo.getSplitter()];
		}
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();

		// 首次下载，获取下载文件长度
		if (this.first) {
			this.length = this.getFileSize();// 获取文件长度
			if (this.length == -1) {
				LogUtils.info("file length is know!");
				this.stop = true;
			} else if (this.length == -2) {
				LogUtils.info("read file length is error!");
				this.stop = true;
			} else if (this.length > 0) {
				for (int i = 0, len = this.startPos.length; i < len; i++) {
					int size = i * (this.length / len);
					this.startPos[i] = size;

					// 设置最后一个结束点的位置
					if (i == len - 1) {
						this.endPos[i] = this.length;
					} else {
						size = (i + 1) * (this.length / len);
						this.endPos[i] = size;
					}
					LogUtils.info("start-end Position[" + i + "]: "
							+ this.startPos[i] + "-" + this.endPos[i]);
				}
			} else {
				LogUtils.info("get file length is error, download is stop!");
				this.stop = true;
			}
		}

		// 子线程开始下载
		if (!this.stop) {
			// 创建单线程下载对象数组
			this.blocks = new DownloadFileBlockRunner[this.startPos.length];

			for (int i = 0; i < this.startPos.length; i++) {
				try {
					// 创建指定个数单线程下载对象，每个线程独立完成指定块内容的下载
					this.blocks[i] = new DownloadFileBlockRunner(
							this.downloadInfo.getUrl(), this.downloadInfo
									.getFilePath()
									+ File.separator
									+ this.downloadInfo.getFileName(),
							this.startPos[i], this.endPos[i], i);
					// 启动线程，开始下载
					new Thread(this.blocks[i]).start();
					LogUtils.info("Thread: " + i + ", startPos: "
							+ this.startPos[i] + ", endPos: " + this.endPos[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 循环写入下载文件长度信息
			LogUtils.info("downloading......");
			while (!this.stop) {
				try {
					writePosInfo();
					Thread.sleep(SLEEP_SECONDS);
					this.stop = true;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < this.startPos.length; i++) {
					if (!this.blocks[i].isDownloadOver()) {
						this.stop = false;
						break;
					}
				}
			}
			LogUtils.info("Download task is finished!");

			FileTools.delFile(this.tempFile);

			long end = System.currentTimeMillis();
			int seconds = (int) ((end - start) / 1000l);
			LogUtils.info("文件下载完成！用时:" + seconds + "秒。");
			
		}
	}

	/**
	 * 将写入点数据保存在临时文件中
	 * 
	 * @throws IOException
	 */
	private void writePosInfo() throws IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(
				this.tempFile));
		dos.writeInt(this.startPos.length);
		for (int i = 0; i < this.startPos.length; i++) {
			dos.writeLong(this.blocks[i].getStartPos());
			dos.writeLong(this.blocks[i].getEndPos());
		}
		dos.close();
	}

	/**
	 * 读取写入点的位置信息
	 * 
	 * @throws IOException
	 */
	private void readPosInfo() throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(
				this.tempFile));
		int startPosLength = dis.readInt();
		this.startPos = new long[startPosLength];
		this.endPos = new long[startPosLength];
		for (int i = 0; i < startPosLength; i++) {
			this.startPos[i] = dis.readLong();
			this.endPos[i] = dis.readLong();
		}
		dis.close();
	}

	/**
	 * 获取下载文件的长度
	 * 
	 * @return
	 */
	private int getFileSize() {
		int fileLength = -1;
		try {
			URL url = new URL(this.downloadInfo.getUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			DownloadFileBlockRunner.setHeader(conn);

			int stateCode = conn.getResponseCode();
			// 判断http status是否为HTTP/1.1 206 Partial Content或者200 OK
			if (stateCode != HttpURLConnection.HTTP_OK
					&& stateCode != HttpURLConnection.HTTP_PARTIAL) {
				LogUtils.error("Error Code: " + stateCode);
				return -2;
			} else if (stateCode >= 400) {
				LogUtils.error("Error Code: " + stateCode);
				return -2;
			} else {
				// 获取长度
				fileLength = conn.getContentLength();
				LogUtils.info("File Length: " + fileLength);
			}

			DownloadFileBlockRunner.printHeader(conn);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileLength;
	}

}
