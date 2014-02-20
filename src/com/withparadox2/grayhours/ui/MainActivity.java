package com.withparadox2.grayhours.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
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
		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, new PanelFragment())
				.commit();
	}
}
