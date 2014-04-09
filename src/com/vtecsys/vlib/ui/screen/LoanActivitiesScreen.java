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
import com.vtecsys.vlib.adapter.LoanActivityAdapter;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Loan;
import com.vtecsys.vlib.model.Patron;
import com.vtecsys.vlib.model.result.PatronAccountResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.ui.dialog.RenewLoanDialog;
import com.vtecsys.vlib.ui.dialog.RenewLoanDialog.OnRenewClickListener;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.Utilities;

public class LoanActivitiesScreen extends BaseScreen implements OnRenewClickListener {
	
	private TextView memberId;
	private ListView listView;
	private TextView emptyView;
	
	private Loan requestedLoan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loan_activities_screen);
		initializeViews();
		
		if (isLoggedIn) {
			if (Utilities.isConnectionAvailable(this)) {
				requestLoanActivities(true);
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
	
	private void requestLoanActivities(boolean hideContent) {
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
				requestLoanActivities(true);
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
					
					LayoutInflater inflater = (LayoutInflater) 
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View headerView = inflater.inflate(R.layout.loan_activity_header, null);
					listView.addHeaderView(headerView, null, false);
					
					List<Loan> loans = result.getLoans();
					LoanActivityAdapter adapter = new LoanActivityAdapter(this, loans);
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
		} else if (ApiData.COMMAND_RENEW_LOAN.equalsIgnoreCase(apiResponse.getRequestName())) {
			if (apiStatus == ApiService.API_STATUS_SUCCESS) {
				if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
					showRenewLoanSuccessDialog();
				} else {
					DialogUtils.showDialog(this, getString(R.string.error),
						apiResponse.getMessage());
				}
			}
		}
	}
	
	public void tryRenewLoan(Loan loan) {
		requestedLoan = loan;
		if (loan == null) {
			return;
		}
		
		if (Utilities.isConnectionAvailable(this)) {
			showRenewLoanDialog();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	private void showRenewLoanDialog() {
		RenewLoanDialog dialog = new RenewLoanDialog();
		dialog.setRetainInstance(true);
		dialog.setMode(RenewLoanDialog.MODE_BEFORE_RENEW);
		dialog.setLoan(requestedLoan);
		dialog.show(getFragmentManager(), "renew_loan");
	}
	
	private void showRenewLoanSuccessDialog() {
		RenewLoanDialog dialog = new RenewLoanDialog();
		dialog.setRetainInstance(true);
		dialog.setMode(RenewLoanDialog.MODE_AFTER_RENEW);
		dialog.setLoan(requestedLoan);
		dialog.show(getFragmentManager(), "renew_loan");
	}
	
	private void requestRenewLoan() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_RENEW_LOAN);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		intent.putExtra(ApiData.PARAM_ID, settings.getString(Settings.MEMBER_ID));
		intent.putExtra(ApiData.PARAM_PASSWD, settings.getString(Settings.PASSWORD));
		intent.putExtra(ApiData.PARAM_ITEMNO, requestedLoan.getItemNumber());
		startService(intent);
		showProgress(false);
	}

	@Override
	public void onRenewYesClick(DialogFragment dialog, Loan loan) {
		requestRenewLoan();
	}

	@Override
	public void onRenewNoClick(DialogFragment dialog) {}

	@Override
	public void onRenewOkClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

}
