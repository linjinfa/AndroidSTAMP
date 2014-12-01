package com.aaron.stamp.module.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import roboguice.fragment.RoboFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.library.frame.annotation.InjectGuice;
import com.aaron.library.frame.pojo.MSG;
import com.aaron.library.frame.task.ITask;
import com.aaron.library.frame.task.IUIController;
import com.aaron.library.frame.task.TaskManager;
import com.aaron.stamp.R;
import com.aaron.stamp.frame.application.StampApplication;
import com.aaron.stamp.frame.util.AaronConstants;
import com.aaron.stamp.frame.view.CustomProgressDialog;
import com.aaron.stamp.frame.volley.task.VolleyManger;
import com.google.gson.Gson;
import com.google.inject.Guice;

/**
 * @author linjinfa@126.com
 * @date 2013-7-1 下午2:13:58 
 */
public abstract class BaseFragment extends RoboFragment implements SensorEventListener,IUIController{

	protected Map<String,String> parms = new HashMap<String, String>();
	private CustomProgressDialog progressDialog = null;
	protected SharedPreferences preferences = null;
	private TaskManager taskManager = null;
	private VolleyManger volleyManger = null;
	protected Gson gson = null;
	protected SensorManager sensorManager;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		preferences = activity.getSharedPreferences(AaronConstants.SETTING_XML, Activity.MODE_PRIVATE);
		taskManager = TaskManager.getInstance();
		taskManager.registerUIController(this);
		volleyManger = VolleyManger.getInstance();
		gson = new Gson();
		sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
	}
	
	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(getLayoutResId(), container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initField();
		super.onActivityCreated(savedInstanceState);
		initUI(savedInstanceState);
		initListener(savedInstanceState);
		initData(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		dismissProgressDialog();
		taskManager.unRegisterUIController(this);
	}
	
	/**
	 * 初始化InjectGuice注解的字段
	 */
	private void initField() {
		Field[] fields = getClass().getDeclaredFields();
		if(fields!=null && fields.length>0){
			for(Field field : fields){
				InjectGuice injectGuice = field.getAnnotation(InjectGuice.class);
				if(injectGuice!=null){
					try {
						field.setAccessible(true);
						field.set(this,Guice.createInjector().getInstance(field.getType()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 返回View Layout的ID
	 * @return
	 */
	protected abstract int getLayoutResId();
	
	/**
	 * 替换Fragment 
	 * @param resLayId
	 * @param fragment
	 * @param isAddBackStack 是否加入返回栈
	 */
	protected void replaceFragment(int resLayId,Fragment fragment,boolean isAddBackStack){
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
		fragmentTransaction.replace(resLayId, fragment);
		if(isAddBackStack)
			fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	/**
	 * 添加Fragment
	 * @param resLayId
	 * @param fragment
	 * @param isAddBackStack
	 * @param hideFragments	要隐藏的Fragment数组
	 */
	protected void addFragment(int resLayId,Fragment showFragment,boolean isAnimation,boolean isAddBackStack,Fragment ...hideFragments){
		((BaseActivity)getActivity()).addFragment(resLayId, showFragment, isAnimation, isAddBackStack, hideFragments);
	}
	
	/**
	 * 显示隐藏Fragment
	 * @param showFragment
	 * @param hideFragments		要隐藏的Fragment数组
	 * @param isAddBackStack	是否加入返回栈
	 */
	protected void showHideFragment(Fragment showFragment,boolean isAnimation,boolean isAddBackStack,Fragment ...hideFragments){
		((BaseActivity)getActivity()).showHideFragment(showFragment, isAnimation, isAddBackStack, hideFragments);
	}
	
	/**
	 * 执行Task任务
	 * @param task
	 */
	protected void execuTask(ITask task) {
		if(task==null)
			return ;
		task.setContext(getActivity());
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
		task.setContext(getActivity());
		task.setmIdentification(getIdentification());
		if(volleyManger==null){
			StampApplication.getInstance().initVolleryManager();
			volleyManger = VolleyManger.getInstance();
		}
		if(volleyManger==null)
			return ;
		volleyManger.addTask(task);
	}
	
	/**
	 * 提示
	 * @param message
	 */
	public void titleTips(String message,boolean isNotifiyTip,boolean isOk){
//		((BaseActivity)getActivity()).titleTips(message,isNotifiyTip,isOk);
	}
	
	/**
	 * 注册传感器事件
	 */
	protected void registerSensor(){
		if(sensorManager!=null){
			sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		}
	}
	
	/**
	 * 解除注册传感器事件
	 */
	protected void unRegisterSensor(){
		if(sensorManager!=null){
			sensorManager.unregisterListener(this);
		}
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER){
            if (Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math.abs(values[2]) > 14){
            	shake();
            }
        }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 摇一摇
	 */
	protected void shake(){
		// TODO Auto-generated method stub
	}
	
	/**
	 * 显示等待进度框
	 * 
	 * @param message
	 * @param cancelable
	 */
	protected void showProgressDialog(String message, boolean cancelable) {
		progressDialog = CustomProgressDialog.show(getActivity(), "", true);
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
	 * 是否正在退出
	 * @return
	 */
	protected boolean isFinish(){
		if(getActivity()==null)
			return true;
		return ((BaseActivity)getActivity()).isFinish;
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
		return getClass().toString()+this;
	}

}
