package com.withparadox2.grayhours.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by Administrator on 14-2-20.
 */
public class AddTaskButton extends BaseButton{


	public AddTaskButton(Context context, String color) {
		super(context, color);
	}

	public AddTaskButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AddTaskButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float width =  getMeasuredWidth();
		float height = getMeasuredHeight();
		float r = width < height ? width*3/8 : height*3/8;
		float padding = 3*r/5;
		canvas.drawCircle(width/2, height/2, r, getCircleStrokePaint());
		if (ACTION_DOWN){
			canvas.drawCircle(width/2, height/2, r, getCircleFilledPaint());
		}
		canvas.drawLine(width/2, height/2-r+padding, width/2, height/2+r-padding, getCircleStrokePaint());
		canvas.drawLine(width/2-r+padding, height/2, width/2+r-padding, height/2, getCircleStrokePaint());
	}
}
