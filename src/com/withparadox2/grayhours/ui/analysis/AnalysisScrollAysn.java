package com.withparadox2.grayhours.ui.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by withparadox2 on 14-4-20.
 */
public class AnalysisScrollAysn {
	private List<ScrollObserver> observerList;
	public interface ScrollObserver{
		public void scrollAnsy(float dis);
	}

	public AnalysisScrollAysn(){
		observerList = new ArrayList<ScrollObserver>();
	}

	public void addObserver(ScrollObserver observer){
		observerList.add(observer);
	}

	public void removeObserver(ScrollObserver observer){
		observerList.remove(observer);
	}

	public void updateObserver(float dis){
		for (ScrollObserver observer:observerList){
			observer.scrollAnsy(dis);
		}
	}
}
