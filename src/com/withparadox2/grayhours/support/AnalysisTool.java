package com.withparadox2.grayhours.support;

import com.withparadox2.grayhours.bean.WorkBean;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.dao.WorkTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by withparadox2 on 14-3-3.
 */
public class AnalysisTool {

	public static Map<Integer, Integer> getDataMap(int index){
		int dateInterval, totalMinutes;
		List<WorkBean> workList = DatabaseManager.getInstanse().getWorkListByIndex(index);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(WorkBean workBean:workList){
			dateInterval = CalenDarTool.getDateIntervalFromBase(workBean.getDate());
			totalMinutes = Integer.parseInt(workBean.getTotalTime());
			map.put(dateInterval, totalMinutes);
		}
		return map;
	}
}
