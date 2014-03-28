package com.vtecsys.vlib.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class AboutScreen extends BaseScreen {
	
	private TextView about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_screen);
		initializeViews();
		
		if (Utilities.isConnectionAvailable(this)) {
			requestAbout();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	@Override
	protected void initializeViews() {
		super.initializeViews();
		about = (TextView) findViewById(R.id.about);
	}
	
	private void requestAbout() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_ABOUT);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
