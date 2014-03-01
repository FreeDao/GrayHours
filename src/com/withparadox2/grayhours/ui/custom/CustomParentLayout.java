package com.withparadox2.grayhours.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Administrator on 14-3-1.
 */
public class CustomParentLayout extends ViewGroup{
	public CustomParentLayout(Context context) {
		super(context);
	}

	public CustomParentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomParentLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
}
