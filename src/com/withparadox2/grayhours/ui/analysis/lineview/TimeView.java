package com.withparadox2.grayhours.ui.analysis.lineview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by withparadox2 on 14-7-2.
 */
public class TimeView extends View{
	private int maxHours = 8,tempMaxHours;
	private int intervalHours=2;
	private float scrollOffSetY;
	private Scroller mScroller;
	private GestureDetector gestureDetector;
	private float mLabelWidth;
	private float cellHeight;
	private Paint drawTimePaint;
	private Paint drawBgPaint;

	private int paddingTop = 20;
	private int paddingBottom = Util.dip2px(20);

	private boolean onTouch = false;
	public static SetMaxTimeCallBack callBack;

	public TimeView(Context context) {
		this(context, null, 0);
	}

	public TimeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}


	public TimeView(final Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
		gestureDetector = new GestureDetector(context, new MyOnGestureListener());
		intitial();
		this.setLongClickable(true);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					onTouch = true;
					scrollOffSetY = 0;
				} else if (event.getAction() == MotionEvent.ACTION_UP){
					onTouch = false;
					AnalysisTool.setSharePreferenceInt(context, String.valueOf(0), maxHours);
				}
				invalidate();
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

    public static void setCallBack(SetMaxTimeCallBack callBack1){
        callBack = callBack1;
    }

	private void intitial(){
		drawTimePaint = new Paint();
		drawTimePaint.setColor(getResources().getColor(R.color.red));
		drawTimePaint.setTextSize(Util.sp2px(15));
		drawTimePaint.setTextAlign(Paint.Align.CENTER);

		drawBgPaint = new Paint();
		drawBgPaint.setColor(getResources().getColor(R.color.lightseagreen));
		drawBgPaint.setAntiAlias(true);
		drawBgPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		mLabelWidth = getMeasuredWidth();
		cellHeight = (getMeasuredHeight()-paddingBottom-paddingTop)*intervalHours/maxHours;
		if(onTouch){
			canvas.drawCircle(mLabelWidth/2, paddingTop, mLabelWidth/2, drawBgPaint);
			canvas.drawRect(0, paddingTop, mLabelWidth, getMeasuredHeight()-paddingBottom,drawBgPaint);
			canvas.drawCircle(mLabelWidth/2, getMeasuredHeight()-paddingBottom, mLabelWidth/2, drawBgPaint);
		}
		drawVerticalLabel(canvas, drawTimePaint);

	}

	private void drawVerticalLabel(Canvas canvas, Paint paint){
		float height = paddingTop+10;
		for (int i=0,n=maxHours/intervalHours; i<=n; i++){
			canvas.drawText(String.valueOf(maxHours - i*intervalHours),
					mLabelWidth/2,
					height,
					paint);
			height += cellHeight;
		}
	}

	private void setChangeMaxHours() {
		int scrollHours = (int)(scrollOffSetY/50f);
		if(tempMaxHours != scrollHours){
			if (scrollOffSetY < 0){
				if (maxHours == 24) {
					maxHours = maxHours - 4;
				} else {
					maxHours = maxHours - 2;
				}
			} else {
				if (maxHours == 20) {
					maxHours = maxHours + 4;
				} else {
					maxHours = maxHours + 2;
				}
			}
			tempMaxHours = scrollHours;
		}
		if (maxHours > 24) {
			maxHours = 24;
		}
		if (maxHours < 4) {
			maxHours = 4;
		}
		setIntervalHours(maxHours);
	}

	private void setIntervalHours(int maxHours){
		switch (maxHours){
			case 24:
			case 20:
			case 16:
				intervalHours = 4;
				break;
			case 18:
				intervalHours = 3;
				break;
			case 14:
			case 12:
			case 10:
			case 8:
				intervalHours = 2;
				break;
			case 6:
			case 4:
				intervalHours = 1;
				break;
		}
	}

	class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener{

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				if(distanceY*scrollOffSetY < 0){
					scrollOffSetY =0;
				}
				scrollOffSetY = scrollOffSetY + distanceY;
				setChangeMaxHours();
				callBack.setTime(maxHours, intervalHours);
			return true;
		}
	}

	public interface SetMaxTimeCallBack{
		public void setTime(int maxTime, int timeInterval);
	}
}
