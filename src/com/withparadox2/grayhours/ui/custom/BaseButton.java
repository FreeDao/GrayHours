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
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TableRow;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by Administrator on 14-2-28.
 */
public class BaseButton extends Button implements ValueAnimator.AnimatorUpdateListener{
	protected boolean ACTION_DOWN = false;
	private boolean clickOnce = false;
	private String stokeColor;
	private String fillColor;

	public BaseButton(Context context, String strokeColor, String fillColor) {
		super(context);
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
			TableRow.LayoutParams.WRAP_CONTENT,
			TableRow.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		this.setBackgroundDrawable(null);
		this.setLayoutParams(layoutParams);
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
			setMeasuredDimension(Util.getScreenWidth()/2,Util.getScreenHeigth()/2);
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
		super.onDraw(canvas);
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
				if (!clickOnce){
					startAnimation(0f);
				} else {
					startAnimation(1f);
				}
				clickOnce = !clickOnce;
				DebugConfig.log("action up");


				break;
		}
		return super.onTouchEvent(event);
	}



	private void startAnimation(float startPoint){
		ValueAnimator animator = ObjectAnimator.ofFloat(this, "ScaleX", 0, 3);
		animator.setDuration(2000);
		animator.addUpdateListener(this);
		animator.start();
	}


	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		float value = (Float)animation.getAnimatedValue();
//		DebugConfig.log("animation is called");
//		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
//			(int) (Util.getScreenWidth() * (1 + value) / 2),
//			(int) (Util.getScreenHeigth() * (1 + value) / 2));
//		layoutParams.gravity = Gravity.CENTER;
//		setLayoutParams(layoutParams);
//		requestLayout();
		invalidate();
	}
}
