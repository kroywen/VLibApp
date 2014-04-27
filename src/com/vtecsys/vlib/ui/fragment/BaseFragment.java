package com.vtecsys.vlib.ui.fragment;

import android.app.Fragment;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiResponseReceiver;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.api.OnApiResponseListener;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.storage.database.DatabaseManager;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class BaseFragment extends Fragment implements OnApiResponseListener {
	
	protected View mainContent;
	protected View progress;
	protected Settings settings;
	protected LocaleManager locale;
	protected ApiResponseReceiver responseReceiver;
	protected DatabaseManager dbManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new Settings(getActivity());
		locale = LocaleManager.getInstance();
		dbManager = DatabaseManager.newInstance(getActivity());
	}
	
	protected void initializeViews(View rootView) {
		progress = rootView.findViewById(R.id.progress);
		mainContent = rootView.findViewById(R.id.main_content);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter(ApiService.ACTION_API_RESULT);
		responseReceiver = new ApiResponseReceiver(this);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
			responseReceiver, intentFilter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(responseReceiver);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		locale.apply(getView());
		Utilities.setFontSize(getView(), Utilities.getFontSize(
			settings.getInt(Settings.FONT_SIZE)));
	}
	
	protected void showProgress(boolean hideContent) {
		if (progress != null) {
			progress.setVisibility(View.VISIBLE);
			if (hideContent && mainContent != null) {
				mainContent.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	protected void hideProgress() {
		if (progress != null) {
			progress.setVisibility(View.INVISIBLE);
		}
		if (mainContent != null) {
			mainContent.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {}

}
