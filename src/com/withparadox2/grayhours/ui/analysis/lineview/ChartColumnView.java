package com.withparadox2.grayhours.ui.analysis.lineview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.ui.AnalysisFragment;

/**
 * Created by withparadox2 on 14-7-2.
 */
public class ChartColumnView extends View{
	public static Paint backgroundGridPaint = null;
	public static Paint dataLinePaint;
	public static Paint circlePaint;
	public static int numOfSegments = 5;
	public static int dateTextHeight = 50;
	private static float cellHeight;
	private static int width;
	private static int height;

	private int position;
	private float pointHeight;

	public ChartColumnView(Context context, int position) {
		super(context);
		this.position = position;
		initial();
	}

	private void initial(){
		if(backgroundGridPaint == null){
			backgroundGridPaint = new Paint();
			dataLinePaint = new Paint();
			circlePaint = new Paint();

			backgroundGridPaint.setStyle(Paint.Style.STROKE);
			backgroundGridPaint.setStrokeWidth(5f);
			backgroundGridPaint.setAntiAlias(true);
			backgroundGridPaint.setColor(getResources().getColor(R.color.lightblue));

			dataLinePaint.setStyle(Paint.Style.STROKE);
			dataLinePaint.setStrokeWidth(8f);
			dataLinePaint.setColor(getResources().getColor(R.color.red));
			dataLinePaint.setAntiAlias(true);

			circlePaint.setStyle(Paint.Style.FILL);
			circlePaint.setColor(getResources().getColor(R.color.red));
			circlePaint.setAntiAlias(true);
		}
	}

	public static void setNumOfSegments(int num){
		numOfSegments = num;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawLineView(canvas);
	}

	private void drawLineView(Canvas canvas){
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		cellHeight = (height - dateTextHeight)/numOfSegments;
		drawBackgroundGrid(canvas);
		drawDataLine(canvas);
		drawCircile(canvas);
	}

	private void drawBackgroundGrid(Canvas canvas){
		float y = 0f;
		for (int i=0; i<numOfSegments; i++){
			canvas.drawLine(0, y, width, y, backgroundGridPaint);
			y += cellHeight;
		}
	}

	private void drawDataLine(Canvas canvas){
		canvas.drawLine(-width/2, getPointHeight(position-1), width/2, getPointHeight(position), dataLinePaint);
		canvas.drawLine(width/2, getPointHeight(position), width*3/2, getPointHeight(position+1), dataLinePaint);
	}

	private void drawCircile(Canvas canvas){
		canvas.drawCircle(width/2, getPointHeight(position), 10, circlePaint);
		circlePaint.setColor(getResources().getColor(R.color.white));
		canvas.drawCircle(width/2, getPointHeight(position), 5, circlePaint);
	}

	private void drawBarView(Canvas canvas){

	}

	private float getPointHeight(int position){
		return (float) AnalysisFragment.map.get(position)*numOfSegments*cellHeight/(24*60);
	}
}
