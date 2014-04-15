package com.withparadox2.grayhours.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by withparadox2 on 14-2-20.
 */
public class Util {


	public static int dip2px(int dipValue) {
		float reSize = GlobalContext.getInstance().getResources().getDisplayMetrics().density;
		return (int) ((dipValue * reSize) + 0.5);
	}

	public static int px2dip(int pxValue) {
		float reSize = GlobalContext.getInstance().getResources().getDisplayMetrics().density;
		return (int) ((pxValue / reSize) + 0.5);
	}

	public static float sp2px(int spValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
			GlobalContext.getInstance().getResources().getDisplayMetrics());
	}

	public static String getCurrentDate(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(calendar.getTime());
	}

	public static String convertSecondsToMinuteHourString(int time){
		StringBuilder formatTimeStringBuilder = new StringBuilder();
		int seconds = time % 60;
		int minutes = time / 60;
		int hours = minutes / 60;
		minutes = minutes % 60;
		formatTimeStringBuilder.append(formatNumLessThanTen(hours))
				.append(":")
				.append(formatNumLessThanTen(minutes))
				.append(":")
				.append(formatNumLessThanTen(seconds));
		return formatTimeStringBuilder.toString();
	}

	public static String formatNumLessThanTen(int num){
		return num < 10 ? "0" + num : String.valueOf(num);
	}

	public static String convertSecondsToHours(int seconds){
		int minutes = seconds/60;
		if(minutes/60 < 1){
			return String.valueOf(minutes) + "m";
		}
		return String.valueOf(minutes/60) + "h" + String.valueOf(minutes%60) + "m";
	}

	public static String convertMinutesToHours(int minutes){
		return formatTimeDigits(minutes / 60) + ":" +formatTimeDigits(minutes % 60);
	}
	
	public static String formatTimeDigits(int digits){
		if(digits < 10){
			return "0"+String.valueOf(digits);
		} else {
			return String.valueOf(digits);
		}
	}

	public static Point getScreenSize(){
		WindowManager windowManager = (WindowManager) GlobalContext.getInstance()
			.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

	public static int getScreenWidth(){
		Point p = getScreenSize();
		return p.x;
	}

	public static int getScreenHeight(){
//		Point p = getScreenSize();
//		if(screenHeight < 0){
//			return p.y;
//		} else {
//			return screenHeight;
//		}
		return screenHeight;
//		return 1134;
	}

	private static int screenHeight = -1;

	public static void setScreenHeight(int height){
		screenHeight = height;
	}
}