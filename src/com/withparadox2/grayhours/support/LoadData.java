package com.withparadox2.grayhours.support;

import android.content.AsyncTaskLoader;
import android.os.AsyncTask;

import java.util.Map;

/**
 * Created by Administrator on 14-4-15.
 */
public class LoadData extends AsyncTask<Integer, Integer, Map> {
	public interface LoadFinishedCallback{
		public void loadFinishedCallback(Map map);
	}

	private LoadFinishedCallback callback;
	private Map<Integer, Integer> map;

	public LoadData(LoadFinishedCallback callback){
		this.callback = callback;
	}

	@Override
	protected Map doInBackground(Integer... params) {
		map = AnalysisTool.getDataMap(params[0]);
		return map;
	}

	@Override
	protected void onPostExecute(Map map) {
		callback.loadFinishedCallback(map);
	}
}
