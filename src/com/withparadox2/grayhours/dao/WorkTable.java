package com.withparadox2.grayhours.dao;

/**
 * Created by Administrator on 14-2-20.
 */
public class WorkTable {
	public static String getWorkTableName(int index){
		return "task_" +index;
	}
	public static final String KEY_ID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_TOTAL_TIME_A_DAY = "total_time";

	public static String getCreatTableSql(int index){
		return "create table if not exists " + getWorkTableName(index)
				+ "("
				+ KEY_ID + " integer primary key autoincrement,"
				+ KEY_DATE + " text,"
				+ KEY_TOTAL_TIME_A_DAY + " text"
				+ ");";
	}
}
