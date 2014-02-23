package com.withparadox2.grayhours.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * Created by Administrator on 14-2-23.
 */
public class MyAppWidgetProvider extends AppWidgetProvider{
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for(int appWidgetId : appWidgetIds){

		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}
}
