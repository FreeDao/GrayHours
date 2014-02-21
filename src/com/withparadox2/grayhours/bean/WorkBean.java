package com.withparadox2.grayhours.bean;

/**
 * Created by Administrator on 14-2-21.
 */
public class WorkBean {
	private long id;
	private String date;
	private String totalTime;


	public WorkBean(){

	}

	public long getId(){
		return id;
	}

	public String getDate(){
		return date;
	}


	public String getTotalTime(){
		return totalTime;
	}

	public void setId(Long id){
		this.id = id;
	}

	public void setDate(String date){
		this.date = date;
	}


	public void setTotalTime(String totalTime){
		this.totalTime = totalTime;
	}
}
