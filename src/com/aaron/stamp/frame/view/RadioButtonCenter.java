package com.aaron.stamp.frame.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

import com.aaron.stamp.R;

/**
 * RadioButton 居中显示
 * @author linjinfa@126.com 
 * @version 2013-1-5 下午3:22:11
 */
public class RadioButtonCenter extends RadioButton{
	
	private Drawable buttonDrawable;
	private int minWidth = 0;
	private int minHeight = 0;

	public RadioButtonCenter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RadioButtonCenter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public RadioButtonCenter(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}
	
	/**
	 * 初始化
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompoundButton, 0, 0);
        buttonDrawable = a.getDrawable(0);
        setButtonDrawable(new BitmapDrawable(getResources(),(Bitmap)null));
        a.recycle();
	}

	public void setBtnDrawable(Drawable d) {
		this.buttonDrawable = d;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(isInEditMode())
			return ;
		super.onDraw(canvas);
		if (buttonDrawable != null) {
			buttonDrawable.setState(getDrawableState());
			int verticalGravity = getGravity()& Gravity.VERTICAL_GRAVITY_MASK;
			int buttonWidth = buttonDrawable.getIntrinsicWidth();
			int height = buttonDrawable.getIntrinsicHeight();
			if(buttonWidth>getWidth()){
				minWidth = buttonWidth+10;
				setMinWidth(minWidth);
			}
			if(height>getHeight()){
				minHeight= height+10;
				setMinHeight(minHeight);
			}
			
			int y = 0;
			switch (verticalGravity) {
			case Gravity.BOTTOM:
				y = Math.abs(getHeight() - height);
				break;
			case Gravity.CENTER_VERTICAL:
				y = Math.abs((getHeight() - height)) / 2;
				break;
			}
			
			int buttonLeft = (getWidth() - buttonWidth) / 2;
			buttonDrawable.setBounds(buttonLeft, y, buttonLeft + buttonWidth, y+height);
			buttonDrawable.draw(canvas);
		}
	}

	public int getMinWidthc() {
		return minWidth;
	}

	public int getMinHeightc() {
		return minHeight;
	}
	
}
