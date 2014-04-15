package com.withparadox2.grayhours.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.withparadox2.grayhours.R;
import com.withparadox2.grayhours.utils.DebugConfig;

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

		fragmentToStart = new PanelFragment();
//		fragmentToStart = new AnalysisFragment();
		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content,fragmentToStart)
				.commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			/**
			 * when item is clicked, android framework will call activity first, then fragment,
			 * because home button has returned true in BaseActivity, here has to return false
			 * forcing ItemListFragment to call it.
			 */
			case android.R.id.home:
				return false;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
