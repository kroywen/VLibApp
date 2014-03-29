package com.vtecsys.vlib.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.vtecsys.vlib.R;

public class ChangePasswordScreen extends BaseScreen implements OnClickListener {
	
	private EditText password1;
	private EditText password2;
	private Button changeBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_screen);
		initializeViews();
		
		if (!isLoggedIn) {
			Intent intent = new Intent(this, LoginScreen.class);
			startActivityForResult(intent, REQUEST_LOGIN);
		}
	}
	
	@Override
	protected void initializeViews() {
		super.initializeViews();
		password1 = (EditText) findViewById(R.id.password1);
		password2 = (EditText) findViewById(R.id.password2);
		changeBtn = (Button) findViewById(R.id.changeBtn);
		changeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.changeBtn) {
			String pass1 = password1.getText().toString();
			String pass2 = password2.getText().toString();
			if (!TextUtils.isEmpty(pass1) && !TextUtils.isEmpty(pass2) &&
				pass1.equals(pass2)) 
			{
				// TODO change
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

}
