package com.withparadox2.grayhours.dao;

/**
 * Created by Administrator on 14-2-20.
 */
public class WorkTable {
	public static String getTaskItemTableName(int index){
		return "task_" +index;
	}
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_START_TIME = "start_time";
	public static final String KEY_TOTAL_TIME = "total_time";}
