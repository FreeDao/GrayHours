package com.withparadox2.grayhours.ui.analysis.lineview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.ui.AnalysisFragment;
import com.withparadox2.grayhours.ui.analysis.githubview.CellView;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by withparadox2 on 14-7-2.
 */
public class ChartColumnView extends View{
	public static Paint backgroundGridPaint = null;
	public static Paint dataLinePaint;
	public static Paint circlePaint;
	public static Paint dateTextPaint;
	public static Paint barPaint;
	public static int numOfSegments = 8;
	public static int dateTextHeight = Util.dip2px(20);
	private static float paddingTop = 10f;
	private static float cellHeight;
	private static int width;
	private static int height;

	private int position;
	private float pointHeight;

	public ChartColumnView(Context context) {
		super(context);
		initial();
	}

	private void initial(){
		if(backgroundGridPaint == null){
			backgroundGridPaint = new Paint();
			dataLinePaint = new Paint();
			circlePaint = new Paint();
			dateTextPaint = new Paint();
			barPaint = new Paint();

			backgroundGridPaint.setStyle(Paint.Style.STROKE);
			backgroundGridPaint.setStrokeWidth(5f);
			backgroundGridPaint.setAntiAlias(true);
			backgroundGridPaint.setColor(getResources().getColor(R.color.lightblue));

			dataLinePaint.setStyle(Paint.Style.STROKE);
			dataLinePaint.setStrokeWidth(7f);
			dataLinePaint.setColor(getResources().getColor(R.color.red));
			dataLinePaint.setAntiAlias(true);

			circlePaint.setStyle(Paint.Style.FILL);
			circlePaint.setColor(getResources().getColor(R.color.red));
			circlePaint.setAntiAlias(true);

			dateTextPaint.setTextSize(20);
			dateTextPaint.setColor(getResources().getColor(R.color.red));
			dateTextPaint.setTextAlign(Paint.Align.CENTER);

			barPaint.setColor(getResources().getColor(R.color.red));
			barPaint.setAlpha(200);
			barPaint.setStyle(Paint.Style.FILL);
		}
	}

	public static void setNumOfSegments(int num){
		numOfSegments = num;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		cellHeight = (height - dateTextHeight - paddingTop)/Float.valueOf(numOfSegments);
		//drawLineView(canvas);
		drawBarView(canvas);
	}

	private void drawLineView(Canvas canvas){
		drawBackgroundGrid(canvas);
		drawDataLine(canvas);
		drawCircile(canvas);
		drawText(canvas);
	}

	private void drawBackgroundGrid(Canvas canvas){
		float y = paddingTop;
		for (int i=0; i<numOfSegments+1; i++){
			canvas.drawLine(0, y, width, y, backgroundGridPaint);
			y += cellHeight;
		}
	}

	private void drawDataLine(Canvas canvas){
		canvas.drawLine(-width/2, getPointHeight(position+1), width/2, getPointHeight(position), dataLinePaint);
		canvas.drawLine(width/2, getPointHeight(position), width*3/2, getPointHeight(position-1), dataLinePaint);
	}

	private void drawCircile(Canvas canvas){
		circlePaint.setColor(getResources().getColor(R.color.red));
		canvas.drawCircle(width / 2, getPointHeight(position), 15, circlePaint);
		circlePaint.setColor(getResources().getColor(R.color.white));
		canvas.drawCircle(width / 2, getPointHeight(position), 6, circlePaint);
	}

	private void drawText(Canvas canvas) {
		canvas.drawText("02-12", width/2, height-dateTextHeight/2, dateTextPaint);
	}

	private void drawBarView(Canvas canvas){
		int paddingLeftRight = 5;
		drawBackgroundGrid(canvas);
		canvas.drawRect(paddingLeftRight, getPointHeight(position), width-paddingLeftRight, height-dateTextHeight, barPaint);
		drawText(canvas);
	}

	private float getPointHeight(int position){
		float height1;
		try {
			height1 = (float) AnalysisFragment.map.get(position)*numOfSegments*cellHeight/(24*60);
		} catch (NullPointerException e){
			height1 = 0;
		}
		return height - dateTextHeight - height1;
	}

	public void setPosition(int position){
		this.position = position;
	}
}
