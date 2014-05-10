package com.withparadox2.grayhours.ui.analysis.githubview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.support.CalendarTool;
import com.withparadox2.grayhours.ui.AnalysisFragment;
import com.withparadox2.grayhours.utils.DebugConfig;

/**
 * Created by withparadox2 on 14-4-21.
 */
public class CellView extends TextView{
	private int padding, size;
	private int columnPosition, index;
	private static Paint paint;
	private int key;
	public static int selectedPositin=AnalysisTool.TODAY_INDEX;


	public CellView(Context context, final int index) {
		super(context);
		this.index = index;
		this.setPadding(5,5,5,5);
		if (paint==null){
			paint = new Paint();
			paint.setColor(getResources().getColor(R.color.github_level_0));
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.FILL);
		}
	}

	public void setPosition(int position){
		this.columnPosition = position;
		key = -position * 7 + index - AnalysisTool.TODAY_INDEX;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (selectedPositin == columnPosition*7+index){
			this.setBackgroundColor(getResources().getColor(R.color.coral));
		} else {
			this.setBackgroundColor(getResources().getColor(R.color.transparent));
		}

		super.onDraw(canvas);
		setPaintColor();
		padding = getPaddingBottom();
		size = getHeight();
		canvas.drawRect(padding, padding, size-padding, size-padding, paint);
	}

	private void setPaintColor(){
		float amount;
		try {
			amount =(float)AnalysisFragment.map.get(key)/AnalysisTool.MAX_TOTAL_MINUTES;
		} catch (NullPointerException e) {
			amount = 0.0f;
		}
		if (amount < 0.01){
			paint.setColor(getResources().getColor(R.color.github_level_0));
		} else if (amount < 0.5){
			paint.setColor(getResources().getColor(R.color.github_level_1));
		} else if (amount < 0.75){
			paint.setColor(getResources().getColor(R.color.github_level_2));
		} else {
			paint.setColor(getResources().getColor(R.color.github_level_3));
		}
	}
}
