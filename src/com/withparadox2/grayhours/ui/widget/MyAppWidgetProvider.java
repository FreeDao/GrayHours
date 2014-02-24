package com.withparadox2.grayhours.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.task.TimeRunTaskThread;
import com.withparadox2.grayhours.ui.UpdateWidgetService;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by withparadox2 on 14-2-23.
 */
public class MyAppWidgetProvider extends AppWidgetProvider{

	public static final String START_BUTTON_CLICK_ACTION = "com.withparadox2.grayhours.START_BUTTON_CLICK_ACTION";
	private boolean TimerIsStopFlag = true;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Intent intent = new Intent().setAction(START_BUTTON_CLICK_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
		remoteViews.setOnClickPendingIntent(R.id.start_button, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(START_BUTTON_CLICK_ACTION)){
			if(!UpdateWidgetService.START_FLAG){
				Intent i = new Intent().setClass(context, UpdateWidgetService.class);
				context.startService(i);
				setButtonText("结束",context);
			} else {
				Intent i = new Intent().setClass(context, UpdateWidgetService.class);
				context.stopService(i);
				setButtonText("开始",context);
			}
			UpdateWidgetService.START_FLAG = !UpdateWidgetService.START_FLAG;
		}

		if (intent.getAction().equals(UpdateWidgetService.UPDATE_TIME_BROADCAST)){
			updateRemoteViews(intent.getIntExtra(TimeRunTaskThread.KEY_TIME, 0), context);
		}
		super.onReceive(context, intent);
	}

	private void updateRemoteViews(int time, Context context){
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
		remoteViews.setTextViewText(R.id.time_text, Util.convertSecondsToMinuteHourString(time));
		AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
	}

	private void setButtonText(String text, Context context){
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
		remoteViews.setTextViewText(R.id.start_button, text);
		AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
	}

}
