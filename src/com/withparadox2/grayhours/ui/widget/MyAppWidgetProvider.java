package com.withparadox2.grayhours.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.task.TimeRunTaskThread;
import com.withparadox2.grayhours.ui.UpdateWidgetService;
import com.withparadox2.grayhours.utils.CustomAction;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by withparadox2 on 14-2-23.
 */
public class MyAppWidgetProvider extends AppWidgetProvider{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Intent intent = new Intent().setAction(CustomAction.CLICK_BUTTON_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
		remoteViews.setOnClickPendingIntent(R.id.start_button, pendingIntent);

		Intent intent1 = new Intent(context, TaskListActivity.class);
		PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
		remoteViews.setOnClickPendingIntent(R.id.task_name_text, pendingIntent1);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(CustomAction.CLICK_BUTTON_ACTION)){
			if(UpdateWidgetService.getTaskBean() == null){
				if(!UpdateWidgetService.START_FLAG){
					Intent i = new Intent().setClass(context, UpdateWidgetService.class);
					context.startService(i);
				} else {
					Intent i = new Intent().setClass(context, UpdateWidgetService.class);
					context.stopService(i);
				}
				UpdateWidgetService.START_FLAG = !UpdateWidgetService.START_FLAG;
			} else {
				Intent i = new Intent().setClass(context, TaskListActivity.class);
				context.startActivity(i);
			}

		}

		if (intent.getAction().equals(CustomAction.START_TASK_ACTION)){
			TaskBean taskBean = intent.getParcelableExtra(UpdateWidgetService.KEY_TASKBEAN);
			setTaskNameText(taskBean.getName(), context);
			setButtonText("结束",context);
		}

		if (intent.getAction().equals(CustomAction.END_TASK_ACTION)){
			setButtonText("开始",context);
		}

		Log.d("=========", intent.getAction());

		if (intent.getAction().equals(CustomAction.SEND_TIME_ACTION)){
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

	private void setTaskNameText(String nameText, Context context){
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
		remoteViews.setTextViewText(R.id.task_name_text, nameText);
		AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
	}

}

