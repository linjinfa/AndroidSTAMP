package com.aaron.stamp.frame.util;

import android.content.Context;
import android.content.Intent;

import com.aaron.stamp.module.main.MainActivity;

/**
 * 退出Util
 * 
 * @author linjinfa@126.com
 * @version 2012-9-16 下午4:17:09
 */
public class SysUtil {

	public static final int EXIT_APPLICATION = 001001;
	private Context context;

	public SysUtil(Context context) {
		this.context = context;
	}

	public void exit() {
		Intent intent = new Intent();
		intent.setClass(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("flag", EXIT_APPLICATION);
		context.startActivity(intent);
	}

}
