package com.withparadox2.grayhours.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.bean.WorkBean;
import com.withparadox2.grayhours.utils.GlobalContext;
import com.withparadox2.grayhours.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-2-20.
 */
public class DatabaseManager {
	private static final String TAG = DatabaseManager.class.getName();
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;
	private static DatabaseManager singleton = null;

	public static DatabaseManager getInstanse(){
		if(singleton == null){
			synchronized(DatabaseManager.class){
				if(singleton == null){
					singleton = new DatabaseManager();
					singleton.databaseHelper = new DatabaseHelper(GlobalContext.getInstance());
					singleton.database = singleton.databaseHelper.getWritableDatabase();
				}
			}
		}
		return singleton;
	}

	public void closeDatabase(){
		if(singleton != null){
			singleton.databaseHelper.close();
		}
	}

	public List<TaskBean> getTaskList(){
		String sql = "select * from " + TaskTable.TABLE_NAME;
		Cursor cursor = database.rawQuery(sql, null);
		List<TaskBean> list = new ArrayList<TaskBean>();
		while (cursor.moveToNext()){
			TaskBean taskBean = new TaskBean();
			int columnIndex = cursor.getColumnIndex(TaskTable.KEY_ID);
			taskBean.setId(cursor.getLong(columnIndex));

			columnIndex = cursor.getColumnIndex(TaskTable.KEY_NAME);
			taskBean.setName(cursor.getString(columnIndex));

			columnIndex = cursor.getColumnIndex(TaskTable.KEY_START_TIME);
			taskBean.setStartTime(cursor.getString(columnIndex));

			columnIndex = cursor.getColumnIndex(TaskTable.KEY_TOTAL_TIME);
			taskBean.setTotalTime(cursor.getString(columnIndex));

			list.add(taskBean);
		}
		cursor.close();
		return list;
	}

	public void addTask(String name, String startTime){
		ContentValues values = new ContentValues();
		values.put(TaskTable.KEY_NAME, name);
		values.put(TaskTable.KEY_START_TIME, startTime);
		values.put(TaskTable.KEY_TOTAL_TIME, "0");
		database.insert(TaskTable.TABLE_NAME, null, values);
	}

	public void updateTotalTimeInTaskTable(long id, String time){
		ContentValues values = new ContentValues();
		values.put(TaskTable.KEY_TOTAL_TIME, time);
		database.update(TaskTable.TABLE_NAME, values, TaskTable.KEY_ID + "=" + id, null);
	}

	public List<TaskBean> getWorkList(){
		String sql = "select * from " + TaskTable.TABLE_NAME;
		Cursor cursor = database.rawQuery(sql, null);
		List<TaskBean> list = new ArrayList<TaskBean>();
		while (cursor.moveToNext()){
			TaskBean taskBean = new TaskBean();
			int columnIndex = cursor.getColumnIndex(TaskTable.KEY_ID);
			taskBean.setId(cursor.getLong(columnIndex));

			columnIndex = cursor.getColumnIndex(TaskTable.KEY_NAME);
			taskBean.setName(cursor.getString(columnIndex));

			columnIndex = cursor.getColumnIndex(TaskTable.KEY_START_TIME);
			taskBean.setStartTime(cursor.getString(columnIndex));

			columnIndex = cursor.getColumnIndex(TaskTable.KEY_TOTAL_TIME);
			taskBean.setTotalTime(cursor.getString(columnIndex));

			list.add(taskBean);
		}
		cursor.close();
		return list;
	}

	public void addWork(int tableIndex, String date, String totalTime){
		ContentValues values = new ContentValues();
		values.put(WorkTable.KEY_DATE, date);
		values.put(WorkTable.KEY_TOTAL_TIME_A_DAY, date);
		values.put(WorkTable.KEY_TOTAL_TIME_A_DAY, totalTime);
		database.insert(WorkTable.getWorkTableName(tableIndex), null, values);
	}

	public void addOrUpdateWork(int tableIndex, String date, String totalTime){
		WorkBean workBean = getLastWorkBeanItem(tableIndex);
		if (workBean != null && workBean.getDate().equals(date)){
			int temp = Integer.parseInt(totalTime) + Integer.parseInt(workBean.getTotalTime());
			updateTotalTimeInWorkTable(tableIndex, workBean.getId(), String.valueOf(temp));
		} else {

		}
	}

	public void updateTotalTimeInWorkTable(int tableIndex, long id, String time){
		ContentValues values = new ContentValues();
		values.put(WorkTable.KEY_TOTAL_TIME_A_DAY, time);
		database.update(WorkTable.getWorkTableName(tableIndex), values, WorkTable.KEY_ID + " = " + id, null);
	}

	private WorkBean getLastWorkBeanItem(int tableIndex){
		String sql = "select * from " + WorkTable.getWorkTableName(tableIndex);
		Cursor cursor = database.rawQuery(sql, null);
		WorkBean workBean;
		if (cursor.moveToLast()){
			workBean = new WorkBean();
			int columnIndex = cursor.getColumnIndex(WorkTable.KEY_ID);
			workBean.setId(cursor.getLong(columnIndex));

			columnIndex = cursor.getColumnIndex(WorkTable.KEY_DATE);
			workBean.setDate(cursor.getString(columnIndex));

			columnIndex = cursor.getColumnIndex(WorkTable.KEY_TOTAL_TIME_A_DAY);
			workBean.setTotalTime(cursor.getString(columnIndex));
			cursor.close();
			return workBean;
		}
		cursor.close();
		return null;
	}
	public void creatWorkTableByIndex(int index){
		String sql = WorkTable.getCreatTableSql(index);
		database.execSQL(sql);
	}


}
