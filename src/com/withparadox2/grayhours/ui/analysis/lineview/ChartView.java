package com.withparadox2.grayhours.ui.analysis.lineview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Scroller;

import com.withparadox2.grayhours.ui.analysis.githubview.GithubView;
import com.withparadox2.grayhours.ui.analysis.hzlistview.HorizontalListView;

/**
 * Created by Administrator on 14-7-2.
 */
public class ChartView extends HorizontalListView implements TimeView.SetMaxTimeCallBack{
    private Context context;
    private GestureDetector gestureDetector;
    private Scroller mScroller;
    public static int maxTime = 12;
    public static int timeInterval = 3;
    private GithubView githubView;
    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        gestureDetector = new GestureDetector(new MyGestureListener());
        this.setLongClickable(true);
        mScroller = new Scroller(context);
        this.setAdapter(new MyAdapter());
        TimeView.setCallBack(this);
        addViews();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScroller.forceFinished(true);
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    public ChartView(Context context) {
        this(context, null, 0);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMeasure =(width - getPaddingLeft() - getPaddingRight())/7;
        cellSize = widthMeasure;
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
            this.addView(myAdapter.getView(-i, columnViewCacheList.poll(), null));
        }
    }

    public void updateChildViews(){
        for(int i=0; i<getChildCount(); i++){
            getChildAt(i).invalidate();
        }
    }

    @Override
    public void setTime(int maxTime, int timeInterval) {
        ChartView.maxTime = maxTime;
        ChartView.timeInterval = timeInterval;
        updateChildViews();
    }

    private class MyAdapter extends BaseAdapter {

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
            ChartColumnView gitColumnView;
            if (convertView != null){
                gitColumnView = (ChartColumnView) convertView;
            } else {
                gitColumnView = new ChartColumnView(context);
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
            fromLineToGit = true;
            updateView();
            requestLayout();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            fromLineToGit = true;
            fling((int) -velocityX, (int) -velocityY);
            return true;
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
        float ds = mScroller.getFinalX();
        int scrollCellWidthNum = (int)(ds / cellSize);
        mScroller.setFinalX((int) (cellSize*scrollCellWidthNum));
        // Invalidates to trigger computeScroll()
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private boolean fromLineToGit = false;
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollOffSet = mScroller.getCurrX();
            if(fromLineToGit) {
                scrollGithubView();
            }
            updateView();
            requestLayout();
        }
    }

    public void scrollToTargetCell(int scrollCellNums){
        fromLineToGit = false;
        int currentCellIndex;
        if (scrollOffSet > 0){
            currentCellIndex = -(int)((scrollOffSet + 10) / cellSize);
        } else {
            currentCellIndex = -(int)((scrollOffSet - 10) / cellSize);
        }

        mScroller.startScroll((int)scrollOffSet, 0, (int)((currentCellIndex-scrollCellNums)*cellSize), 0, 100);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void scrollGithubView(){
        if (scrollOffSet < 0){
            githubView.scrollToTarget((int)(-(scrollOffSet+10)/cellSize));
        } else {
            githubView.scrollToTarget((int)(-(scrollOffSet-10)/cellSize));
        }
    }


    public void setGithubView(GithubView githubView){
        this.githubView = githubView;
    }
}