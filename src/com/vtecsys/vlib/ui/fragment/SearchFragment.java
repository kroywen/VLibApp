package com.vtecsys.vlib.ui.fragment;

import android.app.Fragment;
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
import com.vtecsys.vlib.ui.screen.BrowseResultScreen;
import com.vtecsys.vlib.ui.screen.SearchResultScreen;

public class SearchFragment extends Fragment implements OnClickListener {
	
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
	
	protected void initializeViews(View rootView) {
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
		case R.id.searchAuthorBtn:
		case R.id.searchSubjectBtn:
		case R.id.searchSeriesBtn:
		case R.id.searchAllBtn:
			showSearchScreen((String) v.getTag());
			break;
		case R.id.searchIsbnBtn:
			// TODO
			break;
		case R.id.browseAuthorBtn:
		case R.id.browseSubjectBtn:
		case R.id.browseSeriesBtn:
			showBrowseScreen((String) v.getTag());
			break;
		}
	}
	
	private void showSearchScreen(String searchBy) {
		Intent intent = new Intent(getActivity(), SearchResultScreen.class);
		
		intent.putExtra(ApiData.PARAM_TYPE, ApiData.TYPE_SEARCH);
		intent.putExtra(ApiData.PARAM_TERM, searchView.getText().toString());
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

}
