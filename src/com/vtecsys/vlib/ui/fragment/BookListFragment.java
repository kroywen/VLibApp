package com.vtecsys.vlib.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;

public class BookListFragment extends BaseFragment {
	
	private TextView infoView;
	private ListView listView; 
	
	private String term;
	private String sortBy;
	private String searchBy;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] sortByParams = getResources().getStringArray(R.array.sort_by_params);
		
		Bundle args = getArguments();
		if (args != null) {
			term = args.getString(ApiData.PARAM_TERM);
			sortBy = sortByParams[args.getInt(ApiData.PARAM_SORT_BY)];
			searchBy = args.getString(ApiData.PARAM_SEARCH_BY);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.book_list_fragment, null);
		initializeViews(rootView);
		return rootView;
	}
	
	@Override
	protected void initializeViews(View rootView) {
		super.initializeViews(rootView);
		infoView = (TextView) rootView.findViewById(R.id.infoView);
		listView = (ListView) rootView.findViewById(R.id.listView);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		requestBookList(); 
	}
	
	private void requestBookList() {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiData.COMMAND_SEARCH);
		intent.putExtra(ApiData.PARAM_TYPE, ApiData.TYPE_SEARCH);
		intent.putExtra(ApiData.PARAM_TERM, term);
		intent.putExtra(ApiData.PARAM_SORT_BY, sortBy);
		intent.putExtra(ApiData.PARAM_SEARCH_BY, searchBy);
		intent.putExtra(ApiData.PARAM_LANG, prefs.getInt(ApiData.PARAM_LANG, 0));
		getActivity().startService(intent);
		showProgress();
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
	}

}
