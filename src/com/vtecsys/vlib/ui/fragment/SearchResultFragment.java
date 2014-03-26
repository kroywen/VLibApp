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
import com.vtecsys.vlib.adapter.SearchResultAdapter;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.api.MockApi;
import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.model.SearchResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.ui.screen.CatalogueScreen;

public class SearchResultFragment extends BaseFragment implements OnItemClickListener {
	
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
		View rootView = inflater.inflate(R.layout.search_result_fragment, null);
		initializeViews(rootView);
		return rootView;
	}
	
	@Override
	protected void initializeViews(View rootView) {
		super.initializeViews(rootView);
		infoView = (TextView) rootView.findViewById(R.id.infoView);
		listView = (ListView) rootView.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
	}
	
	@Override
	public void onStart() {
		super.onStart();
//		requestSearch(); // TODO uncomment
		MockApi.requestSearch(getActivity()); // TODO remove
	}
	
	private void requestSearch() {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiData.COMMAND_SEARCH);
		intent.putExtra(ApiData.PARAM_TYPE, ApiData.TYPE_SEARCH);
		intent.putExtra(ApiData.PARAM_TERM, term);
		intent.putExtra(ApiData.PARAM_SORT_BY, sortBy);
		intent.putExtra(ApiData.PARAM_SEARCH_BY, searchBy);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		getActivity().startService(intent);
		showProgress();
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				SearchResult searchResult = (SearchResult) apiResponse.getData();
				List<Book> books = searchResult.getBooks();
				SearchResultAdapter adapter = new SearchResultAdapter(getActivity(), books);
				listView.setAdapter(adapter);
				
				infoView.setText(getString(R.string.search_result_pattern, 
					searchResult.getHits(), searchResult.getLoaded()));
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Book book = ((SearchResultAdapter) parent.getAdapter()).getItem(position);
		Intent intent = new Intent(getActivity(), CatalogueScreen.class);
		intent.putExtra(ApiData.PARAM_RID, book.getRID());
		getActivity().startActivity(intent);
	}

}
