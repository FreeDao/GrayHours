package com.withparadox2.grayhours.dao;

import android.database.sqlite.SQLiteDatabase;
import com.withparadox2.grayhours.utils.GlobalContext;

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
}
