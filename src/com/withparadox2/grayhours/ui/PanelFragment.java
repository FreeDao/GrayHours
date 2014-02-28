package com.withparadox2.grayhours.ui;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.ui.custom.AddTaskButton;
import com.withparadox2.grayhours.ui.custom.TaskButton;
import com.withparadox2.grayhours.utils.Util;

import java.util.List;

/**
 * Created by withparadox2 on 14-2-20.
 */
public class PanelFragment extends BaseFragment{
	private List<TaskBean> taskBeanList;
	private View root;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.panelfragment_layout, container, false);
		buildView(root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
	}

	private void updateList(){
		taskBeanList = DatabaseManager.getInstanse().getTaskList();
	}

	private void buildView(View root){
		updateList();
		TableRow row1 = (TableRow) root.findViewById(R.id.first_row_id);
		TableRow row2 = (TableRow) root.findViewById(R.id.second_row_id);
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

	private void setAddTaskButtonView(TableRow row){
		AddTaskButton addTaskButton = new AddTaskButton(getActivity());
		addTaskButton.setOnClickListener(new AddOnClickListener());
		addTaskButton.setText("add");
		row.addView(addTaskButton);
	}

	private void setTaskButtonView(TableRow row, int index){
		TaskButton taskButton = new TaskButton(getActivity());
		taskButton.setTag(index);
		taskButton.setOnClickListener(new StratWorkOnClickListener());
		taskButton.setText(
			Util.convertSecondsToHours(Integer.parseInt(taskBeanList.get(index).getTotalTime()))
			+ "\n"
			+ taskBeanList.get(index).getName());
		row.addView(taskButton);
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
			startWorkClick((Integer) v.getTag());
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
