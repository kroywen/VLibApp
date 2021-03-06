package com.vtecsys.vlib.ui.screen;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.adapter.BrowseResultAdapter;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Auth;
import com.vtecsys.vlib.model.result.BrowseResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class BrowseResultScreen extends BaseScreen implements OnItemClickListener {
	
	private TextView headerView;
	private TextView infoView;
	private ListView listView;
	private TextView emptyView;
	
	private String term;
	private String sortBy;
	private String browseBy;
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.browse_result_screen, null);
		setContentView(root);
		initializeViews(root);
		
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		
		String[] sortByParams = getResources().getStringArray(R.array.sort_by_params);
		term = intent.getStringExtra(ApiData.PARAM_TERM);
		sortBy = sortByParams[intent.getIntExtra(ApiData.PARAM_SORT_BY, 0)];
		browseBy = intent.getStringExtra(ApiData.PARAM_BROWSE_BY);
		
		String headerTitle = getBrowseByText(browseBy);
		headerView.setText(headerTitle);
		
		if (Utilities.isConnectionAvailable(this)) {
			requestBrowse();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	private String getBrowseByText(String browseByParam) {
		if ("author".equalsIgnoreCase(browseByParam)) {
			return locale.get(LocaleManager.BROWSE_BY_AUTHOR);
		} else if ("subject".equalsIgnoreCase(browseByParam)) {
			return locale.get(LocaleManager.BROWSE_BY_SUBJECT);
		} else if ("series".equalsIgnoreCase(browseByParam)) {
			return locale.get(LocaleManager.BROWSE_BY_SERIES);
		} else {
			return null;
		}
	}
	
	@Override
	protected void initializeViews(View root) {
		super.initializeViews(root);
		
		headerView = (TextView) findViewById(R.id.headerView);
		
		infoView = (TextView) findViewById(R.id.infoView);
		String color = String.format("#%06X", 0xFFFFFF & 
			getResources().getColor(R.color.highlight));
		String info = "<font color=\"" + color +
			"\">0</font> " + locale.get(LocaleManager.RECORDS_LOADED);
		infoView.setText(Html.fromHtml(info));
		
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		emptyView = (TextView) findViewById(R.id.emptyView);
	}
	
	private void requestBrowse() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_BROWSE);
		if (!TextUtils.isEmpty(term)) {
			intent.putExtra(ApiData.PARAM_TERM, term);
		}
		if (!TextUtils.isEmpty(sortBy)) {
			intent.putExtra(ApiData.PARAM_SORT_BY, sortBy);
		}
		intent.putExtra(ApiData.PARAM_BROWSE_BY, browseBy);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(true);
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			String color = String.format("#%06X", 0xFFFFFF & 
				getResources().getColor(R.color.highlight));
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				if (ApiData.COMMAND_BROWSE.equalsIgnoreCase(apiResponse.getRequestName())) {
					Object data = apiResponse.getData();
					if (data != null && data instanceof BrowseResult) {
						BrowseResult result = (BrowseResult) apiResponse.getData();
						List<Auth> authes = result.getAuthes();
						BrowseResultAdapter adapter = new BrowseResultAdapter(this, authes);
						listView.setAdapter(adapter);
						listView.setVisibility(View.VISIBLE);
						emptyView.setVisibility(View.GONE);
						
						String info = "<font color=\"" + color +
							"\">" + result.getLoaded() + "</font> " + locale.get(LocaleManager.RECORDS_LOADED);
						infoView.setText(Html.fromHtml(info));
					}
				}
			} else {
				listView.setVisibility(View.GONE);
				listView.setAdapter(null);
				String info = "<font color=\"" + color +
					"\">0</font> " + locale.get(LocaleManager.RECORDS_LOADED);
				infoView.setText(Html.fromHtml(info));
				emptyView.setVisibility(View.VISIBLE);
				emptyView.setText(apiResponse.getMessage());
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Auth auth = ((BrowseResultAdapter) parent.getAdapter()).getItem(position);
		Intent intent = new Intent(this, SearchResultScreen.class);
		intent.putExtra(ApiData.PARAM_TYPE, ApiData.TYPE_BROWSE);
		intent.putExtra(ApiData.PARAM_TERM, auth.getAuthNo());
		intent.putExtra(ApiData.PARAM_SORT_BY, 1);
		startActivity(intent);
	}

}
