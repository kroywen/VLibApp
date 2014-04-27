package com.vtecsys.vlib.ui.screen;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.adapter.SearchResultAdapter;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.model.result.SearchResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class SearchResultScreen extends BaseScreen implements OnItemClickListener {
	
	private TextView infoView;
	private ListView listView;
	private TextView emptyView;
	private View touchPictureView;
	
	private String type;
	private String term;
	private String sortBy;
	private String searchBy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.search_result_screen, null);
		setContentView(root);
		initializeViews(root);
		
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		
		String[] sortByParams = getResources().getStringArray(R.array.sort_by_params);
		type = intent.getStringExtra(ApiData.PARAM_TYPE);
		term = intent.getStringExtra(ApiData.PARAM_TERM);
		sortBy = sortByParams[intent.getIntExtra(ApiData.PARAM_SORT_BY, 0)];
		searchBy = intent.getStringExtra(ApiData.PARAM_SEARCH_BY);
				
		if (Utilities.isConnectionAvailable(this)) {
			requestSearch();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	@Override
	protected void initializeViews(View root) {
		super.initializeViews(root);
		
		infoView = (TextView) findViewById(R.id.infoView);
		String info = "<font color=\"" + getResources().getColor(R.color.highlight) +
			"\">0</font> " + locale.get(LocaleManager.RECORDS_FOUND) + 
			", <font color=\"" + getResources().getColor(R.color.highlight)+ 
			"\">0</font> " + locale.get(LocaleManager.RECORDS_LOADED) + ".";
		infoView.setText(Html.fromHtml(info));
		
		touchPictureView = findViewById(R.id.touchPictureView);
		touchPictureView.setVisibility(View.GONE);
		
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		emptyView = (TextView) findViewById(R.id.emptyView);
	}
	
	private void requestSearch() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_SEARCH);
		intent.putExtra(ApiData.PARAM_TYPE, type);
		intent.putExtra(ApiData.PARAM_TERM, term);
		intent.putExtra(ApiData.PARAM_SORT_BY, sortBy);
		intent.putExtra(ApiData.PARAM_SEARCH_BY, searchBy);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(true);
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				Object data = apiResponse.getData();
				if (data != null && data instanceof SearchResult) {
					SearchResult result = (SearchResult) apiResponse.getData();
					List<Book> books = result.getBooks();
					SearchResultAdapter adapter = new SearchResultAdapter(this, books);
					listView.setAdapter(adapter);
					listView.setVisibility(View.VISIBLE);
					emptyView.setVisibility(View.GONE);
					String info = "<font color=\"" + getResources().getColor(R.color.highlight) +
						"\">" + result.getHits() + "</font> " + locale.get(LocaleManager.RECORDS_FOUND) + 
						", " + "<font color=\"" + getResources().getColor(R.color.highlight) + 
						"\">" + result.getLoaded() + "</font> " +
						locale.get(LocaleManager.RECORDS_LOADED) + ".";
					infoView.setText(Html.fromHtml(info));
					touchPictureView.setVisibility(View.VISIBLE);
				}
			} else {
				listView.setVisibility(View.GONE);
				listView.setAdapter(null);
				String info = "<font color=\"" + getResources().getColor(R.color.highlight) +
					"\">0</font> " + locale.get(LocaleManager.RECORDS_FOUND) + 
					", <font color=\"" + getResources().getColor(R.color.highlight)+ 
					"\">0</font> " + locale.get(LocaleManager.RECORDS_LOADED) + ".";
				infoView.setText(Html.fromHtml(info));
				emptyView.setVisibility(View.VISIBLE);
				emptyView.setText(apiResponse.getMessage());
				touchPictureView.setVisibility(View.GONE);
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
		Book book = ((SearchResultAdapter) parent.getAdapter()).getItem(position);
		Intent intent = new Intent(this, CatalogueScreen.class);
		intent.putExtra(ApiData.PARAM_RID, book.getRID());
		startActivity(intent);
	}

}
