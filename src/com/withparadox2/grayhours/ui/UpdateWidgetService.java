package com.withparadox2.grayhours.ui;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.support.BaseHandler;
import com.withparadox2.grayhours.task.TimeRunTaskThread;
import com.withparadox2.grayhours.ui.widget.MyAppWidgetProvider;
import com.withparadox2.grayhours.utils.Util;

/**
 * Created by withparadox2 on 14-2-24.
 */
public class UpdateWidgetService extends Service{
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		TimeRunTaskThread timeRunTaskThread = new TimeRunTaskThread(new MyHandler());
		timeRunTaskThread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void updateRemoteViews(String time){
		RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.appwidget_layout);
		remoteViews.setTextViewText(R.id.time_text, time);
		AppWidgetManager.getInstance(this).updateAppWidget(new ComponentName(this, MyAppWidgetProvider.class), remoteViews);
	}

	private class MyHandler extends BaseHandler{

		@Override
		public void handleMessage(Message msg) {
			updateRemoteViews(Util.convertSecondsToMinuteHourString(msg.arg1));
		}

	}
}
