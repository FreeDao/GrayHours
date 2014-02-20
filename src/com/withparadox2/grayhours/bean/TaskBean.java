package com.withparadox2.grayhours.bean;

/**
 * Created by Administrator on 14-2-20.
 */
public class TaskBean {
	private long id;
	private String name;
	private String startTime;
	private String totalTime;

	public TaskBean(long id, String name, String startTime, String totalTime){
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.totalTime = totalTime;
	}

	public TaskBean(){

	}

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
}

