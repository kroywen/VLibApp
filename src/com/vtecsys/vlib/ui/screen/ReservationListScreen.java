package com.vtecsys.vlib.ui.screen;

import java.util.List;

import android.app.DialogFragment;
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
import com.vtecsys.vlib.ui.dialog.CancelReservationDialog;
import com.vtecsys.vlib.ui.dialog.CancelReservationDialog.OnCancelReservationClickListener;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.Utilities;

public class ReservationListScreen extends BaseScreen implements OnCancelReservationClickListener {
	
	private TextView memberId;
	private ListView listView;
	private TextView emptyView;
	
	private Reservation requestedReservation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_list_screen);
		initializeViews();
		
		if (isLoggedIn) {
			if (Utilities.isConnectionAvailable(this)) {
				requestReservationList(true);
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
	
	private void requestReservationList(boolean hideContent) {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_PATR_ACCOUNT);
		intent.putExtra(ApiData.PARAM_ID, settings.getString(Settings.MEMBER_ID));
		intent.putExtra(ApiData.PARAM_PASSWD, settings.getString(Settings.PASSWORD));
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(hideContent);
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
				requestReservationList(true);
			} else {
				finish();
			}
		}
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (ApiData.COMMAND_PATR_ACCOUNT.equalsIgnoreCase(apiResponse.getRequestName())) {
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
					
					if (listView.getHeaderViewsCount() == 0) {
						LayoutInflater inflater = (LayoutInflater) 
							getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View headerView = inflater.inflate(R.layout.reservation_list_header, null);
						listView.addHeaderView(headerView, null, false);
					}
					
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
		} else if (ApiData.COMMAND_CANCEL_RESERVATION.equalsIgnoreCase(apiResponse.getRequestName())) {
			if (apiStatus == ApiService.API_STATUS_SUCCESS) {
				if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
					requestReservationList(false);
				} else {
					DialogUtils.showDialog(this, getString(R.string.error),
						apiResponse.getMessage());
				}
			}
		}
	}
	
	public void tryCancelReservation(Reservation reservation) {
		requestedReservation = reservation;
		if (reservation == null) {
			return;
		}
		
		if (Utilities.isConnectionAvailable(this)) {
			showRequestCancelReservationDialog();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	private void showRequestCancelReservationDialog() {
		CancelReservationDialog dialog = new CancelReservationDialog();
		dialog.setRetainInstance(true);
		dialog.setReservation(requestedReservation);
		dialog.show(getFragmentManager(), "cancelReservation");
	}
	
	private void requestCancelReservation() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_CANCEL_RESERVATION);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		intent.putExtra(ApiData.PARAM_ID, settings.getString(Settings.MEMBER_ID));
		intent.putExtra(ApiData.PARAM_PASSWD, settings.getString(Settings.PASSWORD));
		intent.putExtra(ApiData.PARAM_TYPE, requestedReservation.getType());
		intent.putExtra(ApiData.PARAM_RID, requestedReservation.getRID());
		intent.putExtra(ApiData.PARAM_ISSUE, requestedReservation.getIssue());
		intent.putExtra(ApiData.PARAM_VOLUME, requestedReservation.getVolume());
		startService(intent);
		showProgress(false);
	}

	@Override
	public void onCancelReservationYesClick(DialogFragment dialog, Reservation reservation) {
		requestCancelReservation();
	}

	@Override
	public void onCancelReservationNoClick(DialogFragment dialog) {}
	
}
