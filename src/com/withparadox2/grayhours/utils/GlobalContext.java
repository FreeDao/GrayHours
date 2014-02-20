package com.withparadox2.grayhours.utils;

import android.app.Application;

/**
 * Created by Administrator on 14-2-20.
 */
public class GlobalContext extends Application{
	private static GlobalContext globalContext = null;

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
	}

	public static GlobalContext getInstanse(){
		return globalContext;
	}
}
