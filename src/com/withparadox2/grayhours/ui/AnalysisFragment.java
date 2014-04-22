package com.withparadox2.grayhours.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.bean.TaskBean;
import com.withparadox2.grayhours.support.LoadData;
import com.withparadox2.grayhours.ui.analysis.AnalysisScrollAysn;
import com.withparadox2.grayhours.ui.analysis.githubview.GithubView;
import com.withparadox2.grayhours.ui.analysis.LinePlotView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by withparadox2 on 14-3-2.
 */
public class AnalysisFragment extends Fragment implements ActionBar.OnNavigationListener, LoadData.LoadFinishedCallback{
	private LinePlotView linePlotView;
	private GithubView githubView;
	private TextView textView;

	private List<TaskBean> taskBeanList;
	private AnalysisScrollAysn analysisScrollAysn;
	public AnalysisFragment(List<TaskBean> taskBeanList){
		this.taskBeanList = taskBeanList;
		analysisScrollAysn = new AnalysisScrollAysn();
	}

	public static Map<Integer, Integer> map;
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
		githubView = (GithubView) view.findViewById(R.id.githubview);
		textView = (TextView) view.findViewById(R.id.task_name_text);
		return view;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		TaskBean taskBean = taskBeanList.get(itemPosition);
		textView.setText(taskBean.getName()+"  "+ String.valueOf(Integer.parseInt(taskBean.getTotalTime())/3600)+"h");
		getData(itemPosition);
		return true;
	}

	public void getData(int index){
		LoadData loadData = new LoadData(this);
		loadData.execute(index);
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

	@Override
	public void loadFinishedCallback(Map map1) {
		linePlotView.setData(map1);
		map = map1;
		githubView.setData();
	}
}
