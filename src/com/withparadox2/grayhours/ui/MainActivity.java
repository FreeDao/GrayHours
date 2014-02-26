package com.withparadox2.grayhours.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import com.withparadox2.grayhours.R;

public class MainActivity extends Activity {
	private ActionBar actionBar;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.setTitle(getString(R.string.app_name));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		Fragment fragmentToStart;
		actionBar.setIcon(android.R.color.transparent);
		Log.d("ttttttttttttttt", ""+UpdateWidgetService.START_FLAG + "== mainActivity");
		if(UpdateWidgetService.isMyServiceRunning(getApplicationContext()) ){
			fragmentToStart = new WorkFragment(UpdateWidgetService.getTaskBean());
		} else {
			fragmentToStart = new PanelFragment();
		}
		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content,fragmentToStart)
				.commit();
	}
}
