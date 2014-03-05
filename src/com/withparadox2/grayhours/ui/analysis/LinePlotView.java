package com.withparadox2.grayhours.ui.analysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by withparadox2 on 14-3-3.
 */
public class LinePlotView extends View{
	public LinePlotView(Context context) {
		super(context);
	}

	public LinePlotView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LinePlotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	private void drawBackgroundGrid(Canvas canvas, Paint paint){
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

	}
}
