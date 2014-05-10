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
import com.withparadox2.grayhours.support.AnalysisTool;
import com.withparadox2.grayhours.support.CalendarTool;
import com.withparadox2.grayhours.support.LoadData;
import com.withparadox2.grayhours.ui.analysis.AnalysisScrollAysn;
import com.withparadox2.grayhours.ui.analysis.githubview.GithubView;
import com.withparadox2.grayhours.ui.analysis.LinePlotView;
import com.withparadox2.grayhours.utils.Util;

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
	private boolean initializing = true;
	private int index;
	private TextView totalTimeTextView;
	private TextView maxTimeTextView;
	private TextView averageTimeTextView;
	private TextView usedDaysTextView;
	public AnalysisFragment(List<TaskBean> taskBeanList, int index){
		this.taskBeanList = taskBeanList;
		analysisScrollAysn = new AnalysisScrollAysn();
		this.index = index;
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
		actionBar.setSelectedNavigationItem(index);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.analysisfragment_layout, container, false);
		linePlotView = (LinePlotView) view.findViewById(R.id.lineplotview);
		githubView = (GithubView) view.findViewById(R.id.githubview);
		githubView.setUpdateDateTextListener(new GithubView.UpdateDateTextListener() {
			@Override
			public void setDateText(String text) {
				textView.setText(text);
			}
		});
		githubView.setLinePlotView(linePlotView);
		linePlotView.setGithubView(githubView);
		textView = (TextView) view.findViewById(R.id.task_name_text);
		totalTimeTextView = (TextView) view.findViewById(R.id.total_time);
		maxTimeTextView = (TextView) view.findViewById(R.id.max_time);
		averageTimeTextView = (TextView) view.findViewById(R.id.average_time);
		usedDaysTextView = (TextView) view.findViewById(R.id.used_days);

		return view;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		getData(itemPosition);
		index = itemPosition;
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
		map = map1;
		linePlotView.setIndex(index);
		linePlotView.setData(map1);
		githubView.setData();
		updateInfomationText();
	}

	private void updateInfomationText(){
		totalTimeTextView.setText("total time: "+Util.convertSecondsToHoursMinutes(Integer.parseInt(taskBeanList.get(index).getTotalTime())));
		maxTimeTextView.setText("max time: "+Util.convertSecondsToHoursMinutes(AnalysisTool.MAX_TOTAL_MINUTES*60));
		usedDaysTextView.setText("used days: "+map.size());
		averageTimeTextView.setText("average time: "+Util.convertSecondsToHoursMinutes(Integer.parseInt(taskBeanList.get(index).getTotalTime())/map.size()));
	}
}
