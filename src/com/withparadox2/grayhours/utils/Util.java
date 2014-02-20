package com.withparadox2.grayhours.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by withparadox2 on 14-2-20.
 */
public class Util {


	public static int dip2px(int dipValue) {
		float reSize = GlobalContext.getInstance().getResources().getDisplayMetrics().density;
		return (int) ((dipValue * reSize) + 0.5);
	}

	public static int px2dip(int pxValue) {
		float reSize = GlobalContext.getInstance().getResources().getDisplayMetrics().density;
		return (int) ((pxValue / reSize) + 0.5);
	}

	public static float sp2px(int spValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
			GlobalContext.getInstance().getResources().getDisplayMetrics());
	}


}