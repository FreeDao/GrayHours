package com.withparadox2.grayhours.ui.analysis.githubview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.*;
import android.widget.Scroller;
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.support.CalendarTool;
import com.withparadox2.grayhours.ui.AnalysisFragment;
import com.withparadox2.grayhours.ui.analysis.LinePlotView;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by withparadox2 on 14-4-16.
 */
public class GithubView extends ViewGroup {
	private int cellSize = 0;
	private Queue<ColumnView> columnViewCacheList = new LinkedList<ColumnView>();
	private int maxColumnNum = -1;
	private ColumnView columnView;
	private GestureDetector gestureDetector;
	private float scrollOffSet = 0f;
	private ColumnView convertView;
	private Scroller mScroller;
	private Context context;
	private int currentColumnPosition = 0; //using position to identify each column then each cell
	public static int currentSelectedCell = 0;


	private int addOrMinusScrollOffSet;// the offset from scrollOffSet because of adding or removing view
	private Map<Integer, Integer> map;
	private boolean dataAvaiable = false;

	private LinePlotView linePlotView;

	public GithubView(Context context) {
		this(context, null, 0);
	}

	public GithubView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GithubView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		gestureDetector = new GestureDetector(new MyGestureListener());
		this.setLongClickable(true);
		mScroller = new Scroller(context);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mScroller.forceFinished(true);

				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int height = b - t;
		for (int c=0, childNum = getChildCount(); c<childNum; c++){
			int x = (int) (c*cellSize - scrollOffSet - addOrMinusScrollOffSet);
			getChildAt(c).layout(x, 0, x + cellSize, height);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		cellSize =(height - getPaddingTop() - getPaddingBottom())/7;
		int heightMeasure = cellSize*7;
		int childWidhtMeasureSpec = MeasureSpec.makeMeasureSpec(cellSize, MeasureSpec.EXACTLY);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasure, MeasureSpec.EXACTLY);
		initialView(width);
		for (int c=0, childNum = getChildCount(); c<childNum; c++){
			getChildAt(c).measure(childWidhtMeasureSpec, childHeightMeasureSpec);
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

	private void initialView(int width){
		if(getChildCount() == 0){
			int maxColumnNum = width/cellSize + 1;
			for (int c=0; c<maxColumnNum; c++){
				columnView = new ColumnView(context);
				columnView.setPosition(-c);
				addView(columnView);
			}
			currentColumnPosition = 0;
			CellView.selectedPositin = AnalysisTool.TODAY_INDEX;
		}
	}

	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			mScroller.forceFinished(true);
			scrollOffSet += distanceX;
			updateView();
			requestLayout();
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			fling((int) -velocityX, (int) -velocityY);
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			int pos = currentColumnPosition -  (int) (e.getX()+scrollOffSet + addOrMinusScrollOffSet)/cellSize;
			int ind = (int)(e.getY()/cellSize);
			CellView.selectedPositin = pos*7+ind;
			setDateText(pos, ind);
			linePlotView.scrollToTargetCell(pos * 7 - ind + AnalysisTool.TODAY_INDEX);
			invalidateAll();

			return true;
		}
	}

	private void invalidateAll(){
		ViewGroup tempCol;
		View tempCell;
		for (int i=0, num=getChildCount(); i<num; i++){
			tempCol = (ViewGroup) getChildAt(i);
			for (int j=0; j<7; j++){
				tempCell = tempCol.getChildAt(j);
				tempCell.invalidate();
			}
		}
	}

	private void updateView(){
		int num = getChildCount();
		if (getChildAt(0).getRight() < 0) {
			columnViewCacheList.offer((ColumnView) getChildAt(0));
			removeViewAt(0);
			num--;
			addOrMinusScrollOffSet -= cellSize;
			currentColumnPosition--;
		}

		if (getChildAt(num-1).getLeft() > getWidth()){
			columnViewCacheList.offer((ColumnView) getChildAt(num - 1));
			removeViewAt(num-1);
			num--;
		}

		// shouldn't use while(getChildAt(0).getLeft()>=0), because getLeft() gives the value of previous time
		// while here we have computed a new scrollOffSet, so we need to catch up with it by updating addOrMinusScrollOffSet
		while(- addOrMinusScrollOffSet - scrollOffSet >= 0){
			convertView = columnViewCacheList.poll();
			if (convertView == null){
				convertView = new ColumnView(context);
			}
			currentColumnPosition++;
			convertView.setPosition(currentColumnPosition);
			addView(convertView, 0);
			addOrMinusScrollOffSet += cellSize;
			convertView = null;
			num++;
		}

		int temp = (int) (- addOrMinusScrollOffSet - scrollOffSet + num*cellSize);
		while (temp <= getWidth()){
			convertView = columnViewCacheList.poll();
			if (convertView == null){
				convertView = new ColumnView(context);
			}
			convertView.setPosition(currentColumnPosition-num);
			addView(convertView);
			temp += cellSize;
			convertView = null;
			num++;
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
				velocityX * 2 / 3,
				velocityY,
				-100000, 100000,
				0, 0);
		requestLayout();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollOffSet = mScroller.getCurrX();
			updateView();
			requestLayout();
		}
	}

	public void setData(){
		dataAvaiable = true;
		invalidateAll();
	}

	public void setDateText(int position, int index){


		String dateBase = CalendarTool.getDateFromToday(-position * 7 + index - AnalysisTool.TODAY_INDEX);
		int consumeTime = 0;
		try {
			consumeTime = AnalysisFragment.map.get(CalendarTool.getDateIntervalFromBase(dateBase));
		} catch (NullPointerException e) {
		}
		String dateText = dateBase +"    " + Util.convertMinutesToHours(consumeTime);
		updateDateTextListener.setDateText(dateText);
	}

	private UpdateDateTextListener updateDateTextListener;

	public interface UpdateDateTextListener{
		public void setDateText(String text);
	}

	public void setUpdateDateTextListener(UpdateDateTextListener listener){
		this.updateDateTextListener = listener;
	}

	public void setLinePlotView(LinePlotView linePlotView) {
		this.linePlotView = linePlotView;
	}

//	public void scrollToTarget(int scrollCellsPara){
//		int ind = 7-(scrollCellsPara - AnalysisTool.TODAY_INDEX)%7;
//		int pos = (scrollCellsPara - AnalysisTool.TODAY_INDEX)/7+1;
//		CellView.selectedPositin = pos*7+ind;
//		setDateText(pos, ind);
//		if (currentColumnPosition <= pos && pos<= currentColumnPosition+getChildCount()){
//			invalidateAll();
//		} else {
//			mScroller.startScroll((int)scrollOffSet, 0, (currentColumnPosition - pos)*cellSize, 0, 100);
//			requestLayout();
//		}
//	}

}
