package com.vtecsys.vlib.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.ui.screen.BaseScreen;
import com.vtecsys.vlib.util.Utilities;

public class AboutFragment extends BaseFragment {
	
	private TextView about;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.about_fragment, null);
		initializeViews(rootView);
		return rootView;
	}
	
	@Override
	protected void initializeViews(View rootView) {
		super.initializeViews(rootView);
		about = (TextView) rootView.findViewById(R.id.about);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (Utilities.isConnectionAvailable(getActivity())) {
			requestAbout();
		} else {
			((BaseScreen) getActivity()).showConnectionErrorDialog();
		}
	}
	
	private void requestAbout() {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiData.COMMAND_ABOUT);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		getActivity().startService(intent);
		showProgress(true);
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				String aboutText = (String) apiResponse.getData();
				about.setText(aboutText);
			}
		}
	}

}
