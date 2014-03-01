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
public class CustomParentLayout extends ViewGroup{
	public CustomParentLayout(Context context) {
		super(context);
		ViewGroup.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(lp);
	}

	public CustomParentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomParentLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
			MeasureSpec.getSize(heightMeasureSpec));
		Util.setScreenHeight(getMeasuredHeight());
		int childWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, Util.getScreenWidth());
		int childHeightSpec;
		for (int i = 0; i < getChildCount(); i++){
			LayoutParams lp = getChildAt(i).getLayoutParams();
			if (lp.height > 0){
				childHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY, lp.height);
			} else {
				childHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.AT_MOST, Util.getScreenHeight());
			}
			measureChild(getChildAt(i), childWidthSpec, childHeightSpec);
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		CustomRowLayout rowLayout1 = (CustomRowLayout) getChildAt(0);
		CustomRowLayout rowLayout2 = (CustomRowLayout) getChildAt(1);
		if (rowLayout1.getMeasuredHeight() > rowLayout2.getMeasuredHeight()){
			rowLayout1.layout(0, 0, rowLayout1.getMeasuredWidth(), rowLayout1.getMeasuredHeight());
			rowLayout2.layout(0, rowLayout1.getMeasuredHeight(), rowLayout2.getMeasuredWidth(),
				getMeasuredHeight());
		} else {
			int divideLineHeight = getMeasuredHeight() - rowLayout2.getMeasuredHeight();
			rowLayout1.layout(0, divideLineHeight - rowLayout1.getMeasuredHeight(),
				rowLayout1.getMeasuredWidth(), divideLineHeight);
			rowLayout2.layout(0, divideLineHeight, rowLayout2.getMeasuredWidth(),
				getMeasuredHeight());
		}
	}

	public CustomRowLayout getCustomChild(int index){
		return (CustomRowLayout) getChildAt(index);
	}

}
