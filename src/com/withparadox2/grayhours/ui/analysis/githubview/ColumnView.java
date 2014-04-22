package com.withparadox2.grayhours.ui.analysis.githubview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.utils.DebugConfig;

/**
 * Created by withparadox2 on 14-4-21.
 */
public class ColumnView extends ViewGroup{
	private int position;
	private CellView tempView;

	public ColumnView(Context context) {
		this(context, null, 0);
	}

	public ColumnView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColumnView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addChildViews(context);
	}

	private void addChildViews(Context context){
		for (int i=0; i<7; i++){
			tempView = new CellView(context, i);
			addView(tempView);
		}
	}

	public void setPosition(int position){
		this.position = position;
		for (int i=0, num=getChildCount(); i<num; i++){
			((CellView)getChildAt(i)).setPosition(position);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int childMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
			getChildAt(c).measure(childMeasureSpec, childMeasureSpec);
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int cellSize = r - l;
		for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
			final View child = getChildAt(c);
			child.layout(0, c*cellSize, cellSize, (c + 1) * cellSize);
		}
	}
}
