package com.aaron.stamp.frame.application;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.aaron.library.frame.application.CustomApplication;
import com.aaron.stamp.frame.exception.CrashHandler;
import com.aaron.stamp.frame.util.AaronConstants;
import com.aaron.stamp.frame.volley.task.VolleyManger;


/** 
 * @author linjinfa 331710168@qq.com 
 * @version 创建时间：2014年11月27日 上午11:22:24 
 */
public class StampApplication extends CustomApplication{

	private static StampApplication application;
	
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		
		initVolleryManager();
		
		DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
		AaronConstants.WIDTHPIXELS = displayMetrics.widthPixels;
		AaronConstants.HEIGHTPIXELS = displayMetrics.heightPixels;
		AaronConstants.DENSITYDPI = displayMetrics.densityDpi;
		AaronConstants.DENSITY = displayMetrics.density;

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		AaronConstants.SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();
		AaronConstants.SCREEN_HEIGHT = wm.getDefaultDisplay().getHeight();
		if(AaronConstants.DEBUG){
			System.out.println("==>分辨率  " + AaronConstants.WIDTHPIXELS + " X " + AaronConstants.HEIGHTPIXELS + "  " + AaronConstants.DENSITYDPI+"   "+AaronConstants.DENSITY);
			System.out.println("==>尺寸  " + AaronConstants.SCREEN_WIDTH + " X " + AaronConstants.SCREEN_HEIGHT);
		}
		CrashHandler crashHandler = CrashHandler.getInstance();
		// 注册crashHandler
		crashHandler.init(getApplicationContext());
	}
	
	/**
	 * 初始化VolleryManager
	 */
	public void initVolleryManager(){
		VolleyManger.getInstance(this, mHandler);
	}

	public static StampApplication getInstance() {
		return application;
	}
	
}
