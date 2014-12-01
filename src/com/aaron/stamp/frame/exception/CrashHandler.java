package com.aaron.stamp.frame.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

import com.aaron.stamp.frame.util.LogUtil;

/**
 * 
 * @author linjinfa@126.com
 * @date 2013-4-16 上午9:16:39
 */
public class CrashHandler implements UncaughtExceptionHandler {

	/** Debug Log Tag */
	public static final String TAG = "CrashHandler";
	/** 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能 */
	public static final boolean DEBUG = true;
	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	public CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleep一会后结束程序
			// 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			// android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(10);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		LogUtil.logDebug(ex);
		// final String msg = ex.getLocalizedMessage();
		// // 使用Toast来显示异常信息
		// new Thread() {
		// public void run() {
		// // Toast 显示需要出现在一个线程的消息队列中
		// Looper.prepare();
		// CustomToast.toastShow(mContext, "╮(╯_╰)╭...程序出错啦:" + msg);
		// Looper.loop();
		// }
		// }.start();
		return true;
	}

}