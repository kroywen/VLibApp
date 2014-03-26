package com.vtecsys.vlib.ui.screen;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.ui.fragment.BookListFragment;

public class BookListScreen extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		
		Bundle args = new Bundle();
		args.putString(ApiData.PARAM_TERM, 
			intent.getStringExtra(ApiData.PARAM_TERM));
		args.putInt(ApiData.PARAM_SORT_BY, 
			intent.getIntExtra(ApiData.PARAM_SORT_BY, 0));
		args.putString(ApiData.PARAM_SEARCH_BY, 
			intent.getStringExtra(ApiData.PARAM_SEARCH_BY));
		
		BookListFragment fragment = new BookListFragment();
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
