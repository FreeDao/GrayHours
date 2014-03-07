package com.withparadox2.grayhours.ui.analysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

import java.util.Map;

/**
 * Created by withparadox2 on 14-3-3.
 */
public class LinePlotView extends View{
	private Paint gridPaint;
	private Paint labelPaint;
	private Paint dataLinePaint;

	private int gridLinePaddintLeft = Util.dip2px(20);
	private int gridLinePaddintBottom = Util.dip2px(40);
	private int gridLinePaddintTop = Util.dip2px(20);


	private float widthOfView;
	private float heightOfView;

	private int index = 0;
	private Map<Integer, Integer> map;

	public LinePlotView(Context context) {
		this(context, null, 0);
	}

	public LinePlotView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinePlotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialPaint();
		map = AnalysisTool.getDataMap(index);
	}

	private void initialPaint(){
		gridPaint = new Paint();
		labelPaint = new Paint();
		dataLinePaint = new Paint();

		gridPaint.setColor(getResources().getColor(R.color.grid_line_color));
		gridPaint.setStyle(Paint.Style.STROKE);
		gridPaint.setStrokeWidth(1f);
		labelPaint.setColor(getResources().getColor(R.color.label_color));
		labelPaint.setTextAlign(Paint.Align.CENTER);
		labelPaint.setTextSize(Util.sp2px(13));
		dataLinePaint.setColor(getResources().getColor(R.color.data_line_color));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		widthOfView = getMeasuredWidth();
		heightOfView = getMeasuredHeight();
		drawBackgroundGrid(canvas, gridPaint);
		drawVerticalLabel(canvas, labelPaint);
	}



	private void drawBackgroundGrid(Canvas canvas, Paint paint){
		float cellHeight = (heightOfView - gridLinePaddintBottom - gridLinePaddintTop)/12;
		float cellWidth = (widthOfView - gridLinePaddintLeft)/7;
		for (int i=0; i<=12; i++){
			float y = i*cellHeight;
			canvas.drawLine(gridLinePaddintLeft, y + gridLinePaddintTop, widthOfView,  y + gridLinePaddintTop, paint);
		}
		for (int i=0; i<=7; i++){
			float x = gridLinePaddintLeft + i*cellWidth;
			canvas.drawLine(x, gridLinePaddintTop, x, heightOfView - gridLinePaddintBottom, paint);
		}
	}

	private void drawVerticalLabel(Canvas canvas, Paint paint){
		float cellHeight = (heightOfView - gridLinePaddintBottom -  gridLinePaddintTop)/6;
		float cellWidth = (widthOfView - gridLinePaddintLeft)/7;
		for (int i=0; i<=6; i++){
			canvas.drawText(String.valueOf(i*4), gridLinePaddintLeft/2, gridLinePaddintTop + cellHeight*(6-i)+5, paint);
		}
	}

	public void setIndex(int index){
		this.index = index;
	}
}
