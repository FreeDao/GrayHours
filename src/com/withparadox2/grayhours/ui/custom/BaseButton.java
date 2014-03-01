package com.withparadox2.grayhours.ui.custom;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by withparadox2 on 14-2-28.
 */
public class BaseButton extends Button{
	protected boolean ACTION_DOWN = false;
	private boolean clickOnce = false;
	private String stokeColor;
	private String fillColor;
	private int index;
	private boolean animatingFlag = false;
	private String timeText="";

	public BaseButton(Context context, String strokeColor, String fillColor) {
		super(context);
//		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//			Util.getScreenWidth()/2,
//			Util.getScreenHeight()/2);
		this.setGravity(Gravity.CENTER);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		this.setBackgroundDrawable(null);
//		this.setLayoutParams(layoutParams);
		this.stokeColor = strokeColor;
		this.fillColor = fillColor;
	}

	public BaseButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY){
			setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
		} else {
			setMeasuredDimension(Util.getScreenWidth()/2,Util.getScreenHeight()/2);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float width =  getMeasuredWidth();
		float height = getMeasuredHeight();

		float r = width < height ? width*3/8 : height*3/8;
		Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeWidth(2);
		circlePaint.setColor(Color.parseColor(stokeColor));
		canvas.drawCircle(width/2, height/2, r, circlePaint);
		if (ACTION_DOWN){
			circlePaint.setColor(Color.parseColor(fillColor));
			circlePaint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(width/2, height/2, r, circlePaint);
		}
		circlePaint.setStyle(Paint.Style.FILL);
		circlePaint.setTextSize(40);
		circlePaint.setColor(Color.DKGRAY);
		circlePaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(getText().toString(), width / 2, height / 2, circlePaint);
		canvas.drawText(getTimeText(), width / 2, height / 2 + 50, circlePaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				int x = (int) event.getX();
				int y = (int) event.getY();
				int width =  getMeasuredWidth();
				int height = getMeasuredHeight();
				int r = width < height ? width*3/8 : height*3/8;
				if(((x-width/2)^2) + ((y-height/2)^2) < (r^2)){
					ACTION_DOWN = true;
					invalidate();
				} else {
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				ACTION_DOWN = false;
				invalidate();
				break;
		}
		return super.onTouchEvent(event);
	}



	public void setIndex(int index){
		this.index  = index;
	}

	public int getIndex(){
		return index;
	}

	public void setAnimatingFlag(boolean flag){
		animatingFlag = flag;
	}

	public boolean getAnimatingFlag(){
		return animatingFlag;
	}

	public void setTimeText(String timeText){
		this.timeText = timeText;
		invalidate();
	}

	public String getTimeText(){
		return timeText;
	}
}
