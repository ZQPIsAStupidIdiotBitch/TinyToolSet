package com.tinyTools.tools;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileBlockSaver {
	// 存储文件
	private RandomAccessFile fileBlock;
	
	/**
	 * 默认构造方法
	 * 
	 * @throws IOException
	 */
	public FileBlockSaver() throws IOException {
		this("", 0);
	}
	
	/**
	 * 
	 * @param name	文件路径、名称
	 * @param pos	写入点位置 position
	 * @throws IOException
	 */
	public FileBlockSaver(String name, long pos) throws IOException {
		this.fileBlock = new RandomAccessFile(name, "rw");
		// 在指定的pos位置开始写入数据
		this.fileBlock.seek(pos);
	}
	
	/**
	 * 同步方法写入文件
	 * 
	 * @param buff
	 * @param start
	 * @param length
	 * @return
	 */
	public synchronized int write(byte[] buff, int start, int length) {
		int i = -1;
		try {
			this.fileBlock.write(buff, start, length);
			i = length;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}
	
	/**
	 * 关闭文件
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
        if (this.fileBlock != null) {
            this.fileBlock.close();
        }
    }
}
