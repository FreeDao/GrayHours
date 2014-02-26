package com.withparadox2.grayhours.bean;

import android.os.Parcelable;

/**
 * Created by Administrator on 14-2-20.
 */
public class TaskBean{
	private long id;
	private String name;
	private String startTime;
	private String totalTime;
	private int index;

	public TaskBean(){}

	public long getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public String getStartTime(){
		return startTime;
	}

	public String getTotalTime(){
		return totalTime;
	}

	public int getIndex(){
		return index;
	}

	public void setId(Long id){
		this.id = id;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public void setTotalTime(String totalTime){
		this.totalTime = totalTime;
	}

	public void setIndex(int index){
		this.index = index;
	}
}

