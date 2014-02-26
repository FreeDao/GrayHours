package com.withparadox2.grayhours.ui.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseHelper;
import com.withparadox2.grayhours.dao.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-2-26.
 */
public class TaskListActivity extends Activity{
	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listView = new ListView(this);

		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getList()));
		setContentView(listView);
	}

	private List<String> getList(){
		List<TaskBean> list = DatabaseManager.getInstanse().getTaskList();
		List<String> result = new ArrayList<String>();
		for (TaskBean taskBean:list){
			result.add(taskBean.getName());
		}
		return result;
	}
}
