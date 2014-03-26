package com.vtecsys.vlib.ui.screen;

import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.ui.fragment.CatalogueFragment;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class CatalogueScreen extends BaseScreen {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		
		Bundle args = new Bundle();
		args.putString(ApiData.PARAM_RID, 
			intent.getStringExtra(ApiData.PARAM_RID));
		
		CatalogueFragment fragment = new CatalogueFragment();
		fragment.setArguments(args);
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(android.R.id.content, fragment).commit();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
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

}
