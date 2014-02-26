package com.withparadox2.grayhours.ui.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseHelper;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.ui.UpdateWidgetService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-2-26.
 */
public class TaskListActivity extends Activity{
	private ListView listView;
	private List<TaskBean> list = DatabaseManager.getInstanse().getTaskList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listView = new ListView(this);

		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getList()));
		setContentView(listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getApplicationContext(), UpdateWidgetService.class);
				UpdateWidgetService.setTaskBean(list.get(position));
				getApplicationContext().startService(i);
			}
		});
	}

	private List<String> getList(){
		List<String> result = new ArrayList<String>();
		for (TaskBean taskBean:list){
			result.add(taskBean.getName());
		}
		return result;
	}
}
