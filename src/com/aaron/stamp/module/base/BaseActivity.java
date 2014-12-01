package com.aaron.stamp.module.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import roboguice.activity.RoboFragmentActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.aaron.library.frame.annotation.InjectGuice;
import com.aaron.library.frame.pojo.MSG;
import com.aaron.library.frame.task.ITask;
import com.aaron.library.frame.task.IUIController;
import com.aaron.library.frame.task.TaskManager;
import com.aaron.stamp.R;
import com.aaron.stamp.frame.util.AaronConstants;
import com.aaron.stamp.frame.util.LogUtil;
import com.aaron.stamp.frame.util.SysUtil;
import com.aaron.stamp.frame.view.CustomProgressDialog;
import com.aaron.stamp.frame.volley.task.VolleyManger;
import com.google.inject.Guice;

/**
 * @author linjinfa@126.com
 * @date 2013-4-16 下午4:05:23
 */
public class BaseActivity extends RoboFragmentActivity implements IUIController {

	protected Map<String, String> parms = new HashMap<String, String>();
	private CustomProgressDialog progressDialog = null;
	protected SharedPreferences preferences = null;
	private TaskManager taskManager = null;
	private VolleyManger volleyManger;
	// private View titleTipView;
	/**
	 * 是否结束退出
	 */
	protected boolean isFinish = false;
	/**
	 * 是否正在切换Fragment
	 */
	private boolean isSwitchFragmenting = false;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		int flag = getIntent().getIntExtra("flag", 0);
		if (flag == SysUtil.EXIT_APPLICATION) {
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			return;
		}
		preferences = getSharedPreferences(AaronConstants.SETTING_XML,Activity.MODE_PRIVATE);
		taskManager = TaskManager.getInstance();
		taskManager.registerUIController(this);
		volleyManger = VolleyManger.getInstance();
		super.onCreate(savedInstanceState);
		initField();
		initUI(savedInstanceState);
		initListener(savedInstanceState);
		initData(savedInstanceState);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onDestroy() {
		isFinish = true;
		super.onDestroy();
		dismissProgressDialog();
		taskManager.unRegisterUIController(this);
	}

	/**
	 * 初始化InjectGuice注解的字段
	 */
	private void initField() {
		Field[] fields = getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				try {
					field.setAccessible(true);
					InjectGuice injectGuice = field
							.getAnnotation(InjectGuice.class);
					if (injectGuice != null) {
						field.set(
								this,
								Guice.createInjector().getInstance(
										field.getType()));
					}
				} catch (Exception e) {
					LogUtil.logDebug(e);
				}
			}
		}
	}

	/**
	 * 替换Fragment (默认有动画效果)
	 * 
	 * @param resLayId
	 * @param fragment
	 * @param isAddBackStack
	 *            是否加入返回栈
	 */
	protected void replaceFragment(int resLayId, Fragment fragment,
			boolean isAddBackStack) {
		if (isSwitchFragmenting) {
			return;
		}
		isSwitchFragmenting = true;
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
				R.anim.slide_out_right, R.anim.slide_in_left,
				R.anim.slide_out_right);
		fragmentTransaction.replace(resLayId, fragment);
		if (isAddBackStack)
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		isSwitchFragmenting = false;
	}

	/**
	 * 替换Fragment
	 * 
	 * @param resLayId
	 * @param fragment
	 * @param isAddBackStack
	 * @param isAnimation
	 */
	protected void replaceFragment(int resLayId, Fragment fragment,
			boolean isAddBackStack, boolean isAnimation) {
		if (isSwitchFragmenting) {
			return;
		}
		isSwitchFragmenting = true;
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		if (isAnimation)
			fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
					R.anim.slide_out_right, R.anim.slide_in_left,
					R.anim.slide_out_right);
		fragmentTransaction.replace(resLayId, fragment);
		if (isAddBackStack)
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		isSwitchFragmenting = false;
	}

	/**
	 * 添加Fragment
	 * 
	 * @param resLayId
	 * @param fragment
	 * @param isAddBackStack
	 * @param hideFragments
	 *            要隐藏的Fragment数组
	 */
	protected void addFragment(int resLayId, Fragment showFragment,
			boolean isAnimation, boolean isAddBackStack,
			Fragment... hideFragments) {
		if (isSwitchFragmenting) {
			return;
		}
		isSwitchFragmenting = true;
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		if (isAnimation)
			fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
					R.anim.slide_out_right, R.anim.slide_in_left,
					R.anim.slide_out_right);
		if (hideFragments != null) {
			for (Fragment hideFragment : hideFragments) {
				if (hideFragment != null)
					fragmentTransaction.hide(hideFragment);
			}
		}
		fragmentTransaction.add(resLayId, showFragment);
		if (isAddBackStack)
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		isSwitchFragmenting = false;
	}

	/**
	 * 显示隐藏Fragment
	 * 
	 * @param showFragment
	 * @param hideFragments
	 *            要隐藏的Fragment数组
	 * @param isAddBackStack
	 *            是否加入返回栈
	 */
	protected void showHideFragment(Fragment showFragment, boolean isAnimation,
			boolean isAddBackStack, Fragment... hideFragments) {
		if (isSwitchFragmenting) {
			return;
		}
		isSwitchFragmenting = true;
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		if (isAnimation)
			fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
					R.anim.slide_out_right, R.anim.slide_in_left,
					R.anim.slide_out_right);
		if (hideFragments != null) {
			for (Fragment hideFragment : hideFragments) {
				if (hideFragment != null && hideFragment.isAdded())
					fragmentTransaction.hide(hideFragment);
			}
		}
		if (showFragment != null)
			fragmentTransaction.show(showFragment);
		if (isAddBackStack)
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		isSwitchFragmenting = false;
	}
	
	/**
	 * 执行Task任务
	 * 
	 * @param task
	 */
	protected void execuTask(ITask task) {
		if (task == null)
			return;
		task.setContext(this);
		task.setmIdentification(getIdentification());
		taskManager.addTask(task);
	}
	
	/**
	 * 执行Volley Task任务
	 * @param task
	 */
	protected void execuVolleyTask(ITask task) {
		if(task==null)
			return ;
		task.setContext(this);
		task.setmIdentification(getIdentification());
		volleyManger.addTask(task);
	}

	/**
	 * 提示
	 * 
	 * @param message
	 * @param isNotifiyTip
	 *            是否使用Notification提示
	 * @param isOk
	 */
	protected void titleTips(String message, boolean isNotifiyTip, boolean isOk) {
		// if(isNotifiyTip){
		// NotificationUtil.notifyTipAutoCancel(this, message, isOk);
		// }
		// if(titleTipView==null){
		// titleTipView = findViewById(R.id.titleCommonLay);
		// }
		// if(titleTipView!=null && !isFinishing())
		// TopTipsAutoPopWin.show(this, titleTipView, message);
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void backClick(View view) {
		isFinish = true;
		finish();
	}

	/**
	 * 显示等待进度框
	 * 
	 * @param message
	 * @param cancelable
	 */
	protected void showProgressDialog(String message, boolean cancelable) {
		progressDialog = CustomProgressDialog.show(this, message, cancelable);
	}

	/**
	 * 关闭ProgressDialog
	 */
	protected void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.disappear();
			progressDialog = null;
		}
	}
	
	/**
	 * 退出Dialog
	 */
	protected void exitDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				isFinish = true;
				SysUtil sys = new SysUtil(BaseActivity.this);
				sys.exit();
				onDestroy();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	/**
	 * 是否结束退出
	 * @return
	 */
	public boolean isFinish() {
		return isFinish;
	}

	/**
	 * 返回键
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshUI(int id, MSG msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getIdentification() {
		return getClass().toString() + this;
	}

}
