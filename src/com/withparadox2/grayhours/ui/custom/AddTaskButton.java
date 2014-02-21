package com.withparadox2.grayhours.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableRow;

/**
 * Created by Administrator on 14-2-20.
 */
public class AddTaskButton extends Button{
	public AddTaskButton(Context context) {
		super(context);
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
			TableRow.LayoutParams.WRAP_CONTENT,
			TableRow.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		this.setLayoutParams(layoutParams);
	}

	public AddTaskButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AddTaskButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
}
