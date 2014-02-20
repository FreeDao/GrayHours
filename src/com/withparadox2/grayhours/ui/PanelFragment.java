package com.withparadox2.grayhours.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import com.withparadox2.grayhours.R;

/**
 * Created by Administrator on 14-2-20.
 */
public class PanelFragment extends BaseFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.panelfragment_layout, container, false);
		buildView(view);
		return view;
	}

	private void buildView(View root){

	}
}
