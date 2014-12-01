package com.aaron.stamp.frame.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aaron.stamp.R;

/**
 * 
 * @author linjinfa@126.com
 * @date 2013-4-11 上午10:22:56
 */
public class NotificationUtil {

	/**
	 * 提示
	 * @param context
	 * @param content
	 * @param isOk
	 */
	public static void notifyTipAutoCancel(Context context,String content,boolean isOk){
		if(isOk)
			notifyAutoCancel(context,R.drawable.ok_icon,content);
		else
			notifyAutoCancel(context,R.drawable.wrong_icon,content);
	}
	
	/**
	 * 自动消失Notification
	 * @param context
	 * @param resId	资源图片
	 * @param content 通知内容
	 */
	public static void notifyAutoCancel(Context context,int resIconId,String content){
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new NotificationCompat.Builder(context)
														  .setSmallIcon(resIconId)
														  .setTicker(content)
														  .build();
		mNotificationManager.notify(1, notification);
		mNotificationManager.cancel(1);
	}
	
	/**
	 * Notification 圆形进度加载提示
	 * @param context
	 * @param resId	资源图片
	 * @param content 通知内容
	 */
	public static void notifyLoadingAutoCancel(Context context,String content){
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new NotificationCompat.Builder(context)
														  .setSmallIcon(R.anim.notification_loading)
														  .setTicker(content)
														  .build();
		mNotificationManager.notify(0, notification);
		mNotificationManager.cancel(0);
	}
	
}
	
