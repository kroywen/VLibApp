package com.vtecsys.vlib.ui.screen;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Notices;
import com.vtecsys.vlib.model.result.SiteNameResult;
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
		String predue = Utilities.getPreDueDaysNotificationParam(
			settings.getInt(Settings.PRE_DUE_DAYS_NOTIFICATION));
		if (!TextUtils.isEmpty(predue)) {
			intent.putExtra(ApiData.PARAM_PREDUE, predue);
		}
		intent.putExtra(ApiData.PARAM_N_D, settings.getBoolean(Settings.DUE_DATE_NOTIFICATION) ? "y" : "n");
		intent.putExtra(ApiData.PARAM_N_OD, settings.getBoolean(Settings.OVERDUE_DATE_NOTIFICATION) ? "y" : "n");
		intent.putExtra(ApiData.PARAM_N_COLL, settings.getBoolean(Settings.COLLECTION_NOTIFICATION) ? "y" : "n");
		startService(intent);
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				if (ApiData.COMMAND_SITENAME.equalsIgnoreCase(apiResponse.getRequestName())) {
					SiteNameResult result = (SiteNameResult) apiResponse.getData();
					settings.setString(Settings.APP_TITLE, result.getSiteName());
					settings.setString(Settings.WEB_OPAC_URL, result.getUrl());
					setTitle(result.getSiteName());
					String memberID = settings.getString(Settings.MEMBER_ID);
					if (TextUtils.isEmpty(memberID)) {
						startMainScreen(null);
					} else {
						requestAlerts();
					}
				} else if (ApiData.COMMAND_CHECK_ALERTS.equalsIgnoreCase(apiResponse.getRequestName())) {
					Notices notices = (Notices) apiResponse.getData();
					startMainScreen(notices);
				}
			}
		}
	}
	
	private void startMainScreen(Notices notices) {
		Intent intent = new Intent(this, MainScreen.class);
		intent.putExtra("notices", notices);
		startActivity(intent);
		finish();
	}
	
}
