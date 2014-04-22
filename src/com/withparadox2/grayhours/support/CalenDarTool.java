package com.withparadox2.grayhours.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by withparadox2 on 14-3-7.
 */
public class CalendarTool {
	private final static String BASE_DATE = "2014-00-00";
	public static int getDateIntervalFromBase(String date){
		int result = 0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = dateFormat.parse(date);
			Date date2 = dateFormat.parse(BASE_DATE);
			result = (int) ((date1.getTime() - date2.getTime())/(1000*3600*24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getCurrentDate(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(calendar.getTime());
	}

	public static String getDateFromToday(int x){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, x);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(calendar.getTime());
	}

	public static int getWeekOfToday(){//monday->0, tue->1 .etc.
		Calendar calendar = Calendar.getInstance();
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return intWeek;
	}


}
