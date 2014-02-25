package com.withparadox2.grayhours.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.withparadox2.grayhours.support.BaseHandler;
import com.withparadox2.grayhours.ui.WorkFragment;

/**
 * Created by Administrator on 14-2-20.
 */
public class TimeRunTaskThread extends Thread{
	private BaseHandler handler;
	private Context context;
	private int time = 0;
	private boolean stopFlag = false;

	public static final String UPDATE_TIME_BROADCAST = "com.withparadox2.grayhours.SEND_BROADCAST_ACTION";
	public static final String KEY_TIME = "key_time";

	public TimeRunTaskThread(BaseHandler handler) {
		super();
		this.handler = handler;
	}

	public TimeRunTaskThread(Context context){
		super();
		this.context = context;
	}

	@Override
	public void run() {
		while (!stopFlag){
			context.sendBroadcast(buildIntent(time));
			time ++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Intent buildIntent(int paraTime){
		Intent intent = new Intent();
		intent.setAction(UPDATE_TIME_BROADCAST);
		intent.putExtra(KEY_TIME, paraTime);
		return intent;
	}

	public void stopThread(){
		if(this.isAlive())
			stopFlag = true;
	}
}
