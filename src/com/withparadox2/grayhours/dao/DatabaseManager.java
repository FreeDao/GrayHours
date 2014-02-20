package com.withparadox2.grayhours.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.utils.GlobalContext;

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
					singleton.databaseHelper = new DatabaseHelper(GlobalContext.getInstanse());
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
		return list;
	}


}
