package com.vtecsys.vlib.ui.screen;

import android.content.Intent;
import android.os.Bundle;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class SplashScreen extends BaseScreen {
	
	public static final int SPLASH_TIME = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		if (Utilities.isConnectionAvailable(this)) {
			requestSiteName();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	private void requestSiteName() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_SITENAME);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				appTitle = (String) apiResponse.getData();
				
				Intent intent = new Intent(this, MainScreen.class);
				startActivity(intent);
				finish();
			}
		}
	}
	
}
