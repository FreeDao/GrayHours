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


	public AddTaskButton(Context context) {
		super(context, "#00FF00", "#2200FF00");
	}

	public AddTaskButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AddTaskButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
}
