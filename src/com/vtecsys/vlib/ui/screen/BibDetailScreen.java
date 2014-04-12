package com.vtecsys.vlib.ui.screen;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Tag;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class BibDetailScreen extends BaseScreen {
	
	private LinearLayout tagContainer;
	
	private String rid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bib_detail_screen);
		initializeViews();
		
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		rid = intent.getStringExtra(ApiData.PARAM_RID);
		
		if (Utilities.isConnectionAvailable(this)) {
			requestBibDetail(true);
		} else {
			showConnectionErrorDialog();
		}
	}
	
	@Override
	protected void initializeViews() {
		super.initializeViews();
		tagContainer = (LinearLayout) findViewById(R.id.tagContainer);
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
	
	private void requestBibDetail(boolean hideContent) {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_BIB_DETAIL);
		intent.putExtra(ApiData.PARAM_RID, rid);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(hideContent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (ApiData.COMMAND_BIB_DETAIL.equalsIgnoreCase(apiResponse.getRequestName())) {
			if (apiStatus == ApiService.API_STATUS_SUCCESS) {
				if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
					List<Tag> tags = (List<Tag>) apiResponse.getData();
					populateTagList(tags);
				}
			}
		}
	}
	
	private void populateTagList(List<Tag> tags) {
		tagContainer.removeAllViews();
		if (Utilities.isEmpty(tags)) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) 
			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		for (Tag tag : tags) {
			TextView tv = (TextView) inflater.inflate(R.layout.bib_detail_tag, null);
			String text = "<b>" + tag.getCaption() + ":</b> " + tag.getContents();
			tv.setText(Html.fromHtml(text));
			tagContainer.addView(tv);
		}
	}

}
