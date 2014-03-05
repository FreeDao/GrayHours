package com.withparadox2.grayhours.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.ui.analysis.LinePlotView;

/**
 * Created by withparadox2 on 14-3-2.
 */
public class AnalysisFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.analysisfragment_layout, container, false);
//		View view = new LinePlotView(getActivity());
		return view;
	}
}
