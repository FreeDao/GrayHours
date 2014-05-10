package com.withparadox2.grayhours.ui.analysis;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.support.CalendarTool;
import com.withparadox2.grayhours.support.LoadData;
import com.withparadox2.grayhours.ui.analysis.githubview.GithubView;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

import java.util.Map;

/**
 * Created by withparadox2 on 14-3-3.
 */
public class LinePlotView extends View {
	private Context context;
	private Paint gridPaint;
	private Paint labelPaint;
	private Paint dataLinePaint;
	private Paint framePaint;
	private Paint coverColumnPaint;

	private Scroller mScroller;
	private int mLabelSeparation;
	private int mLabelWidth;
	private int mLabelHeight;


	private float widthOfView;
	private float heightOfView;

	private float cellWidth;
	private float cellHeight;

	private float scrollOffSetX = 0f;
	private float scrollOffSetY = 0f;
	private float initiaGridlOffset; //force grid align one line to the center of Rect

	private int index = 0;
	private Map<Integer, Integer> map;

	private GestureDetector gestureDetector;

	private Rect contentRect = new Rect();


	private boolean dataAvaiable = false;

	private boolean changeMaxHours = false;
	private int maxHours = 12;
	private int tempMaxHours = 100;

	private int intervalHours = 2;
	private int currentCellIndex = 0;

	private GithubView githubView;

	private LoadData loadData;

	public LinePlotView(Context context) {
		this(context, null, 0);
	}

	public LinePlotView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LinePlotView(final Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initialPaint();
		mScroller = new Scroller(context);
		gestureDetector = new GestureDetector(context, new MyOnGestureListener());
		this.setLongClickable(true);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mScroller.forceFinished(true);
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if (event.getX() < contentRect.left && event.getY() > contentRect.top){
						scrollOffSetY = 0;
						changeMaxHours = true;
					} else {
						changeMaxHours = false;
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP && changeMaxHours){
					AnalysisTool.setSharePreferenceInt(context, String.valueOf(index), maxHours);
				}
				return gestureDetector.onTouchEvent(event);
			}
		});

	}




	private void initialLength() {
		widthOfView = getWidth();
		heightOfView = getHeight();

		cellHeight = contentRect.height()/(float)(maxHours/intervalHours);
		cellWidth = contentRect.width()/7f;

		initiaGridlOffset = -cellWidth/2;

	}

	private void initialPaint(){
		gridPaint = new Paint();
		labelPaint = new Paint();
		dataLinePaint = new Paint();
		framePaint = new Paint();
		coverColumnPaint = new Paint();

		gridPaint.setColor(getResources().getColor(R.color.grid_line_color));
		gridPaint.setStyle(Paint.Style.STROKE);
		gridPaint.setStrokeWidth(1f);

		labelPaint.setColor(getResources().getColor(R.color.github_text));
		labelPaint.setTextAlign(Paint.Align.CENTER);
		labelPaint.setTextSize(Util.sp2px(13));

		mLabelHeight = (int) Math.abs(labelPaint.getFontMetrics().top);
		mLabelWidth = Util.dip2px(18);
		mLabelSeparation = 2;

		dataLinePaint.setColor(getResources().getColor(R.color.data_line_color));

		framePaint.setColor(getResources().getColor(R.color.coral));
		framePaint.setStyle(Paint.Style.STROKE);
		framePaint.setStrokeWidth(3f);

		coverColumnPaint.setColor(getResources().getColor(R.color.teal));
		coverColumnPaint.setAlpha(200);
		coverColumnPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initialLength();

		drawVerticalLabel(canvas, labelPaint);
		drawHorizontalLabel(canvas, labelPaint);
		canvas.drawRect(contentRect, framePaint);
		canvas.clipRect(contentRect);
		if(dataAvaiable){
			drawDataLine(canvas, labelPaint);
		}
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
		for (int i=0, n=2*maxHours/intervalHours; i<=n; i++){
			canvas.drawLine(contentRect.left, y, contentRect.right, y, paint);
			y += cellHeight/2f;
		}
		int scrollCellWidthNum = (int)(scrollOffSetX / cellWidth);
		float x = (float)contentRect.left - scrollOffSetX + cellWidth*scrollCellWidthNum + initiaGridlOffset;
		for (int i=0; i<=8; i++){
			canvas.drawLine(x, contentRect.top, x, contentRect.bottom, paint);
			x += cellWidth;
		}
	}

	private void drawVerticalLabel(Canvas canvas, Paint paint){
		float height = 10f + contentRect.top;
		for (int i=0,n=maxHours/intervalHours; i<=n; i++){
			canvas.drawText(String.valueOf(maxHours - i*intervalHours),
					mLabelWidth/2,
					height,
					paint);
			height += cellHeight;
		}
	}

	private void drawHorizontalLabel(Canvas canvas, Paint paint){
		int scrollCellWidthNum = (int)(scrollOffSetX / cellWidth);
		float x = contentRect.right - scrollOffSetX + cellWidth*(scrollCellWidthNum+1) + initiaGridlOffset;
		for (int i=0; i <= 8; i++){
			canvas.drawText(CalendarTool.getDateFromToday(4 - i + scrollCellWidthNum).substring(8),
					x ,
					getHeight(),
					paint);
			x -= cellWidth;
		}
	}

	private void drawDataLine(Canvas canvas, Paint paint){
		int scrollCellWidthNum = (int)(scrollOffSetX / cellWidth);
		float x = contentRect.right - scrollOffSetX + cellWidth*(scrollCellWidthNum+1) - initiaGridlOffset;
		Integer timeOld;
		float heightNew = contentRect.bottom, heightOld, r;

		for (int i=0; i <= 10; i++){
			timeOld = map.get(4-i + scrollCellWidthNum);
			if (timeOld == null){
				timeOld = 0;
			}
			heightOld = contentRect.bottom - timeOld /intervalHours/60f * cellHeight;

			r = (Math.abs(x-contentRect.centerX()-cellWidth) > cellWidth/2) ? 5 : 10;
			canvas.drawCircle(x - cellWidth, heightOld, r, coverColumnPaint);
			canvas.drawLine(x, heightNew, x - cellWidth, heightOld, paint);
			heightNew = heightOld;
			x -= cellWidth;
		}
	}


	public void setIndex(int index){
		this.index = index;
	}

	public void setData(Map map) {
		this.map = map;
		dataAvaiable = true;
		maxHours = AnalysisTool.getSharePreference(context, String.valueOf(index));
		setIntervalHours(maxHours);
		DebugConfig.log("maxHour is :%d, index is:%d", maxHours, index);
		invalidate();

	}

	class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener{

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return super.onSingleTapUp(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			mScroller.forceFinished(true);
			if(!changeMaxHours){
				scrollOffSetX = scrollOffSetX + distanceX;
				scrollGithubView();
			} else {
				if(distanceY*scrollOffSetY < 0){
					scrollOffSetY =0;
				}
				scrollOffSetY = scrollOffSetY + distanceY;
				setChangeMaxHours();
			}
			ViewCompat.postInvalidateOnAnimation(LinePlotView.this);
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			fromLineToGit = true;
			if(!changeMaxHours)
				fling((int) -velocityX, (int) -velocityY);
			return true;
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

	private void fling(int velocityX, int velocityY) {

		// Before flinging, aborts the current animation.
		mScroller.forceFinished(true);
		// Begins the animation
		mScroller.fling(
				// Current scroll position
				(int) scrollOffSetX,
				0,
				velocityX*2/3,
				velocityY,
				Integer.MIN_VALUE, Integer.MAX_VALUE,
				0, 0);
		float ds = mScroller.getFinalX();
		int scrollCellWidthNum = (int)(ds / cellWidth);
		mScroller.setFinalX((int) (cellWidth*scrollCellWidthNum));
		// Invalidates to trigger computeScroll()
		ViewCompat.postInvalidateOnAnimation(this);
	}

	private boolean fromLineToGit = false;
	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollOffSetX = mScroller.getCurrX();
			if(fromLineToGit) {
				scrollGithubView();
			}
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private void scrollGithubView(){
		if (scrollOffSetX < 0){
			githubView.scrollToTarget((int)(-(scrollOffSetX+initiaGridlOffset)/cellWidth));
		} else {
			githubView.scrollToTarget((int)(-(scrollOffSetX-initiaGridlOffset)/cellWidth));
		}
	}

	public void scrollToTargetCell(int scrollCellNums){
		fromLineToGit = false;
		if (scrollOffSetX > 0){
			currentCellIndex = -(int)((scrollOffSetX + 10) / cellWidth);
		} else {
			currentCellIndex = -(int)((scrollOffSetX - 10) / cellWidth);
		}

		mScroller.startScroll((int)scrollOffSetX, 0, (int)((currentCellIndex-scrollCellNums)*cellWidth), 0, 100);
		ViewCompat.postInvalidateOnAnimation(this);

	}

	public void setGithubView(GithubView githubView){
		this.githubView = githubView;
	}
}
