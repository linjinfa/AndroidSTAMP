package com.aaron.stamp.frame.util;


import com.aaron.stamp.R;
import com.aaron.stamp.frame.application.StampApplication;

/**
 * 常量类
 * 
 * @author linjinfa 331710168@qq.com
 * @date 2014-4-15
 */
public final class AaronConstants {

	private AaronConstants() {
		super();
	}

	public final static boolean DEBUG = false;

	/**
	 * 缓存过期时间(毫秒) 1分钟
	 */
	public final static long CACHE_EXPIRED_SECONDS = 1 * 60 * 1000;

	/**
	 * 应用包名
	 */
	public final static String PACKAGE_NAME = StampApplication.getInstance().getPackageName();
	/**
	 * 缓存根目录
	 */
	public final static String CACHE_ROOT = FileUtil.getDataPath() + "aaron/stamp/";
	/**
	 * 图片缓存目录
	 */
	public final static String CACHE_IMG = CACHE_ROOT + "images/";
	/**
	 * 缩略图目录
	 */
	public final static String CACHE_IMG_THUMB = CACHE_IMG + ".thumb/";
	/**
	 * 错误日志目录
	 */
	public final static String CACHE_ERROR = CACHE_ROOT + "error/";
	/**
	 * 屏幕宽度
	 */
	public static int SCREEN_WIDTH = 0;
	/**
	 * 屏幕分辨率宽度
	 */
	public static int WIDTHPIXELS = 800;
	/**
	 * 屏幕高度
	 */
	public static int SCREEN_HEIGHT = 0;
	/**
	 * 屏幕分辨率高度
	 */
	public static int HEIGHTPIXELS = 640;
	/**
	 * 屏幕密度
	 */
	public static int DENSITYDPI = 240;
	public static float DENSITY = 1.5f;
	/**
	 * TAG_KEY
	 */
	public static int TAG_KEY = R.drawable.ic_launcher;

	/********************************************************** XML偏好设置START **********************************************************************/
	/**
	 * xml偏好设置
	 */
	public final static String SETTING_XML = "stampSetting";
	/**
	 * 是否禁止网络图片
	 */
	public static boolean isBanDowloadImage = false;
	/********************************************************** XML偏好设置END **********************************************************************/

}
