package com.withparadox2.grayhours.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.EditText;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.ui.custom.*;
import com.withparadox2.grayhours.utils.CustomAction;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

import java.util.List;
import java.util.Random;

/**
 * Created by withparadox2 on 14-2-20.
 */
public class PanelFragment extends BaseFragment implements ValueAnimator.AnimatorUpdateListener, ValueAnimator.AnimatorListener{
	private List<TaskBean> taskBeanList;
	private CustomParentLayout root;
	private ValueAnimator animator;
	private int animationIndex;
	private boolean zoomFlag = false;
	private BroadcastReceiver broadcastReceiver;
	private boolean serviceIsRunningBeforeActivity = false;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = new CustomParentLayout(getActivity());
		root.addView(new CustomRowLayout(getActivity()));
		root.addView(new CustomRowLayout(getActivity()));
		buildView(root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setTitle("GrayHours");
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
		if (UpdateWidgetService.isMyServiceRunning(getActivity())){
			serviceIsRunningBeforeActivity = true;
			animationIndex = UpdateWidgetService.getTaskBean().getIndex();
			if (!zoomFlag){
				startAnimator();
			}
		} else {
			serviceIsRunningBeforeActivity = false;
			if (zoomFlag){
				startAnimator();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.panelfragment_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.menu_analysis:
				zoomFlag = false;
				getFragmentManager().beginTransaction().replace(android.R.id.content, new AnalysisFragment(taskBeanList, animationIndex)).addToBackStack(null).commit();
				break;
			case R.id.menu_test:
				Random random = new Random();
				for(int i=0; i<500; i++){
					if(i%30 == 0){
						DatabaseManager.getInstanse().writeSingleToDatabaseForTest(-i, 0);
					} else {
						DatabaseManager.getInstanse().writeSingleToDatabaseForTest(-i, random.nextInt(8*60*60));
					}
				}
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateList(){
		taskBeanList = DatabaseManager.getInstanse().getTaskList();
	}

	private void buildView(ViewGroup root){
		updateList();
		CustomRowLayout row1 = (CustomRowLayout) root.getChildAt(0);
		CustomRowLayout row2 = (CustomRowLayout) root.getChildAt(1);
		row1.removeAllViews();
		row2.removeAllViews();
		switch (taskBeanList.size()){
			case 0:
				setAddTaskButtonView(row1);
				break;
			case 1:
				setTaskButtonView(row1, 0);
				setAddTaskButtonView(row1);
				break;
			case 2:
				setTaskButtonView(row1, 0);
				setTaskButtonView(row1, 1);
				setAddTaskButtonView(row2);
				break;
			case 3:
				setTaskButtonView(row1, 0);
				setTaskButtonView(row1, 1);
				setTaskButtonView(row2, 2);
				setAddTaskButtonView(row2);
				break;
			case 4:
				setTaskButtonView(row1, 0);
				setTaskButtonView(row1, 1);
				setTaskButtonView(row2, 2);
				setTaskButtonView(row2, 3);
				break;
		}
	}

	private void setAddTaskButtonView(CustomRowLayout row){
		AddTaskButton addTaskButton = new AddTaskButton(getActivity(), getResources().getString(R.color.royalblue));
		addTaskButton.setOnClickListener(new AddOnClickListener());
		addTaskButton.setText("add");
		row.addView(addTaskButton);
	}

	private void setTaskButtonView(CustomRowLayout row, int index){
		TaskButton taskButton = new TaskButton(getActivity(), getColorBasesOnIndex(index));
		taskButton.setIndex(index);
		taskButton.setOnClickListener(new StratWorkOnClickListener());
		taskButton.setText(taskBeanList.get(index).getName());
		taskButton.setTimeText(Util.convertSecondsToHoursMinutes(Integer.parseInt(taskBeanList.get(index).getTotalTime())));
		row.addView(taskButton);
	}

	private String getColorBasesOnIndex(int index){
		String color = getResources().getString(R.color.hotpink);
		switch (index){
			case 0:
				color = getResources().getString(R.color.hotpink);
			break;
			case 1:
				color = getResources().getString(R.color.darker_purple);
			break;
			case 2:
				color = getResources().getString(R.color.crimson);
			break;
			case 3:
				color = getResources().getString(R.color.goldenrod);
			break;
		}
		return color;
	}




	private void startAnimator(){
		if (!zoomFlag){
			animator = ValueAnimator.ofFloat(0f, 1f);
		} else {
			animator = ValueAnimator.ofFloat(1f, 0f);
		}
		animator.addUpdateListener(this);
		animator.addListener(this);
		animator.setDuration(1000);
		zoomFlag = !zoomFlag;
		animator.start();
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		float value = (Float)animation.getAnimatedValue();
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int)(Util.getScreenWidth()*(1+value)/2),
			(int)(Util.getScreenHeight()*(1+value)/2));

		ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(Util.getScreenWidth(),
				(int)(Util.getScreenHeight()*(1+value)/2));

		root.getCustomChild(animationIndex/2).getCustomChild(animationIndex%2).setLayoutParams(layoutParams);
		root.getCustomChild(animationIndex/2).setLayoutParams(layoutParams1);
		root.requestLayout();
		root.invalidate();
	}

	@Override
	public void onAnimationStart(Animator animation) {
		if (!zoomFlag){
			if (UpdateWidgetService.isMyServiceRunning(getActivity())){
				stopService();
			}
		}

	}

	@Override
	public void onAnimationEnd(Animator animation) {
		if (zoomFlag){
			if (!UpdateWidgetService.isMyServiceRunning(getActivity())||!serviceIsRunningBeforeActivity){
				UpdateWidgetService.setTaskBean(taskBeanList.get(animationIndex));
				if (UpdateWidgetService.isMyServiceRunning(getActivity())){
					stopService();
				}
				startService();
			}

		} else {
			buildView(root);
		}
	}

	@Override
	public void onAnimationCancel(Animator animation) {

	}

	@Override
	public void onAnimationRepeat(Animator animation) {

	}


	private class AddOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {

			AddDialogFragment dialogFragment =
				(AddDialogFragment) getFragmentManager().findFragmentByTag(AddDialogFragment.class.getName());
			if (dialogFragment == null)
				dialogFragment = new AddDialogFragment();
			dialogFragment.show(getFragmentManager(), AddDialogFragment.class.getName());
		}
	}

	private class StratWorkOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			animationIndex = ((BaseButton) v).getIndex();
			startAnimator();
			if (zoomFlag){
				if (UpdateWidgetService.isMyServiceRunning(getActivity())){
					stopService();
				}
			}
		}
	}


	private class AddDialogFragment extends DialogFragment{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final EditText editText = new EditText(getActivity());

			AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.setTitle("Are you sure?")
				.setView(editText)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!TextUtils.isEmpty(editText.getText())) {
							DatabaseManager.getInstanse().addTask(editText.getText().toString(), Util.getCurrentDate());
							DatabaseManager.getInstanse().creatWorkTableByIndex(getNewButtonIndex());
							buildView(root);
						}
					}
				})
				.setNegativeButton("取消", null)
				.create();
			return alertDialog;
		}
	}

	private int getNewButtonIndex(){
		return taskBeanList.size();
	}

	private void stopService(){
		DebugConfig.log("stopService is called");
		Intent i = new Intent().setClass(getActivity(), UpdateWidgetService.class);
		getActivity().stopService(i);
	}

	private void startService(){
		Intent i = new Intent().setClass(getActivity(), UpdateWidgetService.class);
		getActivity().startService(i);
	}

	private void updateTimeTextView(int time){
		root.getCustomChild(animationIndex/2)
			.getCustomChild(animationIndex%2)
			.setTimeText(Util.convertSecondsToMinuteHourString(time));
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CustomAction.SEND_TIME_ACTION)){
				updateTimeTextView((intent.getIntExtra(UpdateWidgetService.KEY_TIME, 0)));
			} else if (action.equals(CustomAction.END_TASK_ACTION)){
//
			}


		}
	}

}
