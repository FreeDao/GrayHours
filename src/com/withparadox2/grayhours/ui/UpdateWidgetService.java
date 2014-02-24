package com.withparadox2.grayhours.ui;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.support.BaseHandler;
import com.withparadox2.grayhours.task.TimeRunTaskThread;
import com.withparadox2.grayhours.ui.widget.MyAppWidgetProvider;
import com.withparadox2.grayhours.utils.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by withparadox2 on 14-2-24.
 */
public class UpdateWidgetService extends Service{
	private Timer timer;

	public static boolean START_FLAG = false;
	public static final String UPDATE_TIME_BROADCAST = "com.withparadox2.grayhours.SEND_BROADCAST_ACTION";
	public static final String KEY_TIME = "key_time";
	private Context context;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		context = this;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("==========+", "start timer");

		startTimer();//启动计时器
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		Log.d("==========", "stop timer");
		stopTimer();//停止计时器
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void startTimer(){
		timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			int time = 0;
			@Override
			public void run() {
				context.sendBroadcast(buildIntent(time));
				time ++;
			}
		};
		timer.schedule(timerTask, 0, 1000);
	}

	public void stopTimer() {
		timer.cancel();
	}

	private Intent buildIntent(int paraTime){
		Intent intent = new Intent();
		intent.setAction(UPDATE_TIME_BROADCAST);
		intent.putExtra(KEY_TIME, paraTime);
		return intent;
	}
}
