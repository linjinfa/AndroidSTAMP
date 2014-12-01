package com.aaron.stamp.module.login;

import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aaron.stamp.R;
import com.aaron.stamp.module.base.BaseActivity;
import com.aaron.stamp.module.main.MainActivity;

/** 登陆界面
 * @author linjinfa 331710168@qq.com 
 * @version 创建时间：2014年12月1日 下午2:54:07 
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity{

	@Override
	public void initUI(Bundle savedInstanceState) {
		super.initUI(savedInstanceState);
	}
	
	/**
	 * 登陆
	 * @param view
	 */
	public void loginClick(View view){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}
