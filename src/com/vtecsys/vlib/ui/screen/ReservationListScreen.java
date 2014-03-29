package com.vtecsys.vlib.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class ReservationListScreen extends BaseScreen {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_list_screen);
		initializeViews();
		
		if (isLoggedIn) {
			if (Utilities.isConnectionAvailable(this)) {
				requestReservationList();
			} else {
				showConnectionErrorDialog();
			}
		} else {
			Intent intent = new Intent(this, LoginScreen.class);
			startActivityForResult(intent, REQUEST_LOGIN);
		}
	}
	
	@Override
	protected void initializeViews() {
		super.initializeViews();
		// TODO
	}
	
	private void requestReservationList() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_PATR_ACCOUNT);
		intent.putExtra(ApiData.PARAM_ID, settings.getString(Settings.MEMBER_ID));
		intent.putExtra(ApiData.PARAM_PASSWD, settings.getString(Settings.PASSWORD));
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(true);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LOGIN) {
			if (resultCode == RESULT_OK) {
				requestReservationList();
			} else {
				finish();
			}
		}
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				Toast.makeText(this, "Response OK", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
