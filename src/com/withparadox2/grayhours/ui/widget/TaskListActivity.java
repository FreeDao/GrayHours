package com.withparadox2.grayhours.ui.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.dao.DatabaseHelper;
import com.withparadox2.grayhours.dao.DatabaseManager;
import com.withparadox2.grayhours.ui.UpdateWidgetService;
import com.withparadox2.grayhours.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by withparadox2 on 14-2-26.
 */
public class TaskListActivity extends Activity{
	private ListView listView;
	private List<TaskBean> list = DatabaseManager.getInstanse().getTaskList();
	private Activity that;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listView = new ListView(this);
		that = this;
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getList()));
		setContentView(listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getApplicationContext(), UpdateWidgetService.class);
				UpdateWidgetService.setTaskBean(list.get(position));
				getApplicationContext().startService(i);
				that.finish();
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

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View v = inflater.inflate(R.layout.dialog_activity_list_item, null);
			TextView textView = (TextView) v.findViewById(R.id.task_name_text);
			textView.setText(list.get(position).getName());
			return v;
		}
	}
}
