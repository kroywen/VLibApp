package com.vtecsys.vlib.ui.screen;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.vtecsys.vlib.ui.dialog.RenewAllLoanDialog;
import com.vtecsys.vlib.ui.dialog.RenewAllLoanDialog.OnRenewAllClickListener;
import com.vtecsys.vlib.ui.dialog.RenewLoanDialog;
import com.vtecsys.vlib.ui.dialog.RenewLoanDialog.OnRenewClickListener;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class LoanActivitiesScreen extends BaseScreen 
	implements OnRenewClickListener, OnRenewAllClickListener,
	OnClickListener 
{
	
	private TextView memberId;
	private View refreshBtn;
	private ListView listView;
	private TextView emptyView;
	private View listHeaderView;
	private Button allBtn;
	
	private Loan requestedLoan;
	private List<Loan> loans;
	private List<Loan> requestedLoanList;
	private boolean renewAll;
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.loan_activities_screen, null);
		setContentView(root);
		initializeViews(root);
		
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
	
	@SuppressLint("InflateParams")
	@Override
	protected void initializeViews(View root) {
		super.initializeViews(root);
		memberId = (TextView) findViewById(R.id.memberId);
		
		refreshBtn = findViewById(R.id.refreshBtn);
		refreshBtn.setOnClickListener(this);
		
		listView = (ListView) findViewById(R.id.listView);
		emptyView = (TextView) findViewById(R.id.emptyView);
		
		LayoutInflater inflater = (LayoutInflater) 
			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listHeaderView = inflater.inflate(R.layout.loan_activity_header, null);
		locale.apply(listHeaderView);
		float fontSize = Utilities.getFontSize(
			settings.getInt(Settings.FONT_SIZE)) - 4.0f;
		Utilities.setFontSize(listHeaderView, fontSize);
		
		allBtn = (Button) listHeaderView.findViewById(R.id.allBtn);
		allBtn.setOnClickListener(this);
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
					String color = String.format("#%06X", 0xFFFFFF & 
						getResources().getColor(R.color.highlight));
					if (patron != null) {
						
						id = "<font color=\"" + color +
							"\">" + locale.get(LocaleManager.MEMBER_ID) + 
							":</font> " + patron.getId();
					} else {
						id = "<font color=\"" + color +
							"\">" + locale.get(LocaleManager.MEMBER_ID) + 
							":</font> " + settings.getString(Settings.MEMBER_ID);
					}
					memberId.setText(Html.fromHtml(id));
					
					if (listView.getHeaderViewsCount() == 0) {
						listView.addHeaderView(listHeaderView, null, false);
					}
					
					loans = result.getLoans();
					LoanActivityAdapter adapter = new LoanActivityAdapter(this, loans);
					listView.setAdapter(adapter);
					listView.setVisibility(View.VISIBLE);
					emptyView.setVisibility(View.GONE);
					
					boolean canRenewAll = canRenewAll();
					allBtn.setEnabled(canRenewAll);
				} else {
					listView.setVisibility(View.GONE);
					listView.setAdapter(null);
					emptyView.setVisibility(View.VISIBLE);
					emptyView.setText(apiResponse.getMessage());
				}
			}
		} else if (ApiData.COMMAND_RENEW_LOAN.equalsIgnoreCase(apiResponse.getRequestName())) {
			if (renewAll) {
				if (apiResponse.getStatus() != ApiResponse.STATUS_OK) {
					DialogUtils.showDialog(this, locale.get(LocaleManager.ERROR),
						apiResponse.getMessage());
				}
				int currentIndex = requestedLoanList.indexOf(requestedLoan);
				if (currentIndex < requestedLoanList.size()-1) {
					currentIndex++;
					requestedLoan = requestedLoanList.get(currentIndex);
					requestRenewLoan();
				} else {
					requestLoanActivities(false);
				}
			} else {
				if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
					String newDueDate = Utilities.extractDate(apiResponse.getMessage());
					requestedLoan.setDueDate(newDueDate);
					showRenewLoanSuccessDialog();
				} else {
					DialogUtils.showDialog(this, locale.get(LocaleManager.ERROR),
						apiResponse.getMessage());
				}
			}
		}
	}
	
	private boolean canRenewAll() {
		if (Utilities.isEmpty(loans)) {
			return false;
		}
		
		for (Loan loan : loans) {
			if (loan.canRenew()) {
				return true;
			}
		}
		
		return false;
	}
	
	public void tryRenewLoan(Loan loan) {
		requestedLoan = loan;
		renewAll = false;
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
		requestLoanActivities(false);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.allBtn:
			boolean canRenewAll = canRenewAll();
			if (canRenewAll) {
				tryRenewAllLoans();
			}
			break;
		case R.id.refreshBtn:
			if (Utilities.isConnectionAvailable(this)) {
				requestLoanActivities(false);
			} else {
				showConnectionErrorDialog();
			}
			break;
		}
	}
	
	private void tryRenewAllLoans() {
		requestedLoanList = getLoanListForRenew();
		requestedLoan = requestedLoanList.get(0);
		if (Utilities.isConnectionAvailable(this)) {
			showRequestRenewAllLoanDialog();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	private List<Loan> getLoanListForRenew() {
		if (Utilities.isEmpty(loans)) {
			return null;
		}
		List<Loan> result = new ArrayList<Loan>();
		for (Loan loan : loans) {
			if (loan.canRenew()) {
				result.add(loan);
			}
		}
		return result;
	}
	
	private void showRequestRenewAllLoanDialog() {
		RenewAllLoanDialog dialog = new RenewAllLoanDialog();
		dialog.setRetainInstance(true);
		dialog.setLoans(requestedLoanList);
		dialog.show(getFragmentManager(), "renewAllLoan");
	}

	@Override
	public void onRenewAllYesClick(DialogFragment dialog, List<Loan> loans) {
		renewAll = true;
		requestRenewLoan();
	}

	@Override
	public void onRenewAllNoClick(DialogFragment dialog) {}

}
