package com.withparadox2.grayhours.utils;

import android.util.Log;

/**
 * Created by Administrator on 14-2-28.
 */
public class DebugConfig {
	public static boolean debug = true;
	public static void log(String message, Object... args){
		log(String.format(message, args));
	}

	public static void log(String message){
		Log.d("LittleThings", message);
	}
}
