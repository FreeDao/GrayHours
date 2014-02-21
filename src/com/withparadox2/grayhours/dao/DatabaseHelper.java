package com.withparadox2.grayhours.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 14-2-20.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "grayhours";
	public static final int DATABASE_VERSION = 1;

	private static final String CREAT_TASK_TABLE_SQL = "create table " + TaskTable.TABLE_NAME
		+ "("
		+ TaskTable.KEY_ID + " integer primary key autoincrement,"
		+ TaskTable.KEY_NAME + " text,"
		+ TaskTable.KEY_START_TIME + " text,"
		+ TaskTable.KEY_TOTAL_TIME + " text"
		+ ");";

	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAT_TASK_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
