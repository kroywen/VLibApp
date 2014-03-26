package com.vtecsys.vlib.ui.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiResponseReceiver;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.api.MockApi;
import com.vtecsys.vlib.api.OnApiResponseListener;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.Utilities;

public class SplashScreen extends BaseScreen implements OnApiResponseListener {
	
	public static final int SPLASH_TIME = 2000;
	
	private ApiResponseReceiver responseReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		IntentFilter intentFilter = new IntentFilter(ApiService.ACTION_API_RESULT);
		responseReceiver = new ApiResponseReceiver(this);
		LocalBroadcastManager.getInstance(this).registerReceiver(
			responseReceiver, intentFilter);
		
		if (Utilities.isConnectionAvailable(this)) {
//			requestSiteName(); // TODO uncomment
			MockApi.requestSitename(this); // TODO remove
		} else {
			showConnectionErrorDialog();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(responseReceiver);
	}
	
	private void requestSiteName() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_SITENAME);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
	}
	
	private void showConnectionErrorDialog() {
		DialogUtils.showErrorDialog(this,
			R.string.connection_error_title, 
			R.string.connection_error_message,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			},
			new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			}
		);
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				Intent intent = new Intent(this, MainScreen.class);
				
				String siteName = (String) apiResponse.getData();
				intent.putExtra(MainScreen.APP_TITLE, siteName);
				
				startActivity(intent);
				finish();
			}
		}
	}
	
}
