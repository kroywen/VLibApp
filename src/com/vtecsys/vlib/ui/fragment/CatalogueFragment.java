package com.vtecsys.vlib.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.api.MockApi;
import com.vtecsys.vlib.storage.Settings;

public class CatalogueFragment extends BaseFragment {
	
	private String rid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		if (args != null) {
			rid = args.getString(ApiData.PARAM_RID);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.catalogue_fragment, null);
		initializeViews(rootView);
		return rootView;
	}
	
	@Override
	protected void initializeViews(View rootView) {
		super.initializeViews(rootView);
		// TODO
	}
	
	@Override
	public void onStart() {
		super.onStart();
//		requestCatalogue(); // TODO uncomment
		MockApi.requestCatalogue(getActivity()); // TODO remove
	}
	
	private void requestCatalogue() {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiData.COMMAND_CATALOGUE);
		intent.putExtra(ApiData.PARAM_RID, rid);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		getActivity().startService(intent);
		showProgress();
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		// TODO
		Toast.makeText(getActivity(), "Response", Toast.LENGTH_SHORT).show();
	}

}
