package com.withparadox2.grayhours.support;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Administrator on 14-2-24.
 */
public class BaseHandler extends Handler{

	public BaseHandler(Looper looper){
		super(looper);
	}

	public BaseHandler(){}

	private Message getMessage(){
		return this.obtainMessage();
	}

	public void sendMessageToTarget(int time){
		Message message = getMessage();
		message.arg1 = time;
		message.sendToTarget();

	}
}
