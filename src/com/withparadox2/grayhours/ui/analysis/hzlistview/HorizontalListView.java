package com.withparadox2.grayhours.ui.analysis.hzlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.withparadox2.grayhours.ui.analysis.githubview.GitColumnView;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 14-7-2.
 */
public class HorizontalListView extends ViewGroup{
	protected BaseAdapter myAdapter;
	protected int cellSize;
	protected float addOrMinusScrollOffSet, scrollOffSet;
	protected Queue<View> columnViewCacheList = new LinkedList<View>();
	protected int currentColumnPosition = 0;
	private Context context;

	public HorizontalListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public HorizontalListView(Context context) {
		this(context, null, 0);
	}

	public HorizontalListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}


	public void setAdapter(BaseAdapter baseAdapter){
		this.myAdapter = baseAdapter;
	}

	public void updateView(){
		int num = getChildCount();
		while (getChildAt(0).getRight() < 0) {
			columnViewCacheList.offer((View) getChildAt(0));

			removeViewAt(0);
			num--;
			addOrMinusScrollOffSet -= cellSize;
			currentColumnPosition--;
		}

		while (getChildAt(num-1).getLeft() > getWidth()){
			columnViewCacheList.offer((View) getChildAt(num - 1));
			removeViewAt(num-1);
			num--;
		}

		// shouldn't use while(getChildAt(0).getLeft()>=0), because getLeft() gives the value of previous time
		// while here we have computed a new scrollOffSet, so we need to catch up with it by updating addOrMinusScrollOffSet
		while(- addOrMinusScrollOffSet - scrollOffSet >= 0){
			currentColumnPosition++;
			addView(myAdapter.getView(currentColumnPosition, columnViewCacheList.poll(), null), 0);
			addOrMinusScrollOffSet += cellSize;
			num++;
		}

		int temp = (int) (- addOrMinusScrollOffSet - scrollOffSet + num* cellSize);
		while (temp <= getWidth()){
			addView(myAdapter.getView(currentColumnPosition-num, columnViewCacheList.poll(), null));
			temp += cellSize;
			num++;
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int height = b - t;
		int x = (int) (- scrollOffSet - addOrMinusScrollOffSet);
		for (int c=0, childNum = getChildCount(); c<childNum; c++){
			cellSize = getChildAt(0).getMeasuredWidth();
			getChildAt(c).layout(x, 0, x + cellSize, height);
			x += cellSize;
		}
	}

	protected void initialView(int width, int cellSize){
		if(getChildCount() == 0){
			int maxColumnNum = width/cellSize + 1;
			for (int c=0; c<maxColumnNum; c++){
				addView(myAdapter.getView(-c, columnViewCacheList.poll(), null));
			}
			currentColumnPosition = 0;
		}
	}



}
