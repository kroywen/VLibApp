package com.vtecsys.vlib.ui.screen;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiResponseReceiver;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.api.OnApiResponseListener;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.DialogUtils;

public class BaseScreen extends Activity implements OnApiResponseListener {
	
	protected ApiResponseReceiver responseReceiver;
	public static String appTitle;
	protected Settings settings;
	protected View mainContent;
	protected View progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new Settings(this);
		
		setTitle(appTitle);
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		IntentFilter intentFilter = new IntentFilter(ApiService.ACTION_API_RESULT);
		responseReceiver = new ApiResponseReceiver(this);
		LocalBroadcastManager.getInstance(this).registerReceiver(
			responseReceiver, intentFilter);
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.build();
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
			.defaultDisplayImageOptions(options)
			.build();
		ImageLoader.getInstance().init(configuration);
	}
	
	protected void initializeViews() {
		progress = findViewById(R.id.progress);
		mainContent = findViewById(R.id.main_content);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(responseReceiver);
	}
	
	protected void showProgress(boolean hideContent) {
		if (progress != null) {
			progress.setVisibility(View.VISIBLE);
			if (hideContent && mainContent != null) {
				mainContent.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	protected void hideProgress() {
		if (progress != null) {
			progress.setVisibility(View.INVISIBLE);
		}
		if (mainContent != null) {
			mainContent.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		// TODO Auto-generated method stub
		
	}
	
	protected void showConnectionErrorDialog() {
		DialogUtils.showErrorDialog(this,
			R.string.connection_error_title, 
			R.string.connection_error_message,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			},
			new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			}
		);
	}

}
