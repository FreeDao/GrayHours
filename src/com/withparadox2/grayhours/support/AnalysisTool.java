package com.withparadox2.grayhours.support;

import android.content.Context;
import android.content.SharedPreferences;
import com.withparadox2.grayhours.bean.WorkBean;
import com.withparadox2.grayhours.dao.DatabaseManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by withparadox2 on 14-3-3.
 */
public class AnalysisTool {
	public static final String PREFS_NAME = "grayhours";


	public static int TODAY_INDEX = CalendarTool.getWeekOfToday()-1;
	public static int MAX_TOTAL_MINUTES = 0;


	public static Map<Integer, Integer> getDataMap(int index){
		MAX_TOTAL_MINUTES = 0;
		int dateInterval, totalMinutes;
		List<WorkBean> workList = DatabaseManager.getInstanse().getWorkListByIndex(index);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(WorkBean workBean:workList){
			dateInterval = CalendarTool.getDateIntervalFromBase(workBean.getDate());
			totalMinutes = Integer.parseInt(workBean.getTotalTime())/60;
			if(totalMinutes > MAX_TOTAL_MINUTES) MAX_TOTAL_MINUTES = totalMinutes;
			map.put(dateInterval, totalMinutes);
		}
		return map;
	}

	public static Map<Integer, Integer> getDataMapTest(int index){
		Random random = new Random();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i=-20; i<20; i++){
			map.put(CalendarTool.getDateIntervalFromBase(CalendarTool.getDateFromToday(i)),
				random.nextInt(24*60));
		}
		return map;
	}

	public static int getSharePreference(Context context, String key){
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		return settings.getInt(key, 12);
	}

	public static void setSharePreferenceInt(Context context, String key, int value){
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}

}
