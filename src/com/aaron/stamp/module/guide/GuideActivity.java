package com.aaron.stamp.module.guide;

import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;

import com.aaron.stamp.R;
import com.aaron.stamp.module.base.BaseActivity;
import com.aaron.stamp.module.login.LoginActivity;

/** 引导界面
 * @author linjinfa 331710168@qq.com 
 * @version 创建时间：2014年11月27日 下午4:24:49 
 */
@ContentView(R.layout.activity_guide)
public class GuideActivity extends BaseActivity{

	@Override
	public void initUI(Bundle savedInstanceState) {
		super.initUI(savedInstanceState);
		findViewById(R.id.guideImgView).postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
			}
		}, 1500);
	}

}
