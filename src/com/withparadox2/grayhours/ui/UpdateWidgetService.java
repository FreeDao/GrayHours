package com.withparadox2.grayhours.ui;

import android.app.ActivityManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
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
import com.withparadox2.grayhours.utils.CustomAction;
import com.withparadox2.grayhours.utils.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by withparadox2 on 14-2-24.
 */
public class UpdateWidgetService extends Service{
	private static Timer timer;

	public static boolean START_FLAG = false;
	public static final String UPDATE_TIME_BROADCAST = "com.withparadox2.grayhours.SEND_BROADCAST_ACTION";
	public static final String KEY_TIME = "key_time";
	private Context context;

	private BroadcastReceiver broadcastReceiver;

	public int onStartCommand(Intent intent, int flags, int startId) {

		context = this;
		startTimer();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d("==========", "stop timer");
		stopTimer();
		super.onDestroy();
	}

	private void sendEndTaskBroadcast(){
		Intent i = new Intent();
		i.setAction(CustomAction.END_TASK_ACTION);
		context.sendBroadcast(i);
	}

	private void sendStartTaskBroadcast(){
		Intent i = new Intent();
		i.setAction(CustomAction.START_TASK_ACTION);
		context.sendBroadcast(i);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void startTimer(){
		sendStartTaskBroadcast();
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
		if (timer != null){
			timer.cancel();
			timer.purge();
		}
		sendEndTaskBroadcast();
	}

	private Intent buildIntent(int paraTime){
		Intent intent = new Intent();
		intent.setAction(CustomAction.SEND_TIME_ACTION);
		intent.putExtra(KEY_TIME, paraTime);
		return intent;
	}

	public static boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (UpdateWidgetService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
		}
	}
}
