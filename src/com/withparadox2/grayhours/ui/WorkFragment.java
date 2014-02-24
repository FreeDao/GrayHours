package com.withparadox2.grayhours.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.support.BaseHandler;
import com.withparadox2.grayhours.task.TimeRunTaskThread;
import com.withparadox2.grayhours.utils.Util;


/**
 * Created by withparadox2 on 14-2-20.
 */
public class WorkFragment extends BaseFragment{

	private TextView timeTextView;
	private Button startButton;
	private TaskBean taskBean;
	private BroadcastReceiver broadcastReceiver;

	public WorkFragment(TaskBean taskBean){
		this.taskBean = taskBean;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.workfragment_layout, container, false);
		TextView tastText = (TextView) v.findViewById(R.id.task_name_text);
		tastText.setText(taskBean.getName());
		startButton = (Button) v.findViewById(R.id.start_button);
		startButton.setOnClickListener(new OnStartButtonClickListener());
		timeTextView = (TextView) v.findViewById(R.id.time_text);
		updateTimeTextView(0);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(UpdateWidgetService.UPDATE_TIME_BROADCAST));
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				updateTimeTextView(intent.getIntExtra(UpdateWidgetService.KEY_TIME, 0));
			}
		};
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	private class OnStartButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent i = new Intent().setClass(getActivity(), UpdateWidgetService.class);
			getActivity().startService(i);
//			if(timeRunTaskThread == null || !timeRunTaskThread.isAlive()){
//				timeRunTaskThread = new TimeRunTaskThread(handler);
//				timeRunTaskThread.start();
//				startButton.setText("结束");
//			} else {
//				timeRunTaskThread.stopThread();
//				saveTimeToDb(timeTextView.getTag().toString());
//				updateTimeTextView(0);
//				startButton.setText("开始");
//			}
		}
	}


	private void updateTimeTextView(int time){

		timeTextView.setText(Util.convertSecondsToMinuteHourString(time));
		timeTextView.setTag(time);
	}

	private void saveTimeToDb(String time){
		DatabaseManager.getInstanse().updateTotalTimeInTaskTable(taskBean, time);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				getFragmentManager().popBackStack();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
