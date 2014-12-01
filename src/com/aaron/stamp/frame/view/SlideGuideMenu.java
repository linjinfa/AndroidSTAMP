package com.aaron.stamp.frame.view;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aaron.library.frame.util.DensityUtil;
import com.aaron.stamp.R;

/**
 * 滑块底部引导菜单
 * @author linjinfa
 * @email linjinfa@live.cn	331710168@qq.com
 * @time 2013-6-30 下午8:42:51
 */
public class SlideGuideMenu extends RadioGroup {
	
	/**
	 * 延迟
	 */
	private static final int DELAY = 5;
	/**
	 * 速度
	 */
	private static final int SPEED = 50;
	/**
	 * 移动
	 */
	private static final int MOVE = 1;
	/**
	 * Menu图片
	 */
	private List<Drawable> menuImageList = new ArrayList<Drawable>();
	/**
	 * Menu文字
	 */
	private List<String> menuNameList = new ArrayList<String>();
	/**
	 * Menu点击事件
	 */
	private SlideGuideClick slideGuideClick;
	/**
	 * 移动的背景块
	 */
	private Drawable sliderDrawable;
	/**
	 * 字体大小
	 */
	private int textSize = 30;
	/**
	 * 文字颜色
	 */
	private ColorStateList textColor;
	/**
	 * 存放已初始化的Fragment
	 */
	private Fragment[] fragments;
	/**
	 * 当前显示的Fragment
	 */
	private Fragment currFragment;
	/**
	 * fragmentViewId
	 */
	private int fragmentViewId;
	/**
	 * 是否正在切换Fragment
	 */
	private boolean isSwitchFragmenting = false;
	/**
	 * 当前的区域
	 */
	private Rect mNowRect = new Rect();
	/**
	 * 结束的区域
	 */
    private Rect mEndRect = new Rect();
    /**
     * 点击的position位置
     */
    private int defaultIndex = 0;
    /**
     * 是否正在执行动画
     */
    private boolean isAniming = false;

    private Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		if (msg.what == MOVE) {
    			//如果还没有到达目标区域，就延迟DELAY后，重新绘图
    			isAniming = !move();
    			if (isAniming) {
    				sendEmptyMessageDelayed(MOVE, DELAY);
    			}
    		}
    	};
    };

    /**
     * 
     * @param context
     * @param attrs
     */
	public SlideGuideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		if (isInEditMode()) {
			return;
		}
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.slideguide);
		int menuIconsResId = typedArray.getResourceId(R.styleable.slideguide_menuIcons, View.NO_ID);
		int menuNamesResId = typedArray.getResourceId(R.styleable.slideguide_menuNames, View.NO_ID);
		int fragmentsResId = typedArray.getResourceId(R.styleable.slideguide_fragments, View.NO_ID);
		fragmentViewId = typedArray.getResourceId(R.styleable.slideguide_fragmentViewId, View.NO_ID);
		sliderDrawable = typedArray.getDrawable(R.styleable.slideguide_sliderImg);
		textSize = typedArray.getInt(R.styleable.slideguide_textSize, textSize);
		textColor = typedArray.getColorStateList(R.styleable.slideguide_textColor);
		if(textColor==null){
			textColor = getResources().getColorStateList(R.drawable.menu_black_white_txt);
		}
		loadResList(menuIconsResId,menuNamesResId);
		initFragment(fragmentsResId);
		loadMenu();
		typedArray.recycle();
	}

	public SlideGuideMenu(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(isInEditMode())
			return ;
		if(getChildCount()!=0){
//			getChildAt(defaultIndex).getHitRect(mNowRect);
			getHitRectByView(getChildAt(defaultIndex),mNowRect);
		}
		invalidate();
		super.onLayout(changed, l, t, r, b);
	}

	/**
	 * 加载资源List
	 */
	private void loadResList(int menuIconsResId, int menuNamesResId){
		if(menuIconsResId!=View.NO_ID){
			TypedArray menuImageTyped = getContext().getResources().obtainTypedArray(menuIconsResId);
			if(menuImageTyped!=null){
				for(int i=0;i<menuImageTyped.length();i++){
					menuImageList.add(menuImageTyped.getDrawable(i));
				}
				menuImageTyped.recycle();
			}
		}
		if(menuNamesResId!=View.NO_ID){
			TypedArray menuNameTyped = getContext().getResources().obtainTypedArray(menuNamesResId);
			if(menuNameTyped!=null){
				for(int i=0;i<menuNameTyped.length();i++){
					menuNameList.add(menuNameTyped.getString(i));
				}
				menuNameTyped.recycle();
			}
		}
	}
	
	/**
	 * 初始化Fragment
	 * @param fragmentTyped
	 */
	private void initFragment(int fragmentsResId){
		if(fragmentsResId!=View.NO_ID){
			TypedArray fragmentTyped = getContext().getResources().obtainTypedArray(fragmentsResId);
			if(fragmentTyped!=null){
				fragments = new Fragment[fragmentTyped.length()];
				for(int i=0;i<fragmentTyped.length();i++){
					fragments[i] = Fragment.instantiate(getContext(), fragmentTyped.getString(i));
				}
				fragmentTyped.recycle();
			}
		}
	}
	
	/**
	 * 加载菜单
	 */
	public void loadMenu(){
		setOrientation(RadioGroup.HORIZONTAL);
		setWillNotDraw(false);
		removeAllViews();
		if(menuImageList.size()==0)
			return ;
		for(int menuPosition = 0;menuPosition<menuImageList.size();menuPosition++){
			RadioButton radioButton;
			if(menuNameList.size()==0){
				radioButton = new RadioButtonCenter(getContext());
				((RadioButtonCenter)radioButton).setBtnDrawable(menuImageList.get(menuPosition));
				radioButton.setPadding(0, DensityUtil.px2dp(getContext(), 40), 0, DensityUtil.px2dp(getContext(), 40));
			}else{
				radioButton = new RadioButton(getContext());
				radioButton.setCompoundDrawablesWithIntrinsicBounds(null, menuImageList.get(menuPosition), null, null);
				radioButton.setPadding(0, DensityUtil.px2dp(getContext(), 5), 0, DensityUtil.px2dp(getContext(), 5));
			}
			radioButton.setButtonDrawable(new BitmapDrawable(getResources(),(Bitmap)null));
			radioButton.setTag(""+menuPosition);
			if(menuPosition<menuNameList.size()){
				radioButton.setGravity(Gravity.CENTER);
				radioButton.setText(menuNameList.get(menuPosition));
				radioButton.setTextColor(textColor);
				radioButton.setTextSize(DensityUtil.px2sp(getContext(), textSize));
			}
			radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						for(int i=0;i<getChildCount();i++){
							if(getChildAt(i) != buttonView){
								((RadioButton)getChildAt(i)).setChecked(false);
							}
						}
					}
				}
			});
			radioButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isAniming)
						return ;
					int index = Integer.parseInt(v.getTag().toString());
					if(fragments!=null && fragments.length!=0){
						switchFragment(index);
					}
					if(slideGuideClick!=null)
						slideGuideClick.onSlideGuideClick(Integer.parseInt(v.getTag().toString()));
					if(sliderDrawable!=null)
						selectTab(v);
					defaultIndex = index;
				}
			});
			RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT, 1);
			if(menuNameList.size()==0){
				params.topMargin = 10;
				params.bottomMargin = 5;
			}
			addView(radioButton, params);
			if(menuPosition==0)
				radioButton.performClick();
		}
	}
	
	/**
	 * 在menu被点击时调用它，将mDrawable移动到目标控件v
	 * @param 目标控件
	 */
	private void selectTab(View v) {
//		v.getHitRect(mEndRect);
		getHitRectByView(v,mEndRect);
		if (mNowRect.right != mEndRect.right) {
			mHandler.sendEmptyMessage(MOVE); //向Handler发送消息，开始移动mDrawable
		}
	}
	
	/**
	 * 重新计算图片的位置
	 * @return 动画是否结束
	 */
	private boolean move() {
		//已非常接近目标控件, 直接让mNowRect和mEndRect重合
		if (Math.abs(mNowRect.left - mEndRect.left) <= SPEED) {
			mNowRect.left = mEndRect.left;
			mNowRect.right = mEndRect.right;
			invalidate();
			return true;
		}
	
		int direction = 0;
		if (mNowRect.left < mEndRect.left) {
			direction = 1; //向右
		} else {
			direction = -1; //向左
		}

		mNowRect.left += SPEED * direction;
		mNowRect.right += SPEED * direction;
		invalidate();
		return false;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(sliderDrawable!=null){
			sliderDrawable.setBounds(mNowRect);
			sliderDrawable.draw(canvas);
		}
	}
	
	/**
	 * 
	 * @param view
	 * @param outRect
	 */
	private void getHitRectByView(View view, Rect outRect){
		view.getHitRect(outRect);
		if(view instanceof RadioButtonCenter){
			RadioButtonCenter radioButtonCenter = (RadioButtonCenter) view;
			if(radioButtonCenter.getMinHeightc()!=0){
				outRect.bottom =radioButtonCenter.getMinHeightc()+outRect.top;
			}
		}
		RadioGroup.LayoutParams params = (LayoutParams) view.getLayoutParams();
		if(params!=null){
			outRect.top -= params.topMargin; 
			outRect.left -=params.leftMargin;
			outRect.right +=params.rightMargin;
			outRect.bottom +=params.bottomMargin;
		}
	}
	
	/**
	 * 切换Fragment
	 * @param fragmentIndex
	 */
	private void switchFragment(int fragmentIndex){
		if(!(getContext() instanceof FragmentActivity) || fragmentViewId == View.NO_ID){
			return ;
		}
		if(isSwitchFragmenting || fragmentIndex>fragments.length-1)
			return ;
		isSwitchFragmenting = true;
		FragmentTransaction fragmentTransaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
		for(Fragment hideFragment : fragments){
			if(hideFragment!=null && hideFragment.isAdded())
				fragmentTransaction.hide(hideFragment);
		}
		Fragment showFragment = fragments[fragmentIndex];
		if(!showFragment.isAdded()){
			fragmentTransaction.add(fragmentViewId, showFragment);
		}else{
			fragmentTransaction.show(showFragment);
		}
		fragmentTransaction.commit();
		currFragment = showFragment;
		isSwitchFragmenting = false;
	}
	
	/**
	 * 根据position设置选中
	 * @param position
	 */
	public void setSelected(int position){
		if(position>getChildCount()){
			return ;
		}
		defaultIndex = position;
		RadioButton radioButton = (RadioButton)getChildAt(position);
		radioButton.setChecked(true);
	}
	
	/**
	 * 还原菜单未中状态
	 */
	public void restoreMenu(){
		int raBtnId = getCheckedRadioButtonId();
		if(raBtnId!=-1){
			((RadioButton)findViewById(raBtnId)).setChecked(false);
		}
	}

	/**
	 * 菜单个数
	 * @return
	 */
	public int getMenuCount() {
		return menuImageList.size();
	}

	/**
	 * 当前显示的Fragment
	 * @return
	 */
	public Fragment getCurrFragment() {
		return currFragment;
	}

	public void setMenuImageList(List<Drawable> menuImageList) {
		this.menuImageList = menuImageList;
	}

	public void setMenuNameList(List<String> menuNameList) {
		this.menuNameList = menuNameList;
	}

	public SlideGuideClick getOnSlideGuideClick() {
		return slideGuideClick;
	}

	public void setOnSlideGuideClick(SlideGuideClick slideGuideClick) {
		this.slideGuideClick = slideGuideClick;
	}

	/**
	 * Menu点击接口
	 * @author linjinfa@126.com
	 * @date 2013-7-1 下午5:41:09
	 */
	public interface SlideGuideClick{
		void onSlideGuideClick(int position);
	}
}
