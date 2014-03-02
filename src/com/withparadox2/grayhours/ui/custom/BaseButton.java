package com.withparadox2.grayhours.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by withparadox2 on 14-2-28.
 */
public class BaseButton extends Button{
	protected boolean ACTION_DOWN = false;
	private int index;
	private String timeText = "";//if not set to empty,gh will crash in addbutton
	protected String baseColor;

	protected Paint paint;


	public BaseButton(Context context, String color) {
		super(context);
		this.setBackgroundDrawable(null);
		this.baseColor = color;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	public BaseButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected Paint getCircleStrokePaint(){
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(Color.parseColor(baseColor));
		return paint;
	}

	protected Paint getCircleFilledPaint(){
		paint.setAlpha(50);
		paint.setStyle(Paint.Style.FILL);
		return paint;
	}

	private Paint getTextPaint(){
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(40);
		paint.setColor(Color.DKGRAY);
		paint.setTextAlign(Paint.Align.CENTER);
		return paint;
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
		canvas.drawCircle(width/2, height/2, r, getCircleStrokePaint());
		if (ACTION_DOWN){
			canvas.drawCircle(width/2, height/2, r, getCircleFilledPaint());
		}
		canvas.drawText(getText().toString(), width / 2, height / 2, getTextPaint());
		canvas.drawText(getTimeText(), width / 2, height / 2 + 50, getTextPaint());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				ACTION_DOWN = true;
				invalidate();
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

	public void setTimeText(String timeText){
		this.timeText = timeText;
		invalidate();
	}

	public String getTimeText(){
		return timeText;
	}
}
