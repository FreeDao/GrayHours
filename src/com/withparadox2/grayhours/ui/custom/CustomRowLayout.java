package com.withparadox2.grayhours.ui.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by withparadox2 on 14-3-1.
 */
public class CustomRowLayout extends ViewGroup{
	public CustomRowLayout(Context context) {
		super(context);
		ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, Util.getScreenHeight()/2);
		this.setLayoutParams(params);
	}

	public CustomRowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomRowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int childWidthSpec;
		int childHeightSpec;
		for (int i = 0; i < getChildCount(); i++){
			LayoutParams lp = getChildAt(i).getLayoutParams();
			if (lp.height > 0){
				childHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, lp.height);
				childWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, lp.width);
			} else {
				childHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, Util.getScreenHeight()/2);
				childWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, Util.getScreenWidth()/2);
			}
			measureChild(getChildAt(i), childWidthSpec, childHeightSpec);
		}
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY){
			setMeasuredDimension(Util.getScreenWidth(), MeasureSpec.getSize(heightMeasureSpec));
		} else {
			setMeasuredDimension(Util.getScreenWidth(), Util.getScreenHeight()/2);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (getChildCount() == 1){

		}
		switch (getChildCount()){
			case 0:
				break;
			case 1:
				BaseButton child = (BaseButton) getChildAt(0);
				child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
				break;
			case 2:
				BaseButton child1 = (BaseButton)getChildAt(0);
				BaseButton child2 = (BaseButton)getChildAt(1);
				if (child1.getAnimatingFlag()||child2.getAnimatingFlag()){
					if(child1.getAnimatingFlag()){
						child1.layout(0, 0, child1.getMeasuredWidth(), child1.getMeasuredHeight());
						child2.layout(child1.getMeasuredWidth(), 0,
							child1.getMeasuredWidth() + child2.getMeasuredWidth(), child2.getMeasuredHeight());
					} else {
						child2.layout(getMeasuredWidth() - child2.getMeasuredWidth(), 0,
							getMeasuredWidth(), child2.getMeasuredHeight());
						child1.layout(getMeasuredWidth() - child2.getMeasuredWidth() - child1.getMeasuredWidth(), 0,
							getMeasuredWidth() - child2.getMeasuredWidth(), child1.getMeasuredHeight());
					}

				} else {
					child1.layout(0, 0, child1.getMeasuredWidth(), child1.getMeasuredHeight());
					child2.layout(child1.getMeasuredWidth(), 0,
						getMeasuredWidth(), child2.getMeasuredHeight());

				}
				break;
		}
	}

	public BaseButton getCustomChild(int index){
		return (BaseButton) getChildAt(index);
	}
}
