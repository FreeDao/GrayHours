package com.withparadox2.grayhours.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
import com.withparadox2.grayhours.utils.CustomAction;
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
		UpdateWidgetService.setTaskBean(taskBean);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.workfragment_layout, container, false);
		TextView tastText = (TextView) v.findViewById(R.id.task_name_text);
		tastText.setText(taskBean.getName());
		startButton = (Button) v.findViewById(R.id.start_button);
		startButton.setOnClickListener(new OnStartButtonClickListener());
		timeTextView = (TextView) v.findViewById(R.id.time_text);

		if(UpdateWidgetService.isMyServiceRunning(getActivity())){
			startButton.setText("结束");
		} else {
			startButton.setText("开始");
		}
		updateTimeTextView(0);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CustomAction.START_TASK_ACTION);
		intentFilter.addAction(CustomAction.END_TASK_ACTION);
		intentFilter.addAction(CustomAction.SEND_TIME_ACTION);
		broadcastReceiver = new MyBroadcastReceiver();
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	private class OnStartButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if(!UpdateWidgetService.isMyServiceRunning(getActivity())){
				Intent i = new Intent().setClass(getActivity(), UpdateWidgetService.class);
				getActivity().startService(i);
			} else {
				Intent i = new Intent().setClass(getActivity(), UpdateWidgetService.class);
				getActivity().stopService(i);
				updateTimeTextView(0);
			}
		}
	}


	private void updateTimeTextView(int time){

		timeTextView.setText(Util.convertSecondsToMinuteHourString(time));
		timeTextView.setTag(time);
	}



	private class MyBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CustomAction.START_TASK_ACTION)){
				startButton.setText("结束");
			} else if (action.equals(CustomAction.END_TASK_ACTION)){
				startButton.setText("开始");
			} else if (action.equals(CustomAction.SEND_TIME_ACTION)){
				timeTextView.setText(Util.convertSecondsToMinuteHourString(intent.getIntExtra(UpdateWidgetService.KEY_TIME, 0)));
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				if(UpdateWidgetService.isMyServiceRunning(getActivity())){
					getActivity().finish();
				} else {
					getActivity().getFragmentManager()
						.beginTransaction()
						.replace(android.R.id.content, new PanelFragment())
						.commit();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	

	private void stopService(){
		Intent i = new Intent().setClass(getActivity(), UpdateWidgetService.class);
		getActivity().stopService(i);
		updateTimeTextView(0);
	}
}
