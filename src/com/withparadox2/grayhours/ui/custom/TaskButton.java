package com.withparadox2.grayhours.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

/**
 * Created by Administrator on 14-2-20.
 */
public class TaskButton extends Button{
	public TaskButton(Context context) {
		super(context);
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
			TableRow.LayoutParams.WRAP_CONTENT,
			TableRow.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		this.setLayoutParams(layoutParams);
	}

	public TaskButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TaskButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


}
