package com.withparadox2.grayhours.ui.analysis;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.support.CalendarTool;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

import java.util.Map;
import java.util.Random;

/**
 * Created by withparadox2 on 14-3-3.
 */
public class LinePlotView extends View implements ValueAnimator.AnimatorUpdateListener{
	private Paint gridPaint;
	private Paint labelPaint;
	private Paint dataLinePaint;
	private Paint framePaint;

	private int mLabelSeparation;
	private int mLabelWidth;
	private int mLabelHeight;


	private float widthOfView;
	private float heightOfView;

	private float cellWidth;
	private float cellHeight;

	private float scrollOffSet = 0f;

	private int index = 0;
	private Map<Integer, Integer> map;

	private GestureDetector gestureDetector;

	private Rect contentRect = new Rect();

	public LinePlotView(Context context) {
		this(context, null, 0);
	}

	public LinePlotView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinePlotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialPaint();
		map = AnalysisTool.getDataMapTest(0);
		gestureDetector = new GestureDetector(context, new MyOnGestureListener());
		this.setLongClickable(true);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	private void initialLength() {
		widthOfView = getWidth();
		heightOfView = getHeight();

		cellHeight = contentRect.height()/12;
		cellWidth = contentRect.width()/7;
	}

	private void initialPaint(){
		gridPaint = new Paint();
		labelPaint = new Paint();
		dataLinePaint = new Paint();
		framePaint = new Paint();

		gridPaint.setColor(getResources().getColor(R.color.grid_line_color));
		gridPaint.setStyle(Paint.Style.STROKE);
		gridPaint.setStrokeWidth(1f);

		labelPaint.setColor(getResources().getColor(R.color.label_color));
		labelPaint.setTextAlign(Paint.Align.CENTER);
		labelPaint.setTextSize(Util.sp2px(13));

		mLabelHeight = (int) Math.abs(labelPaint.getFontMetrics().top);
		mLabelWidth = Util.dip2px(18);
		mLabelSeparation = 2;

		dataLinePaint.setColor(getResources().getColor(R.color.data_line_color));

		framePaint.setColor(getResources().getColor(R.color.coral));
		framePaint.setStyle(Paint.Style.STROKE);
		framePaint.setStrokeWidth(3f);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initialLength();

		drawVerticalLabel(canvas, labelPaint);
		drawHorizontalLabel(canvas, labelPaint);
		canvas.drawRect(contentRect, framePaint);

		canvas.clipRect(contentRect);
//		drawDataLine(canvas, labelPaint);
		drawBackgroundGrid(canvas, gridPaint);
		canvas.restore();

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		contentRect.set(
				mLabelWidth + mLabelSeparation + getPaddingLeft(),
				getPaddingTop(),
				getWidth() - getPaddingRight(),
				getHeight() - mLabelHeight - mLabelSeparation - getPaddingBottom());
	}

	private void drawBackgroundGrid(Canvas canvas, Paint paint){
		float y = contentRect.top;
		for (int i=0; i<=11; i++){
			canvas.drawLine(contentRect.left, y, contentRect.right, y, paint);
			y += cellHeight;
		}
		float x = contentRect.left - scrollOffSet + cellWidth * (int) (scrollOffSet/cellWidth);
		for (int i=0; i<=7; i++){
			canvas.drawLine(x, contentRect.top, x, contentRect.bottom, paint);
			x += cellWidth;
		}
	}

	private void drawVerticalLabel(Canvas canvas, Paint paint){
		float height = 10f + contentRect.top;
		for (int i=0; i<=6; i++){
			canvas.drawText(String.valueOf(24-(i*4)),
					mLabelWidth/2,
					height,
					paint);
			height += cellHeight*2;
		}
	}

	private void drawHorizontalLabel(Canvas canvas, Paint paint){
		int scrollCellWidthNum = (int)(scrollOffSet / cellWidth);
		float width = contentRect.left - scrollOffSet + cellWidth*scrollCellWidthNum;
		for (int i=0; i < 7; i++){
			canvas.drawText(CalendarTool.getDateFromToday(i + scrollCellWidthNum).substring(8),
					width ,
					getHeight(),
					paint);
			width += cellWidth;
		}
	}

/*	private void drawDataLine(Canvas canvas, Paint paint){
		int scrollCellWidthNum = (int)(scrollOffSet / cellWidth);
		int newKey, oldKey;
		float newX, oldX;
		for (int i=0; i <= 8; i++){
			newKey = CalendarTool.getDateIntervalFromBase(CalendarTool.getDateFromToday(i + scrollCellWidthNum));
			oldKey = CalendarTool.getDateIntervalFromBase(CalendarTool.getDateFromToday(i-1 + scrollCellWidthNum));
			newX = gridLinePaddintLeft + i*cellWidth - scrollOffSet + cellWidth*scrollCellWidthNum;
			oldX = gridLinePaddintLeft + (i-1)*cellWidth - scrollOffSet + cellWidth*scrollCellWidthNum;
			if (!map.containsKey(newKey)){
				map.put(newKey,new Random().nextInt(24*60));
			}
			if (!map.containsKey(oldKey)){
				map.put(oldKey,new Random().nextInt(24*60));
			}


			canvas.drawLine(newX,
					gridLinePaddintTop + map.get(newKey)/120f * cellHeight,
					oldX,
					gridLinePaddintTop + map.get(oldKey)/120f * cellHeight,
					paint);

		}


	}*/


	public void setIndex(int index){
		this.index = index;
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {

	}

	class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			DebugConfig.log("onSingleTapUp has been called");
			return super.onSingleTapUp(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			scrollOffSet = scrollOffSet + distanceX;
//			DebugConfig.log("onScroll has been called " + scrollOffSet);
			invalidate();
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//			DebugConfig.log("onFling has been called");
//			scrollOffSet = scrollOffSet + velocityX;
//			return super.onFling(e1, e2, velocityX, velocityY);
//		}
	}
}
