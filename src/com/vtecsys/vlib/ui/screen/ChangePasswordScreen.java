package com.vtecsys.vlib.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class ChangePasswordScreen extends BaseScreen implements OnClickListener {
	
	private EditText password1;
	private EditText password2;
	private Button changeBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.change_password_screen, null);
		setContentView(root);
		initializeViews(root);
		
		if (!isLoggedIn) {
			Intent intent = new Intent(this, LoginScreen.class);
			startActivityForResult(intent, REQUEST_LOGIN);
		}
	}
	
	@Override
	protected void initializeViews(View root) {
		super.initializeViews(root);
		password1 = (EditText) findViewById(R.id.password1);
		password1.setHint(locale.get(LocaleManager.ENTER_NEW_PASSWORD));
		password2 = (EditText) findViewById(R.id.password2);
		password2.setHint(locale.get(LocaleManager.RE_ENTER_NEW_PASSWORD));
		changeBtn = (Button) findViewById(R.id.changeBtn);
		changeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.changeBtn) {
			String pass1 = password1.getText().toString();
			String pass2 = password2.getText().toString();
			if (!pass1.equals(pass2)) {
				Toast.makeText(this, locale.get(LocaleManager.PASSWORDS_NOT_MATCH),
					Toast.LENGTH_SHORT).show();
			} else {
				if (Utilities.isConnectionAvailable(this)) {
					requestChangePassword();
				} else {
					showConnectionErrorDialog();
				}
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LOGIN) {
			if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}
	
	private void requestChangePassword() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_CHANGE_PASSWORD);
		intent.putExtra(ApiData.PARAM_ID, settings.getString(Settings.MEMBER_ID));
		intent.putExtra(ApiData.PARAM_PASSWD, settings.getString(Settings.PASSWORD));
		intent.putExtra(ApiData.PARAM_NEW_PWD, password1.getText().toString().trim());
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(false);
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (ApiData.COMMAND_CHANGE_PASSWORD.equalsIgnoreCase(apiResponse.getRequestName())) {
			if (apiStatus == ApiService.API_STATUS_SUCCESS) {
				if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
					settings.setString(Settings.PASSWORD, password1.getText().toString().trim());
					Toast.makeText(this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
					finish();
				} else {
					DialogUtils.showDialog(this, locale.get(LocaleManager.ERROR),
						apiResponse.getMessage());
				}
			}
		}
	}

}
