package com.withparadox2.grayhours.ui;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseManager;
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
	public static final String KEY_TIME = "key_time";
	public static final String KEY_TASKBEAN = "key_taskbean";
	private Context context;
	private static TaskBean taskBean;
	private PowerManager.WakeLock wakeLock;

	private BroadcastReceiver broadcastReceiver;

	public int onStartCommand(Intent intent, int flags, int startId) {

		context = this;
		startTimer();
		taskBean = DatabaseManager.getInstanse().getTaskList().get(taskBean.getIndex());
		PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wakeLock.acquire();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d("==========", "stop timer");
		stopTimerAndSave();
		wakeLock.release();
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
		i.putExtra(KEY_TASKBEAN, taskBean);
		context.sendBroadcast(i);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	private int time = 0;

	private void startTimer(){
		stopTimer();
		sendStartTaskBroadcast();
		timer = new Timer();
		time = 0;
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				context.sendBroadcast(buildIntent(time));
				time ++;
			}
		};
		timer.schedule(timerTask, 0, 1000);
	}

	private void stopTimer(){
		if (timer != null){
			timer.cancel();
			timer.purge();
		}
	}


	public void stopTimerAndSave() {
		stopTimer();
		sendEndTaskBroadcast();
		saveTimeToDb(String.valueOf(time));
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

	public static void setTaskBean(TaskBean paraTaskBean){
		taskBean = paraTaskBean;
	}

	public static TaskBean getTaskBean(){
		return taskBean;
	}

	private void saveTimeToDb(String time){
		DatabaseManager.getInstanse().updateTotalTimeInTaskTable(taskBean, time);
		DatabaseManager.getInstanse().addOrUpdateWork(taskBean.getIndex(), Util.getCurrentDate(), time);
	}
}
