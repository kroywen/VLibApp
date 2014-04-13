package com.vtecsys.vlib.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.adapter.SimpleAdapter;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.ui.screen.BrowseResultScreen;
import com.vtecsys.vlib.ui.screen.ISBNScannerScreen;
import com.vtecsys.vlib.ui.screen.SearchResultScreen;
import com.vtecsys.vlib.util.LocaleManager;

public class SearchFragment extends BaseFragment implements OnClickListener {
	
	public static final int REQUEST_SCAN = 0;
	
	private EditText searchView;
	private Spinner sortByView;
	private Button searchTitleBtn;
	private Button searchAuthorBtn;
	private Button searchSubjectBtn;
	private Button searchSeriesBtn;
	private Button searchAllBtn;
	private Button searchIsbnBtn;
	private Button browseAuthorBtn;
	private Button browseSubjectBtn;
	private Button browseSeriesBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search_fragment, null);
		initializeViews(rootView);
		return rootView;
	}
	
	@Override
	protected void initializeViews(View rootView) {
		super.initializeViews(rootView);
		searchView = (EditText) rootView.findViewById(R.id.searchView);
		searchView.setHint(locale.get(LocaleManager.PLEASE_ENTER_THE_KEYWORDS));
		sortByView = (Spinner) rootView.findViewById(R.id.sortByView);
		populateSortBySpinner();
		searchTitleBtn = (Button) rootView.findViewById(R.id.searchTitleBtn);
		searchTitleBtn.setOnClickListener(this);
		searchAuthorBtn = (Button) rootView.findViewById(R.id.searchAuthorBtn);
		searchAuthorBtn.setOnClickListener(this);
		searchSubjectBtn = (Button) rootView.findViewById(R.id.searchSubjectBtn);
		searchSubjectBtn.setOnClickListener(this);
		searchSeriesBtn = (Button) rootView.findViewById(R.id.searchSeriesBtn);
		searchSeriesBtn.setOnClickListener(this);
		searchAllBtn = (Button) rootView.findViewById(R.id.searchAllBtn);
		searchAllBtn.setOnClickListener(this);
		searchIsbnBtn = (Button) rootView.findViewById(R.id.searchIsbnBtn);
		searchIsbnBtn.setOnClickListener(this);
		browseAuthorBtn = (Button) rootView.findViewById(R.id.browseAuthorBtn);
		browseAuthorBtn.setOnClickListener(this);
		browseSubjectBtn = (Button) rootView.findViewById(R.id.browseSubjectBtn);
		browseSubjectBtn.setOnClickListener(this);
		browseSeriesBtn = (Button) rootView.findViewById(R.id.browseSeriesBtn);
		browseSeriesBtn.setOnClickListener(this);
	}
	
	private void populateSortBySpinner() {
		String[] items = new String[] {
			locale.get(LocaleManager.NONE),
			locale.get(LocaleManager.TITLE),
			locale.get(LocaleManager.AUTHOR),
			locale.get(LocaleManager.CALL_NO),
			locale.get(LocaleManager.PUBLICATION_DATE)
		};
		SimpleAdapter adapter = new SimpleAdapter(
			getActivity(), R.layout.simple_spinner_item, items);
		sortByView.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchTitleBtn:
			showSearchScreen(searchView.getText().toString(), "title");
			break;
		case R.id.searchAuthorBtn:
			showSearchScreen(searchView.getText().toString(), "author");
			break;
		case R.id.searchSubjectBtn:
			showSearchScreen(searchView.getText().toString(), "subject");
			break;
		case R.id.searchSeriesBtn:
			showSearchScreen(searchView.getText().toString(), "series"); 
			break;
		case R.id.searchAllBtn:
			showSearchScreen(searchView.getText().toString(), "all"); 
			break;
		case R.id.searchIsbnBtn:
			showISBNScannerScreen();
			break;
		case R.id.browseAuthorBtn:
			showBrowseScreen("author");
			break;
		case R.id.browseSubjectBtn:
			showBrowseScreen("subject");
			break;
		case R.id.browseSeriesBtn:
			showBrowseScreen("series");
			break;
		}
	}
	
	private void showSearchScreen(String term, String searchBy) {
		Intent intent = new Intent(getActivity(), SearchResultScreen.class);
		
		intent.putExtra(ApiData.PARAM_TYPE, ApiData.TYPE_SEARCH);
		intent.putExtra(ApiData.PARAM_TERM, term);
		intent.putExtra(ApiData.PARAM_SEARCH_BY, searchBy);
		intent.putExtra(ApiData.PARAM_SORT_BY, sortByView.getSelectedItemPosition());
		
		getActivity().startActivity(intent);
	}
	
	private void showBrowseScreen(String browseBy) {
		Intent intent = new Intent(getActivity(), BrowseResultScreen.class);
		
		intent.putExtra(ApiData.PARAM_TERM, searchView.getText().toString());
		intent.putExtra(ApiData.PARAM_BROWSE_BY, browseBy);
		intent.putExtra(ApiData.PARAM_SORT_BY, sortByView.getSelectedItemPosition());
		
		getActivity().startActivity(intent);
	}
	
	private void showISBNScannerScreen() {
		Intent intent = new Intent(getActivity(), ISBNScannerScreen.class);
		startActivityForResult(intent, REQUEST_SCAN);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_SCAN && resultCode == Activity.RESULT_OK) {
			if (data != null) {
				String isbn = data.getStringExtra("isbn");
				showSearchScreen(isbn, "isbn");
			}
		}
	}

}
