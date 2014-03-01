package com.withparadox2.grayhours.ui;

import android.animation.ValueAnimator;
import android.app.*;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.ui.custom.*;
import com.withparadox2.grayhours.utils.Util;

import java.util.List;

/**
 * Created by withparadox2 on 14-2-20.
 */
public class PanelFragment extends BaseFragment implements ValueAnimator.AnimatorUpdateListener{
	private List<TaskBean> taskBeanList;
	private CustomParentLayout root;
	private ValueAnimator animator;
	private int animationIndex;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = new CustomParentLayout(getActivity());

		root.addView(new CustomRowLayout(getActivity()));
		root.addView(new CustomRowLayout(getActivity()));
		root.getChildAt(0).setBackgroundColor(Color.YELLOW);
		root.getChildAt(0).setBackgroundColor(Color.BLUE);

		buildView(root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		animator = ValueAnimator.ofFloat(0f, 1f);
		animator.addUpdateListener(this);
		animator.setDuration(2000);
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
		AddTaskButton addTaskButton = new AddTaskButton(getActivity());
//		addTaskButton.setOnClickListener(new AddOnClickListener());
		addTaskButton.setText("add");
		row.addView(addTaskButton);
	}

	private void setTaskButtonView(CustomRowLayout row, int index){
		TaskButton taskButton = new TaskButton(getActivity());
		taskButton.setIndex(index);
		taskButton.setOnClickListener(new StratWorkOnClickListener());
		taskButton.setText(
			Util.convertSecondsToHours(Integer.parseInt(taskBeanList.get(index).getTotalTime()))
			+ "\n"
			+ taskBeanList.get(index).getName());
		row.addView(taskButton);
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		float value = (Float)animation.getAnimatedValue();
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int)(Util.getScreenWidth()*(1+value)/2),
			(int)(Util.getScreenHeight()*(1+value)/2));
		((CustomRowLayout)(root.getChildAt(0))).getChildAt(0).setLayoutParams(layoutParams);
		root.requestLayout();
		root.invalidate();
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
//			startWorkClick((Integer) v.getTag());
			animationIndex = ((BaseButton) v).getIndex();
			animator.start();
		}
	}

	private void startWorkClick(int index){
		TaskBean taskBean = taskBeanList.get(index);
		Fragment fragment = new WorkFragment(taskBean);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(android.R.id.content, fragment);
		transaction.commit();
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

}
