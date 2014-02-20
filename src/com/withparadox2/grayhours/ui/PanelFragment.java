package com.withparadox2.grayhours.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.ui.custom.AddTaskButton;
import com.withparadox2.grayhours.ui.custom.TaskButton;

import java.util.List;

/**
 * Created by Administrator on 14-2-20.
 */
public class PanelFragment extends BaseFragment{
	private List<TaskBean> taskBeanList;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.panelfragment_layout, container, false);
		taskBeanList = DatabaseManager.getInstanse().getTaskList();
		buildView(view);
		return view;
	}

	private void buildView(View root){
		TableRow row1 = (TableRow) root.findViewById(R.id.first_row_id);
		TableRow row2 = (TableRow) root.findViewById(R.id.second_row_id);
		switch (taskBeanList.size()){
			case 0:
				return;
			case 1:
				TaskButton taskButton = new TaskButton(getActivity());
				AddTaskButton addTaskButton = new AddTaskButton(getActivity());
				row1.addView(taskButton);
				row1.addView(addTaskButton);
				break;

		}

	}
}
