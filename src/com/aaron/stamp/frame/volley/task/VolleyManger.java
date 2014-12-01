package com.aaron.stamp.frame.volley.task;

import java.util.Map;

import android.content.Context;
import android.os.Handler;

import com.aaron.library.frame.pojo.MSG;
import com.aaron.library.frame.task.ITask;
import com.aaron.stamp.frame.application.StampApplication;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/** 
 * @author linjinfa 331710168@qq.com 
 * @version 创建时间：2014年8月9日 下午1:33:38 
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class VolleyManger {

	private static VolleyManger volleyManger = null;
	private RequestQueue requestQueue;
	private Handler mHandler;
	
	private VolleyManger(Context context,Handler mHandler){
		this.mHandler = mHandler;
		requestQueue = Volley.newRequestQueue(context,new VolleyHttpStack());
	}
	
	/**
	 * 
	 * @param context
	 * @param mHandler
	 * @return
	 */
	public synchronized static VolleyManger getInstance(Context context,Handler mHandler) {
		if (volleyManger == null)
			volleyManger = new VolleyManger(context,mHandler);
		return volleyManger;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized static VolleyManger getInstance() {
		if(volleyManger==null)
			StampApplication.getInstance().initVolleryManager();
		return volleyManger;
	}
	
	/**
	 * 添加并执行Task
	 * @param task
	 */
	public void addTask(ITask task){
		if (mHandler == null || task==null)
			return ;
		MSG msg = task.doTask(mHandler);
		if(msg.getObj() instanceof Request){
			requestQueue.add((Request)msg.getObj());
		}
	}
	
	/**
	 * 根据taskId取消
	 * @param taskId
	 */
	public void cancel(int taskId){
		requestQueue.cancelAll(String.valueOf(taskId));
	}
	
	/**
	 * 销毁
	 */
	public void destroy(){
		requestQueue.stop();
		requestQueue = null;
		volleyManger = null;
	}

	/**
	 * 
	 * @return
	 */
	public RequestQueue getRequestQueue() {
		return requestQueue;
	}
	
	/**
	 * 删除对应的缓存数据
	 * @param url
	 */
	public void removeCache(String url,int method,Map<String,String> params){
		if(requestQueue!=null && requestQueue.getCache()!=null){
			requestQueue.getCache().remove(VolleyRequestTask.getUrl(url, method, params));
		}
	}
	
}
