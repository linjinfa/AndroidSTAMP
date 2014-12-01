package com.aaron.stamp.frame.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * 日志操作类
 * 
 * @author linjinfa 331710168@qq.com
 * @date 2014-1-14 下午5:19:56
 */
public class LogUtil {

	/**
	 * 日志等级
	 */
	private final static String logLevel = "DEBUG";
	/**
	 * 日志文件是否追加
	 */
	private final static boolean isAppend = true;
	/**
	 * 日志文件路径
	 */
	private final static String logFilePath = AaronConstants.CACHE_ERROR
			+ "log.log";

	/**
	 * 写入日志
	 * 
	 * @param tagObj
	 * @param message
	 * @param throwable
	 */
	public static synchronized void logDebug(final Object tagObj,
			final String message, final Throwable throwable) {
		if (AaronConstants.DEBUG)
			Log.d("LogUtil", "", throwable);
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuffer sb = new StringBuffer("\n[" + logLevel + "]\n");
				if (tagObj != null)
					sb.append(tagObj.getClass().getName());
				sb.append("  " + getNow() + "\n");
				if (message != null && message.trim().length() != 0) {
					sb.append(message + "\n");
				}
				if (throwable != null) {
					sb.append(throwableToString(throwable));
				}
				File logFile = getLogFile();
				if (logFile != null) {
					writeFile(logFile.getAbsolutePath(), sb.toString());
				}
			}
		}).start();
	}

	/**
	 * 写入日志
	 * 
	 * @param tagObj
	 * @param message
	 */
	public static void logDebug(Object tagObj, String message) {
		logDebug(tagObj, message, null);
	}

	/**
	 * 写入日志
	 * 
	 * @param tagObj
	 * @param throwable
	 */
	public static void logDebug(Object tagObj, Throwable throwable) {
		logDebug(tagObj, null, throwable);
	}

	/**
	 * 写入日志
	 * 
	 * @param message
	 * @param throwable
	 */
	public static void logDebug(String message, final Throwable throwable) {
		logDebug(null, message, throwable);
	}

	/**
	 * 写入日志
	 * 
	 * @param message
	 */
	public static void logDebug(String message) {
		logDebug(null, message, null);
	}

	/**
	 * 写入日志
	 * 
	 * @param throwable
	 */
	public static void logDebug(Throwable throwable) {
		logDebug(null, null, throwable);
	}

	/**
	 * 将Throwable转成String信息
	 * 
	 * @param throwable
	 * @return
	 */
	private static String throwableToString(Throwable throwable) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		// printStackTrace(PrintWriter s)
		// 将此 throwable 及其追踪输出到指定的 PrintWriter
		throwable.printStackTrace(printWriter);
		Throwable cause = throwable.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		String result = info.toString();
		printWriter.close();
		return result;
	}

	/**
	 * 返回Log日志File
	 * 
	 * @return
	 */
	private static File getLogFile() {
		if (logFilePath != null) {
			String path = "";
			if (logFilePath.contains("/")) {
				path = logFilePath.substring(0, logFilePath.lastIndexOf("/"));
				File floderFile = new File(path);
				if (!floderFile.exists()) {
					floderFile.mkdirs();
				}
			}
			File logFile = new File(logFilePath);
			if (!isAppend) { // 不追加每次重新创建日志文�?
				String fileName = logFile.getName();
				String pref = "";
				if (fileName.contains(".")) {
					String fileNames[] = fileName.split("[.]");
					if (fileNames != null && fileNames.length >= 2) {
						fileName = fileNames[0];
						pref = fileNames[1];
					}
				}
				if (path != null && path.trim().length() != 0
						&& !path.endsWith("/")) {
					path += "/";
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyyMMddHHmmss");
				return new File(path + simpleDateFormat.format(new Date())
						+ "." + pref);
			}
			return new File(logFilePath);
		}
		return null;
	}

	/**
	 * 返回当前日期时间
	 * 
	 * @return
	 */
	private static String getNow() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 写入文件
	 * 
	 * @param path
	 * @param content
	 * @return
	 */
	private synchronized static boolean writeFile(String path, String content) {
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(path, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			randomFile.write(content.getBytes());
		} catch (IOException e) {
			return false;
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
					randomFile = null;
				} catch (IOException e) {
					return false;
				}
			}
		}
		return true;
	}

}
