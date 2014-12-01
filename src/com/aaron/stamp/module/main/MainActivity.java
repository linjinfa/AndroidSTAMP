package com.aaron.stamp.module.main;

import roboguice.inject.ContentView;
import android.os.Bundle;
import android.view.KeyEvent;

import com.aaron.stamp.R;
import com.aaron.stamp.module.base.BaseActivity;

/** 
 * 主界面
 * @author linjinfa 331710168@qq.com 
 * @version 创建时间：2014年12月1日 下午3:48:08 
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity{
	
	@Override
	public void initUI(Bundle savedInstanceState) {
		super.initUI(savedInstanceState);
	}
	
	/**
	 * 返回键
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			exitDialog();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

}
