package com.withparadox2.grayhours.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.task.TimeRunTaskThread;
import com.withparadox2.grayhours.utils.Util;


/**
 * Created by Administrator on 14-2-20.
 */
public class WorkFragment extends BaseFragment{

	private TextView timeTextView;
	private SetTimeTextHandler handler;
	private TimeRunTaskThread timeRunTaskThread;
	private TaskBean taskBean;

	public WorkFragment(TaskBean taskBean){
		this.taskBean = taskBean;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.workfragment_layout, container, false);
		Button button = (Button) v.findViewById(R.id.start_button);
		button.setOnClickListener(new OnStartButtonClickListener());
		timeTextView = (TextView) v.findViewById(R.id.time_text);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		handler = new SetTimeTextHandler(Looper.myLooper());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timeRunTaskThread != null){
			timeRunTaskThread.stopThread();
		}

	}

	private class OnStartButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if(timeRunTaskThread == null || !timeRunTaskThread.isAlive()){
				timeRunTaskThread = new TimeRunTaskThread(handler);
				timeRunTaskThread.start();
			} else {
				timeRunTaskThread.stopThread();
			}
		}
	}

	public class SetTimeTextHandler extends Handler{

		public SetTimeTextHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			updateTimeTextView(msg.arg1);
		}

		private Message getMessage(){
			return this.obtainMessage();
		}

		public void sendMessageToTarget(int time){
			Message message = getMessage();
			message.arg1 = time;
			message.sendToTarget();

		}
	}

	private void updateTimeTextView(int time){

		timeTextView.setText(String.valueOf(time));
	}

	private void saveTimeToDb(int time){
		DatabaseManager.getInstanse().addWork(taskBean.getIndex(), Util.getCurrentDate(), String.valueOf(time));
	}

}
