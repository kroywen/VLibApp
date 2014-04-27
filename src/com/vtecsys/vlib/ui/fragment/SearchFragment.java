package com.vtecsys.vlib.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
	
	private AutoCompleteTextView searchView;
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
		searchView = (AutoCompleteTextView) rootView.findViewById(R.id.searchView);
		searchView.setHint(locale.get(LocaleManager.PLEASE_ENTER_THE_KEYWORDS));
		searchView.setThreshold(1);
		searchView.addTextChangedListener(watcher);
		
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
			showBrowseScreen(searchView.getText().toString(), "author");
			break;
		case R.id.browseSubjectBtn:
			showBrowseScreen(searchView.getText().toString(), "subject");
			break;
		case R.id.browseSeriesBtn:
			showBrowseScreen(searchView.getText().toString(), "series");
			break;
		}
	}
	
	private void showSearchScreen(String term, String searchBy) {
		saveTerm(term);
		
		Intent intent = new Intent(getActivity(), SearchResultScreen.class);
		intent.putExtra(ApiData.PARAM_TYPE, ApiData.TYPE_SEARCH);
		intent.putExtra(ApiData.PARAM_TERM, term);
		intent.putExtra(ApiData.PARAM_SEARCH_BY, searchBy);
		intent.putExtra(ApiData.PARAM_SORT_BY, sortByView.getSelectedItemPosition());
		getActivity().startActivity(intent);
	}
	
	private void showBrowseScreen(String term, String browseBy) {
		saveTerm(term);
		
		Intent intent = new Intent(getActivity(), BrowseResultScreen.class);
		intent.putExtra(ApiData.PARAM_TERM, term);
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
	
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			if (TextUtils.isEmpty(text)) {
				searchView.setAdapter(null);
			} else {
				List<String> words = dbManager.getWords(text);
				if (words == null || words.isEmpty()) {
					searchView.setAdapter(null);
				} else {
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				        android.R.layout.simple_dropdown_item_1line, words);
					searchView.setAdapter(adapter);
				}
			}
		}
	};
	
	private void saveTerm(String term) {
		boolean found = dbManager.hasWord(term);
		if (!found) {
			dbManager.saveWord(term);
		}
	}

}
