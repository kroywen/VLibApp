package com.vtecsys.vlib.ui.screen;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.vtecsys.vlib.R;

public class LoginScreen extends BaseScreen implements OnClickListener {
	
	private EditText memberId;
	private EditText password;
	private CheckBox remember;
	private Button loginBtn;
	private Button resetBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		initializeViews();
	}
	
	@Override
	protected void initializeViews() {
		super.initializeViews();
		memberId = (EditText) findViewById(R.id.memberId);
		password = (EditText) findViewById(R.id.password);
		remember = (CheckBox) findViewById(R.id.remember);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(this);
		resetBtn = (Button) findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(this);		
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
			// TODO login
			break;
		case R.id.resetBtn:
			reset();
			break;
		}
	}
	
	private void reset() {
		memberId.setText(null);
		password.setText(null);
		remember.setChecked(false);
	}

}
