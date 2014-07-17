package com.withparadox2.grayhours.ui.analysis.githubview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Scroller;

import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.support.CalendarTool;
import com.withparadox2.grayhours.ui.AnalysisFragment;
import com.withparadox2.grayhours.ui.analysis.hzlistview.HorizontalListView;
import com.withparadox2.grayhours.ui.analysis.lineview.ChartView;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

import java.util.Map;

/**
 * Created by withparadox2 on 14-4-16.
 */
public class GithubView extends HorizontalListView {
	private int maxColumnNum = -1;
	private GitColumnView gitColumnView;
	private GestureDetector gestureDetector;
	private GitColumnView convertView;
	private Scroller mScroller;
	private Context context;
	public static int cellPosition = 0;
	public static int cellIndex = 0;


	private Map<Integer, Integer> map;
	private boolean dataAvaiable = false;

	private ChartView chartView;

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
		cellIndex = AnalysisTool.TODAY_INDEX;
		cellPosition = 0;
		this.setAdapter(new MyAdapter());
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mScroller.forceFinished(true);
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		cellSize =(height - getPaddingTop() - getPaddingBottom())/7;
		int heightMeasure = cellSize*7;
		int childWidhtMeasureSpec = MeasureSpec.makeMeasureSpec(cellSize, MeasureSpec.EXACTLY);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasure, MeasureSpec.EXACTLY);
		initialView(width, cellSize);
		CellView.selectedPositin = AnalysisTool.TODAY_INDEX;
		for (int c=0, childNum = getChildCount(); c<childNum; c++){
			getChildAt(c).measure(childWidhtMeasureSpec, childHeightMeasureSpec);
		}
		setMeasuredDimension(width, height);
	}

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GitColumnView gitColumnView;
			if (convertView != null){
				gitColumnView = (GitColumnView) convertView;
			} else {
				gitColumnView = new GitColumnView(context);
			}
			gitColumnView.setPosition(position);
			return gitColumnView;
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
			cellPosition = pos;
			cellIndex = ind;
			chartView.scrollToTargetCell(pos * 7 - ind + AnalysisTool.TODAY_INDEX);
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
				Integer.MIN_VALUE, Integer.MAX_VALUE,
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
		setDateText(cellPosition, cellIndex);
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

	public void setLinePlotView(ChartView chartView) {
		this.chartView = chartView;
	}

	public void scrollToTarget(int scrollCellsPara){
		updateView();
		int ind,pos;
		int temp = AnalysisTool.TODAY_INDEX - scrollCellsPara;
		DebugConfig.log("todyindex:%d, scrollCellsPara is:%d", AnalysisTool.TODAY_INDEX ,scrollCellsPara);
		if (scrollCellsPara <= 0) {
			if (temp <= 6){
				pos = 0;
				ind = temp;
			} else {
				ind = temp%7;
				pos = -temp/7;
			}
		} else {
			if (temp >= 0) {
				pos = 0;
				ind = temp;
			} else {
				ind = 7-(scrollCellsPara - AnalysisTool.TODAY_INDEX)%7;
				if (ind == 7){
					ind = 0;
					pos = (scrollCellsPara - AnalysisTool.TODAY_INDEX)/7;
				} else {
					pos = (scrollCellsPara - AnalysisTool.TODAY_INDEX)/7 + 1;
				}
			}
		}
		cellIndex = ind;
		cellPosition = pos;
		CellView.selectedPositin = pos*7+ind;
		setDateText(pos, ind);
		mScroller.forceFinished(true);

		if (currentColumnPosition-2 < pos) {
			mScroller.startScroll((int) scrollOffSet, 0, -2 * cellSize, 0, 1);
		} else if(currentColumnPosition - getChildCount()+2 > pos) {
			mScroller.startScroll((int)scrollOffSet, 0, 2*cellSize, 0, 1);
	
		}
		invalidate();//can not be removed, but why??? how about requestLayout????
		invalidateAll();
	}

}
