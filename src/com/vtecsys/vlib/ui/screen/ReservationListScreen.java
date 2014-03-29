package com.vtecsys.vlib.ui.screen;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.adapter.ReservationListAdapter;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Patron;
import com.vtecsys.vlib.model.Reservation;
import com.vtecsys.vlib.model.result.PatronAccountResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class ReservationListScreen extends BaseScreen {
	
	private TextView memberId;
	private ListView listView;
	private TextView emptyView;
	
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
		memberId = (TextView) findViewById(R.id.memberId);
		listView = (ListView) findViewById(R.id.listView);
		emptyView = (TextView) findViewById(R.id.emptyView);
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
				PatronAccountResult result = (PatronAccountResult)
					apiResponse.getData();
				Patron patron = result.getPatron();
				
				String id = null;
				if (patron != null) {
					id = getString(R.string.member_id_pattern, patron.getId());
				} else {
					id = settings.getString(Settings.MEMBER_ID);
				}
				memberId.setText(id);
				
				LayoutInflater inflater = (LayoutInflater) 
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View headerView = inflater.inflate(R.layout.reservation_list_header, null);
				listView.addHeaderView(headerView, null, false);
				
				List<Reservation> reservations = result.getReservations();
				ReservationListAdapter adapter = 
					new ReservationListAdapter(this, reservations);
				listView.setAdapter(adapter);
				listView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
			} else {
				listView.setVisibility(View.GONE);
				listView.setAdapter(null);
				emptyView.setVisibility(View.VISIBLE);
				emptyView.setText(apiResponse.getMessage());
			}
		}
	}
	
}
