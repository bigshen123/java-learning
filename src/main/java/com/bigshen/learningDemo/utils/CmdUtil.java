package com.bigshen.learningDemo.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令工具
 * @author Admin
 *
 */
public class CmdUtil {

	public static final Log logger = LogFactory.getLog(CmdUtil.class);
	
	private static final BigDecimal BYTE = new BigDecimal(1024);
	
	/**
	 * 获取操作系统类型
	 * @return
	 */
	public static String getOS() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("windows") >= 0) {
			return "windows";
		}
		if (os.indexOf("linux") >= 0) {
			return "linux";
		}
		return null;
	}
	
	/**
	 * 执行命令
	 * @param cmd 命令
	 * @throws Exception 
	 */
	public static void executeCmd(String cmd) throws Exception {
		logger.info("执行指令："+cmd);
		if (cmd == null || cmd.isEmpty()) {
			throw new Exception("执行命令失败，命令为空。");
		}
		
		String os = getOS();
		if ("linux".equals(os)) {
			executeLinuxCmd(cmd);
			return;
		}
		if ("windows".equals(os)) {
			executeWindowsCmd(cmd);
			return;
		}
	}
	
	/**
	 * 执行命令，并返回命令行输出结果
	 * @param cmd 命令
	 * @return 命令行输出结果
	 * @throws Exception 
	 */
	public static List<String> executeCmdWithResult(String cmd) throws Exception {
		String os = getOS();
		if ("linux".equals(os)) {
			return executeLinuxCmd(cmd);
		}
		if ("windows".equals(os)) {
			return executeWindowsCmdWithResult(cmd);
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 获取内存大小
	 * 单位：GB
	 * @throws Exception 
	 */
	public static double getMemoryTotal() {
		try {
			if ("linux".equals(getOS())) {
				return getLinuxMemory();
			}
			if ("windows".equals(getOS())) {
				return getWindowsMemory();
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("获取内存大小失败，异常信息：" + e.getMessage(), e);
			}
		}
		return 0.00;
	}
	
	/**
	 * 获取磁盘大小，单位：GB
	 * @throws Exception 
	 */
	public static double getDiskTotal() {
		try {
			if ("linux".equals(getOS())) {
				return getLinuxDisk();
			}
			if ("windows".equals(getOS())) {
				return getWindowsDisk();
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("获取磁盘大小失败，异常信息：" + e.getMessage(), e);
			}
		}
		return 0.00;
	}
	
	/**
	 * 获取Linux内存大小，单位：GB
	 * @return
	 * @throws Exception 
	 */
	private static double getLinuxMemory() throws Exception {
		BigDecimal memorySize = new BigDecimal(0.00);
		
		String cmd = "cat /proc/meminfo";
		List<String> result = executeCmdWithResult(cmd);
		for(String str : result) {
			if (str.contains("MemTotal")) {
				str = str.replaceAll("\\D", "");
				memorySize = new BigDecimal(str).divide(BYTE).divide(BYTE, 2, BigDecimal.ROUND_HALF_UP);
				break;
			}
		}
		return memorySize.doubleValue();
	}
	
	/**
	 * 获取Windows内存大小，单位：GB
	 * @return
	 * @throws Exception 
	 */
	private static double getWindowsMemory() throws Exception {
		BigDecimal memorySize = new BigDecimal(0.00);
		
		String cmd = "wmic path win32_physicalmemory get capacity";
		List<String> result = executeCmdWithResult(cmd);
		for (String str : result) {
			str = str.replace(" ", "");
			if (str.matches("^[\\d]+")) {
				memorySize = new BigDecimal(str).divide(BYTE).divide(BYTE)
						.divide(BYTE, 2, BigDecimal.ROUND_HALF_UP);
			}
		}
		return memorySize.doubleValue();
	}
	
	/**
	 * 获取Linux磁盘大小，单位：GB
	 * @return
	 * @throws Exception 
	 */
	private static double getLinuxDisk() throws Exception {
		BigDecimal diskTotal = new BigDecimal(0.00);
		
		String cmd = "df -h";
		List<String> result = executeCmdWithResult(cmd);
		for(String line : result) {
			if (line.contains("Filesystem")) continue;
			
			String[] split = line.split("\\s+");
			String zoneSize = split[1].substring(0, split[1].length()-2);
			if (split[1].endsWith("G")) {
				diskTotal = diskTotal.add(new BigDecimal(zoneSize));
			} else if (split[1].endsWith("M")) {
				BigDecimal gb = new BigDecimal(zoneSize).divide(BYTE);
				diskTotal  =diskTotal.add(gb);
			} else if (split[1].endsWith("K")) {
				BigDecimal gb = new BigDecimal(zoneSize).divide(BYTE).divide(BYTE);
				diskTotal  =diskTotal.add(gb);
			}
		}
		return diskTotal.doubleValue();
	}
	
	/**
	 * 获取Windows磁盘大小，单位：GB
	 * @return
	 * @throws Exception 
	 */
	private static double getWindowsDisk() throws Exception {
		BigDecimal gbSize = new BigDecimal(0.00);
		
		String cmd = "wmic diskdrive get size /value";
		List<String> result = executeCmdWithResult(cmd);
		for(String str : result) {
			if (str.startsWith("Size")) {
				str = str.replaceAll("\\D", "");
				gbSize = new BigDecimal(str).divide(BYTE).divide(BYTE)
						.divide(BYTE, 2, BigDecimal.ROUND_HALF_UP);
				break;
			}
		}
		return gbSize.doubleValue();
	}
	
	/**
	 * 执行Linux命令，并返回命令行输出结果
	 * @param cmd 命令
	 * @return 命令行输出结果
	 * @throws Exception 
	 */
	public static List<String> executeLinuxCmd(String cmd) throws Exception {
		List<String> result = new ArrayList<String>();
		Process process = null;

		String[] cmds = {"/bin/sh", "-c", new String(cmd.getBytes()), "utf-8"};
		logger.info("process 执行指令："+cmds);
		process = Runtime.getRuntime().exec(cmds);
		logger.info("process 执行指令完成："+cmds);

		logger.info(String.format("---------- Start execute linux command: %s ----------", cmd));
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"), 1024);
		String line = null;
		while ((line = br.readLine()) != null) {
			result.add(line);
			logger.info(line);
		}
		LinuxInputStreamThread errIsThread = new LinuxInputStreamThread(process.getErrorStream());
		errIsThread.start();
		br.close();
		logger.info("销毁process");
	//	process.destroy();
		return result;
	}
	
	/**
	 * 执行Windows命令
	 * @param cmd 命令
	 * @throws Exception 
	 */
	private static void executeWindowsCmd(String cmd) throws Exception {
		Process process = null;
		String[] cmds = {"cmd", "/C", new String(cmd.getBytes()), "utf-8"};
		Runtime runtime = Runtime.getRuntime();
		process = runtime.exec(cmds);

		if (logger.isInfoEnabled()) {
			logger.info(String.format("---------- Start execute windows command: %s ----------", cmd));
		}
		WindowsInputStreamThread isThread = new WindowsInputStreamThread(process.getInputStream());
		isThread.start();
		WindowsInputStreamThread errIsThread = new WindowsInputStreamThread(process.getErrorStream());
		errIsThread.start();
	}
	
	/**
	 * 执行Windows命令，并返回命令行输出结果
	 * @param cmd 命令
	 * @return 命令行输出结果
	 * @throws Exception 
	 */
	private static List<String> executeWindowsCmdWithResult(String cmd) throws Exception {
		List<String> result = new ArrayList<String>();
		Process process = null;
		BufferedReader br = null;

		String[] cmds = {"cmd", "/c", cmd};
		Runtime runtime = Runtime.getRuntime();
		process = runtime.exec(cmds);

		if (logger.isInfoEnabled()) {
			logger.info(String.format("---------- Start execute windows command: %s ----------", cmd));
		}
		br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"), 1024);
		String line = null;
		while ((line = br.readLine()) != null) {
			result.add(line);
			logger.info(line);
		}
		WindowsInputStreamThread errIsThread = new WindowsInputStreamThread(process.getErrorStream());
		errIsThread.start();
		return result;
	}
	
	private static class WindowsInputStreamThread extends Thread {
		
		private InputStream is;
		
		public WindowsInputStreamThread(InputStream is) {
			this.is = is;
		}

		@Override
		public void run() {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(is, "GBK"), 1024);
				String line = null;
				while ((line = br.readLine()) != null) {
					logger.info(line);
				}
			} catch (IOException e) {
				if (logger.isErrorEnabled()) {
					logger.error( e);
				}
			}
		}
		
	}
	
private static class LinuxInputStreamThread extends Thread {
		
		private InputStream is;
		
		public LinuxInputStreamThread(InputStream is) {
			this.is = is;
		}

		@Override
		public void run() {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"), 1024);
				String line = null;
				while ((line = br.readLine()) != null) {
					logger.info(line);
				}
			} catch (IOException e) {
				if (logger.isErrorEnabled()) {
					logger.error( e);
				}
			}
		}
		
	}
	
}
