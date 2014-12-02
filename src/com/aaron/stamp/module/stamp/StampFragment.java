package com.aaron.stamp.module.stamp;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.aaron.stamp.R;
import com.aaron.stamp.module.base.BaseFragment;

/** STAMP
 * @author linjinfa 331710168@qq.com 
 * @version 创建时间：2014年12月1日 下午4:12:56 
 */
public class StampFragment extends BaseFragment implements OnClickListener{

	@InjectView(R.id.titleTxtView)
	private TextView titleTxtView;
	@InjectView(R.id.backBtn)
	private Button backBtn;
	@InjectView(R.id.nextBtn)
	private Button nextBtn;
	@InjectView(R.id.viewFlipper)
	private ViewFlipper viewFlipper;
	
	@Override
	protected int getLayoutResId() {
		return R.layout.fragment_stamp;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		backBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		backBtn.setVisibility(View.INVISIBLE);
		viewFlipper.setInAnimation(getActivity(), R.anim.slide_in_left);
		viewFlipper.setOutAnimation(getActivity(), R.anim.slide_out_right);
		viewFlipper.setDisplayedChild(1);
		backBtn.performClick();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.backBtn:
			viewFlipper.showPrevious();
			if(viewFlipper.getDisplayedChild()==0){
				backBtn.setVisibility(View.INVISIBLE);
			}else{
				backBtn.setVisibility(View.VISIBLE);
			}
			nextBtn.setVisibility(View.VISIBLE);
			titleTxtView.setText(viewFlipper.getCurrentView().getContentDescription());
			break;
		case R.id.nextBtn:
			viewFlipper.showNext();
			if(viewFlipper.getDisplayedChild()==viewFlipper.getChildCount()-1){
				nextBtn.setVisibility(View.INVISIBLE);
			}else{
				nextBtn.setVisibility(View.VISIBLE);
			}
			backBtn.setVisibility(View.VISIBLE);
			titleTxtView.setText(viewFlipper.getCurrentView().getContentDescription());
			break;
		}
	}
	
}
