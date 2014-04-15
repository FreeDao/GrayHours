package com.withparadox2.grayhours.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.ui.analysis.LinePlotView;
import com.withparadox2.grayhours.utils.DebugConfig;
import com.withparadox2.grayhours.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by withparadox2 on 14-3-2.
 */
public class AnalysisFragment extends Fragment implements ActionBar.OnNavigationListener{
	private LinePlotView linePlotView;
	private TextView textView;

	private List<TaskBean> taskBeanList;
	public AnalysisFragment(List<TaskBean> taskBeanList){
		this.taskBeanList = taskBeanList;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayList<String> itemList = new ArrayList<String>();
		for (TaskBean taskBean:taskBeanList){
			itemList.add(taskBean.getName());
		}
		ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1, android.R.id.text1, itemList);
		actionBar.setListNavigationCallbacks(aAdpt, this);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.analysisfragment_layout, container, false);
		linePlotView = (LinePlotView) view.findViewById(R.id.lineplotview);
		textView = (TextView) view.findViewById(R.id.task_name_text);
		return view;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		TaskBean taskBean = taskBeanList.get(itemPosition);
		textView.setText(taskBean.getName()+"  "+ Util.convertSecondsToHours(Integer.parseInt(taskBean.getTotalTime())));
		linePlotView.getData(itemPosition);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DebugConfig.log("home click");

		switch (item.getItemId()){

			case android.R.id.home:
				getFragmentManager().popBackStack();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
