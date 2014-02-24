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

/**
 * Created by withparadox2 on 14-2-23.
 */
public class MyAppWidgetProvider extends AppWidgetProvider{

	public static final String START_BUTTON_CLICK_ACTION = "com.withparadox2.grayhours.START_BUTTON_CLICK_ACTION";
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
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
			remoteViews.setTextViewText(R.id.task_name_text, "Hello World++!");
			Toast.makeText(context, "Click Button", Toast.LENGTH_SHORT).show();
			AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
		}
		super.onReceive(context, intent);
	}
}
