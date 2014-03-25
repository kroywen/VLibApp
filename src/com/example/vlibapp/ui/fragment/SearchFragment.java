package com.example.vlibapp.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.vlibapp.R;

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
	
	private void initializeViews(View rootView) {
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
			break;
		case R.id.searchAuthorBtn:
			break;
		case R.id.searchSubjectBtn:
			break;
		case R.id.searchSeriesBtn:
			break;
		case R.id.searchAllBtn:
			break;
		case R.id.searchIsbnBtn:
			break;
		case R.id.browseAuthorBtn:
			break;
		case R.id.browseSubjectBtn:
			break;
		case R.id.browseSeriesBtn:
			break;
		}
	}

}
