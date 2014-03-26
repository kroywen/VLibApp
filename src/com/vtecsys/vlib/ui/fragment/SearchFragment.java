package com.vtecsys.vlib.ui.fragment;

import android.annotation.SuppressLint;
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
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.ui.screen.BookListScreen;

public class SearchFragment extends BaseFragment implements OnClickListener {
	
	public enum SearchBy {
		TITLE,
		AUTHOR,
		SUBJECT,
		SERIES,
		ISBN,
		ALL
	}
	
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
		sortByView = (Spinner) rootView.findViewById(R.id.sortByView);
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchTitleBtn:
			search(SearchBy.TITLE);
			break;
		case R.id.searchAuthorBtn:
			search(SearchBy.AUTHOR);
			break;
		case R.id.searchSubjectBtn:
			search(SearchBy.SUBJECT);
			break;
		case R.id.searchSeriesBtn:
			search(SearchBy.SERIES);
			break;
		case R.id.searchAllBtn:
			search(SearchBy.ALL);
			break;
		case R.id.searchIsbnBtn:
			search(SearchBy.ISBN);
			break;
		case R.id.browseAuthorBtn:
			break;
		case R.id.browseSubjectBtn:
			break;
		case R.id.browseSeriesBtn:
			break;
		}
	}
	
	@SuppressLint("DefaultLocale")
	private void search(SearchBy search) {
		Intent intent = new Intent(getActivity(), BookListScreen.class);
		
		intent.putExtra(ApiData.PARAM_TERM, searchView.getText().toString());
		intent.putExtra(ApiData.PARAM_SEARCH_BY, search.name().toLowerCase());
		intent.putExtra(ApiData.PARAM_SORT_BY, sortByView.getSelectedItemPosition());
		
		getActivity().startActivity(intent);
	}

}
