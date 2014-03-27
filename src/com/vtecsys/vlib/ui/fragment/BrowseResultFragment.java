package com.vtecsys.vlib.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vtecsys.vlib.model.BrowseResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.ui.screen.SearchResultScreen;

public class BrowseResultFragment extends BaseFragment implements OnItemClickListener {
	
	private TextView infoView;
	private ListView listView;
	private TextView emptyView;
	
	private String term;
	private String sortBy;
	private String browseBy;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] sortByParams = getResources().getStringArray(R.array.sort_by_params);
		
		Bundle args = getArguments();
		if (args != null) {
			term = args.getString(ApiData.PARAM_TERM);
			sortBy = sortByParams[args.getInt(ApiData.PARAM_SORT_BY)];
			browseBy = args.getString(ApiData.PARAM_BROWSE_BY);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.browse_result_fragment, null);
		initializeViews(rootView);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		requestBrowse();
	}
	
	@Override
	protected void initializeViews(View rootView) {
		super.initializeViews(rootView);
		infoView = (TextView) rootView.findViewById(R.id.infoView);
		infoView.setText(getString(R.string.search_result_pattern, 0, 0));
		
		listView = (ListView) rootView.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		emptyView = (TextView) rootView.findViewById(R.id.emptyView);
	}
	
	private void requestBrowse() {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiData.COMMAND_BROWSE);
		intent.putExtra(ApiData.PARAM_TERM, term);
		intent.putExtra(ApiData.PARAM_SORT_BY, sortBy);
		intent.putExtra(ApiData.PARAM_BROWSE_BY, browseBy);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		getActivity().startService(intent);
		showProgress();
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				BrowseResult browseResult = (BrowseResult) apiResponse.getData();
				List<Auth> authes = browseResult.getAuthes();
				BrowseResultAdapter adapter = new BrowseResultAdapter(getActivity(), authes);
				listView.setAdapter(adapter);
				listView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
				infoView.setText(getString(R.string.browse_result_pattern,
					browseResult.getLoaded()));
			} else {
				listView.setVisibility(View.GONE);
				listView.setAdapter(null);
				infoView.setText(getString(R.string.search_result_pattern, 0, 0));
				emptyView.setVisibility(View.VISIBLE);
				emptyView.setText(apiResponse.getMessage());
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Auth auth = ((BrowseResultAdapter) parent.getAdapter()).getItem(position);
		Intent intent = new Intent(getActivity(), SearchResultScreen.class);
		intent.putExtra(ApiData.PARAM_TYPE, ApiData.TYPE_BROWSE);
		intent.putExtra(ApiData.PARAM_TERM, auth.getAuthNo());
		intent.putExtra(ApiData.PARAM_SORT_BY, 1);
		getActivity().startActivity(intent);
	}

}
