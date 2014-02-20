package com.withparadox2.grayhours.task;

import com.withparadox2.grayhours.ui.WorkFragment;

/**
 * Created by Administrator on 14-2-20.
 */
public class TimeRunTaskThread extends Thread{
	private WorkFragment.SetTimeTextHandler handler;
	private int time = 0;
	private boolean stopFlag = false;

	public TimeRunTaskThread(WorkFragment.SetTimeTextHandler handler) {
		super();
		this.handler = handler;
	}

	@Override
	public void run() {
		while (!stopFlag){
			time ++;
			handler.sendMessageToTarget(time);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopThread(){
		stopFlag = true;
	}

	public boolean isRunning(){
		return !stopFlag;
	}
}
