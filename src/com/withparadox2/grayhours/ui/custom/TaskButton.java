package com.withparadox2.grayhours.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by Administrator on 14-2-20.
 */
public class TaskButton extends BaseButton{


	public TaskButton(Context context) {
		super(context, "#FF0000", "#22FF0000");
	}

	public TaskButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TaskButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
