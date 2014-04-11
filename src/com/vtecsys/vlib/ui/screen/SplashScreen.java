package com.vtecsys.vlib.ui.screen;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Notices;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class SplashScreen extends BaseScreen {
	
	public static final int SPLASH_TIME = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
		
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
	
	private void requestAlerts() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_CHECK_ALERTS);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		intent.putExtra(ApiData.PARAM_ID, settings.getString(Settings.MEMBER_ID));
		intent.putExtra(ApiData.PARAM_PASSWD, settings.getString(Settings.PASSWORD));
		String predue = getPreDueDaysNotificationParam(
			settings.getInt(Settings.PRE_DUE_DAYS_NOTIFICATION));
		if (!TextUtils.isEmpty(predue)) {
			intent.putExtra(ApiData.PARAM_PREDUE, predue);
		}
		intent.putExtra(ApiData.PARAM_N_D, settings.getBoolean(Settings.DUE_DATE_NOTIFICATION) ? "y" : "n");
		intent.putExtra(ApiData.PARAM_N_OD, settings.getBoolean(Settings.OVERDUE_DATE_NOTIFICATION) ? "y" : "n");
		intent.putExtra(ApiData.PARAM_N_COLL, settings.getBoolean(Settings.COLLECTION_NOTIFICATION) ? "y" : "n");
		startService(intent);
	}
	
	private String getPreDueDaysNotificationParam(int position) {
		switch (position) {
		case 0:
			return "none";
		case 1:
			return "3";
		case 2:
			return "7";
		default:
			return null;
		}
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				if (ApiData.COMMAND_SITENAME.equalsIgnoreCase(apiResponse.getRequestName())) {
					appTitle = (String) apiResponse.getData();
					setTitle(appTitle);
					requestAlerts();
				} else if (ApiData.COMMAND_CHECK_ALERTS.equalsIgnoreCase(apiResponse.getRequestName())) {
					Notices notices = (Notices) apiResponse.getData();
					showAlerts(notices);
					
					Intent intent = new Intent(this, MainScreen.class);
					startActivity(intent);
					finish();
				}
			}
		}
	}
	
	private void showAlerts(Notices notices) {
		if (notices == null) {
			return;
		}
		if (notices.hasPredue()) {
			Toast.makeText(this, notices.getPredue(), Toast.LENGTH_LONG).show();
		}
		if (notices.hasDue()) {
			Toast.makeText(this, notices.getDue(), Toast.LENGTH_LONG).show();
		} 
		if (notices.hasOverdue()) {
			Toast.makeText(this, notices.getOverdue(), Toast.LENGTH_LONG).show();
		}
		if (notices.hasCollection()) {
			Toast.makeText(this, notices.getCollection(), Toast.LENGTH_LONG).show();
		}
	}
	
}
