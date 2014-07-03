package com.withparadox2.grayhours.ui.analysis.lineview;

import android.content.Context;
import android.util.AttributeSet;
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.ui.analysis.githubview.CellView;
import com.withparadox2.grayhours.ui.analysis.hzlistview.HorizontalListView;
import com.withparadox2.grayhours.utils.DebugConfig;

import java.util.Map;

/**
 * Created by Administrator on 14-7-2.
 */
public class ChartView extends HorizontalListView{
	private Context context;
	public ChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		addViews();

	}

	public ChartView(Context context) {
		this(context, null, 0);
	}

	public ChartView(Context context, AttributeSet attrs) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int widthMeasure =(width - getPaddingLeft() - getPaddingRight())/7;
		int heightMeasure = height - getPaddingBottom() - getPaddingTop();
		int childWidhtMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasure, MeasureSpec.EXACTLY);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasure, MeasureSpec.EXACTLY);
		for (int c=0, childNum = getChildCount(); c<childNum; c++){
			getChildAt(c).measure(childWidhtMeasureSpec, childHeightMeasureSpec);
		}
		setMeasuredDimension(width, height);
	}

	private void addViews(){
		for(int i=0; i<7; i++){
			this.addView(new ChartColumnView(context, i));
		}
	}

	private void updateChildViews(){
		for(int i=0; i<getChildCount(); i++){
			getChildAt(i).invalidate();
		}
	}
}
