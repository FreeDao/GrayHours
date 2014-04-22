package com.withparadox2.grayhours.ui.analysis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by withparadox2 on 14-4-21.
 */
public abstract class BaseViewGroup extends ViewGroup{

	public BaseViewGroup(Context context) {
		super(context);
	}

	public BaseViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
}
