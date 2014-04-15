package com.withparadox2.grayhours.support;

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

	public static Map<Integer, Integer> getDataMap(int index){
		int dateInterval, totalMinutes;
		List<WorkBean> workList = DatabaseManager.getInstanse().getWorkListByIndex(index);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(WorkBean workBean:workList){
			dateInterval = CalendarTool.getDateIntervalFromBase(workBean.getDate());
			totalMinutes = Integer.parseInt(workBean.getTotalTime())/60;
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


}
