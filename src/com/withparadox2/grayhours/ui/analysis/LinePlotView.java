package com.withparadox2.grayhours.ui.analysis;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.LoaderManager;
import android.content.Context;
import android.graphics.*;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;
import android.widget.Scroller;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.support.CalendarTool;
import com.withparadox2.grayhours.support.LoadData;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

import java.util.Map;
import java.util.Random;

/**
 * Created by withparadox2 on 14-3-3.
 */
public class LinePlotView extends View implements LoadData.LoadFinishedCallback{
	private Paint gridPaint;
	private Paint labelPaint;
	private Paint dataLinePaint;
	private Paint framePaint;

	private Scroller mScroller;
	private int mLabelSeparation;
	private int mLabelWidth;
	private int mLabelHeight;


	private float widthOfView;
	private float heightOfView;

	private float cellWidth;
	private float cellHeight;

	private float scrollOffSet = 0f;
	private float initiaGridlOffset; //force grid align one line to the center of Rect

	private int index = 0;
	private Map<Integer, Integer> map;

	private GestureDetector gestureDetector;

	private Rect contentRect = new Rect();

	private float flingStartX;

	private String dateText;

	private boolean dataAvaiable = false;

	public LinePlotView(Context context) {
		this(context, null, 0);
	}

	public LinePlotView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinePlotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialPaint();
		mScroller = new Scroller(context);
		gestureDetector = new GestureDetector(context, new MyOnGestureListener());
		this.setLongClickable(true);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mScroller.forceFinished(true);
				return gestureDetector.onTouchEvent(event);
			}
		});
		LoadData loadData = new LoadData(this);
		loadData.execute(index);
	}

	private void initialLength() {
		widthOfView = getWidth();
		heightOfView = getHeight();

		cellHeight = contentRect.height()/12f;
		cellWidth = contentRect.width()/7f;

		initiaGridlOffset = cellWidth/2;

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
		canvas.drawLine(contentRect.centerX(), contentRect.top,
						contentRect.centerX(), contentRect.bottom, framePaint);

		canvas.clipRect(contentRect);
		if(dataAvaiable){
			drawDataLine(canvas, labelPaint);
		}
		drawBackgroundGrid(canvas, gridPaint);
		canvas.restore();
		if(dataAvaiable)
			canvas.drawText(dateText, contentRect.centerX(), contentRect.top-20, labelPaint);


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
		for (int i=0; i<=12; i++){
			canvas.drawLine(contentRect.left, y, contentRect.right, y, paint);
			y += cellHeight;
		}
		int scrollCellWidthNum = (int)(scrollOffSet / cellWidth);
		float x = (float)contentRect.left - scrollOffSet + cellWidth*scrollCellWidthNum + initiaGridlOffset;
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
		float x = contentRect.right - scrollOffSet + cellWidth*scrollCellWidthNum + initiaGridlOffset;
		for (int i=0; i <= 7; i++){
			canvas.drawText(CalendarTool.getDateFromToday(3 - i + scrollCellWidthNum).substring(8),
					x ,
					getHeight(),
					paint);
			x -= cellWidth;
		}
	}

	private void drawDataLine(Canvas canvas, Paint paint){
		int scrollCellWidthNum = (int)(scrollOffSet / cellWidth);
		float x = contentRect.right - scrollOffSet + cellWidth*scrollCellWidthNum + initiaGridlOffset;
		dateText = CalendarTool.getDateFromToday(scrollCellWidthNum);
		Integer timeNew, timeOld;
		timeNew = map.get(CalendarTool.getDateIntervalFromBase(CalendarTool.getDateFromToday(3 + scrollCellWidthNum)));

		for (int i=0; i <= 8; i++){
			timeOld = map.get( CalendarTool.getDateIntervalFromBase(CalendarTool.getDateFromToday(2-i + scrollCellWidthNum)));
			if(timeNew != null && timeOld != null){
				canvas.drawLine(x,
						contentRect.bottom - timeNew / 120f * cellHeight,
						x - cellWidth,
						contentRect.bottom - timeOld / 120f * cellHeight,
						paint);
			}
			timeNew = timeOld;
			x -= cellWidth;
		}
	}


	public void setIndex(int index){
		this.index = index;
	}

	@Override
	public void loadFinishedCallback(Map map) {
		this.map = map;
		dataAvaiable = true;
		DebugConfig.log("the length of map is %d", map.size());
		invalidate();
	}


	class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener{

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			DebugConfig.log("onSingleTapUp has been called");

			return super.onSingleTapUp(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			mScroller.forceFinished(true);
			scrollOffSet = scrollOffSet + distanceX;
			ViewCompat.postInvalidateOnAnimation(LinePlotView.this);
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			flingStartX = e2.getX();
			fling((int) -velocityX, (int) -velocityY);
			return true;
		}
	}

	private void fling(int velocityX, int velocityY) {

		// Before flinging, aborts the current animation.
		mScroller.forceFinished(true);
		// Begins the animation
		mScroller.fling(
				// Current scroll position
				(int) scrollOffSet,
				0,
				velocityX,
				velocityY,
				(int) (2-map.size()*cellWidth), 0,
				0, 0);
		// Invalidates to trigger computeScroll()
		ViewCompat.postInvalidateOnAnimation(this);
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollOffSet = mScroller.getCurrX();
			ViewCompat.postInvalidateOnAnimation(this);
		}
//		DebugConfig.log("onFling has been called " +  mScroller.getCurrX());
	}
}
