package com.withparadox2.grayhours.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.task.TimeRunTaskThread;


/**
 * Created by Administrator on 14-2-20.
 */
public class WorkFragment extends BaseFragment{

	private String timeText;
	private SetTimeTextHandler handler;
	private TimeRunTaskThread timeRunTaskThread;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.workfragment_layout, container, false);
		Button button = (Button) v.findViewById(R.id.start_button);
		button.setOnClickListener(new OnStartButtonClickListener());
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		handler = new SetTimeTextHandler(Looper.getMainLooper());
	}

	private class OnStartButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if(timeRunTaskThread == null || !timeRunTaskThread.isRunning()){
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

		}

		private Message getMessage(){
			return this.obtainMessage();
		}

		public void sendMessageToTarget(int time){
			getMessage().arg1 = time;
			getMessage().sendToTarget();
		}
	}






}
