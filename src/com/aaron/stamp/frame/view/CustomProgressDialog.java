package com.aaron.stamp.frame.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aaron.stamp.R;

/**
 * 自定义等待Dialog
 * 
 * @author linjinfa 331710168@qq.com
 * @date 2014年5月9日
 */
public class CustomProgressDialog extends Dialog {

	private static CustomProgressDialog dialog;
	private TextView progress_message;

	public CustomProgressDialog(Context context,int resLayId) {
		super(context);
		init(resLayId);
	}

	public CustomProgressDialog(Context context,int resLayId, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(resLayId);
	}

	public CustomProgressDialog(Context context,int resLayId, int theme) {
		super(context, theme);
		init(resLayId);
	}

	/**
	 * 显示
	 * 
	 * @param context
	 * @param message
	 * @param cancelable
	 * @return
	 */
	public static CustomProgressDialog show(Context context, CharSequence message, boolean cancelable) {
		return show(context,message,cancelable,R.layout.progress_dialog,R.style.ActivityDialog);
	}

	/**
	 * 
	 * @param context
	 * @param message
	 * @param cancelable
	 * @param theme
	 * @return
	 */
	public static CustomProgressDialog show(Context context, CharSequence message, boolean cancelable, int resLayId, int theme) {
		dialog = new CustomProgressDialog(context, resLayId, theme);
		dialog.getWindow().setWindowAnimations(R.style.AlphaWindowExitAnimStyle);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.setMessage(message);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.show();
		return dialog;
	}

	/**
	 * 关闭
	 */
	public void disappear() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	/**
	 * 初始化
	 */
	private void init(int resLayId) {
		setContentView(resLayId);
		progress_message = (TextView) findViewById(R.id.progress_message);
		disappear();
	}

	/**
	 * 设置消息
	 * 
	 * @param message
	 */
	private void setMessage(CharSequence message) {
		if (TextUtils.isEmpty(message)) {
			progress_message.setVisibility(View.GONE);
		} else {
			progress_message.setVisibility(View.VISIBLE);
			progress_message.setText(message);
		}
	}

}
