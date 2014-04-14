package com.vtecsys.vlib.ui.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Patron;
import com.vtecsys.vlib.model.result.PatronAccountResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class LoginScreen extends BaseScreen implements OnClickListener, OnCheckedChangeListener {
	
	private EditText memberId;
	private EditText password;
	private CheckBox remember;
	private Button loginBtn;
	private Button resetBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.login_screen, null);
		setContentView(root);
		initializeViews(root);
		updateViews();
	}
	
	@Override
	protected void initializeViews(View root) {
		super.initializeViews(root);
		memberId = (EditText) findViewById(R.id.memberId);
		memberId.setHint(locale.get(LocaleManager.MEMBER_ID));
		password = (EditText) findViewById(R.id.password);
		password.setHint(locale.get(LocaleManager.PASSWORD));
		remember = (CheckBox) findViewById(R.id.remember);
		remember.setOnCheckedChangeListener(this);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(this);
		resetBtn = (Button) findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(this);		
	}
	
	private void updateViews() {
		memberId.setText(settings.getString(Settings.MEMBER_ID));
		boolean rememberPassword = settings.getBoolean(Settings.REMEMBER_PASSWORD);
		remember.setChecked(rememberPassword);
		if (rememberPassword) {
			password.setText(settings.getString(Settings.PASSWORD));
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			hideSoftKeyboard();
			if (Utilities.isConnectionAvailable(this)) {
				login();
			} else {
				showConnectionErrorDialog();
			}
			break;
		case R.id.resetBtn:
			reset();
			break;
		}
	}
	
	private void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) 
			getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(memberId.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
	}
	
	private void login() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_PATR_ACCOUNT);
		intent.putExtra(ApiData.PARAM_ID, memberId.getText().toString().trim());
		intent.putExtra(ApiData.PARAM_PASSWD, password.getText().toString().trim());
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(false);
	}
	
	private void reset() {
		memberId.setText(null);
		password.setText(null);
		remember.setChecked(false);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		settings.setBoolean(Settings.REMEMBER_PASSWORD, isChecked);
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				PatronAccountResult result = (PatronAccountResult) apiResponse.getData();
				Patron patron = result.getPatron();
				settings.setString(Settings.MEMBER_ID, 
					memberId.getText().toString().trim());
				settings.setString(Settings.PASSWORD, 
					password.getText().toString().trim());
				settings.setString(Settings.MEMBER_NAME, patron.getSurname());
				isLoggedIn = true;
				setResult(RESULT_OK);
				finish();
			} else {
				DialogUtils.showDialog(this, locale.get(LocaleManager.ERROR),
					apiResponse.getMessage());
			}
		}
	}

}
