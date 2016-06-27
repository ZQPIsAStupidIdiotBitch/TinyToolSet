package com.tinyTools.entrance;

import java.io.File;
import java.util.Scanner;

import com.tinyTools.Constant;
import com.tinyTools.commonUtil.NetUtils;
import com.tinyTools.commonUtil.StringUtils;
import com.tinyTools.tools.FileTools;
/**
 * 提供命令
 * exit			退出命令
 * echo			回复命令，简单的把输入在输出一次
 * dnld			下载命令，格式如 dnld url fileName filePath threadNum。其中url为必须项
 * cleanFile	批量删除文件命令，格式如cleanFile rootPath [fileType]
 * @author Administrator
 */
public class ToolsRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print(Constant.COMMAND_SYMBOL);

		while (true) {
			// 获取屏幕输入
			String input = scanner.nextLine();

			if (Constant.COMMAND_EXIT.equalsIgnoreCase(input)) {
				// 退出命令
				System.out.print("^_^ 亲～～～您真的要退出吗(Y/N)?");
				input = scanner.nextLine();
				if (Constant.CONFIRM_YES.equalsIgnoreCase(input)) {
					// 确认退出
					System.exit(0);
				}
			} else if (Constant.COMMAND_HELP.equalsIgnoreCase(input)) {
				// 帮助命令
				System.out.println("帮助文档说明......");
			} else {
				// 工具命令
				String[] command = input.split(Constant.COMMAND_SEPARATOR);
				if (command != null && command.length > 0) {
					toolsHandler(command, scanner);
				}
			}

			// 命令行等待符
			System.out.print(Constant.COMMAND_SYMBOL);
		}
	}

	/**
	 * 命令业务处理
	 * 
	 * @param command
	 */
	private static final void toolsHandler(String[] command, Scanner scanner) {
		String com = command[0];
		if (Constant.COMMAND_ECHO.equalsIgnoreCase(com)) {
			// 学话
			if (command.length > 1) {
				String rtStr = "";
				for (int i = 1; i < command.length; i++) {
					rtStr += (Constant.COMMAND_SEPARATOR + command[i]);
				}

				System.out.println("repeat:" + rtStr);
			}
		} else if (Constant.COMMAND_CLEAN_FILE.equalsIgnoreCase(com)) {
			// 清除文件
			if (command.length == 1 || command.length > 3) {
				System.out.println("抱歉您输入的命令格式不正确，请输入形如	"
						+ Constant.COMMAND_CLEAN_FILE
						+ Constant.COMMAND_SEPARATOR + "rootPath"
						+ Constant.COMMAND_SEPARATOR
						+ "[fileType] 的命令，其中文件类型可为空");
			} else {
				String rootPath = command[1];
				String fileType = "";
				if (command.length == 3) {
					fileType = command[2];
				}
				try {
					File file = new File(rootPath);
					FileTools.delFile(file, fileType);
					System.out.println("文件删除成功!");
				} catch (Exception e) {
					System.err.println("文件删除异常，原因:" + e.getCause() + "\r\n"
							+ e.getMessage());
				}
			}
		} else if (Constant.COMMAND_DOWNLOAD.equalsIgnoreCase(com)) {
			// 文件下载
			// 屏幕输入
			String input = "";

			String url = null;
			String fileName = null;
			String filePath = null;
			String threadNum = null;
			int splitter = 0;
			if (command.length == 1) {
				System.out.print(Constant.COMMAND_SYMBOL + "请输入下载地址:");
				input = scanner.nextLine();
				url = input;

				System.out.print(Constant.COMMAND_SYMBOL + "请输入保存文件名:");
				input = scanner.nextLine();
				fileName = input;

				System.out.print(Constant.COMMAND_SYMBOL + "请输入保存地址:");
				input = scanner.nextLine();
				filePath = input;

				System.out.print(Constant.COMMAND_SYMBOL + "请输入并发数:");
				input = scanner.nextLine();
				threadNum = input;
			} else {
				if (command.length == 2) {
					url = command[1];
				} else if (command.length == 3) {
					url = command[1];
					threadNum = command[2];
				} else if (command.length == 5) {
					url = command[1];
					fileName = command[2];
					filePath = command[3];
					threadNum = command[4];
				}
			}

			// 分块
			splitter = (StringUtils.isNotBlank(threadNum) && StringUtils
					.isNumeric(threadNum)) ? Integer.parseInt(threadNum) : 1;
			
			NetUtils.download(url, fileName, filePath, splitter);
			
		} else if (Constant.COMMAND_ECHO.equalsIgnoreCase(com)) { // TODO
		} else if (!"".equalsIgnoreCase(com)) {
			System.out.println("抱歉，本宝宝没有提供您输入的命令功能，或者您的输入有误，请仔细检查后重新输入。");
		}
	}
}
