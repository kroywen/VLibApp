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

public class BaseFragment extends Fragment implements OnApiResponseListener {
	
	protected View progress;
	protected Settings settings;
	protected ApiResponseReceiver responseReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new Settings(getActivity());
		
		IntentFilter intentFilter = new IntentFilter(ApiService.ACTION_API_RESULT);
		responseReceiver = new ApiResponseReceiver(this);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
			responseReceiver, intentFilter);
	}
	
	protected void initializeViews(View rootView) {
		progress = rootView.findViewById(R.id.progress);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(responseReceiver);
	}
	
	protected void showProgress() {
		if (progress != null) {
			progress.setVisibility(View.VISIBLE);
		}
	}
	
	protected void hideProgress() {
		if (progress != null) {
			progress.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		// TODO Auto-generated method stub
		
	}

}
