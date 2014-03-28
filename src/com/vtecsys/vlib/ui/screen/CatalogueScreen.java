package com.vtecsys.vlib.ui.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vtecsys.vlib.R;
import com.vtecsys.vlib.adapter.VolumeAdapter;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.model.CatalogueResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class CatalogueScreen extends BaseScreen implements OnClickListener {
	
	private ImageView bookCover;
	private Button reserveBtn;
	private ListView listView;
	private TextView isbn;
	private TextView callNumber;
	private TextView author;
	private TextView title;
	private TextView edition;
	private TextView publication;
	
	private String rid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalogue_screen);
		initializeViews();
		
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		rid = intent.getStringExtra(ApiData.PARAM_RID);
		
		if (Utilities.isConnectionAvailable(this)) {
			requestCatalogue();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	@Override
	protected void initializeViews() {
		super.initializeViews();
		
		bookCover = (ImageView) findViewById(R.id.bookCover); 
		listView = (ListView) findViewById(R.id.listView);
		reserveBtn = (Button) findViewById(R.id.reserveBtn);
		reserveBtn.setOnClickListener(this);
		
		isbn = (TextView) findViewById(R.id.isbn);
		callNumber = (TextView) findViewById(R.id.callnumber);
		author = (TextView) findViewById(R.id.author);
		title = (TextView) findViewById(R.id.title);
		edition = (TextView) findViewById(R.id.edition);
		publication = (TextView) findViewById(R.id.publication);
		
		LayoutInflater inflater = (LayoutInflater) 
			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater.inflate(R.layout.volume_header, null);
		listView.addHeaderView(headerView, null, false);
	}
	
	private void requestCatalogue() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_CATALOGUE);
		intent.putExtra(ApiData.PARAM_RID, rid);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(true);
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (apiStatus == ApiService.API_STATUS_SUCCESS) {
			if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
				CatalogueResult result = (CatalogueResult) apiResponse.getData();
				updateData(result);
			}
		}
	}
	
	private void updateData(CatalogueResult result) {
		Book book = result.getBook();
		
		ImageLoader.getInstance().displayImage(book.getBookCover(), bookCover);
		
		isbn.setText(book.getISBN());
		callNumber.setText(book.getCallNumber());
		author.setText(book.getAuthor());
		title.setText(book.getTitle());
		edition.setText(book.getEdition());
		publication.setText(book.getPublication());
		
		VolumeAdapter adapter = new VolumeAdapter(this, result.getVolumes());
		listView.setAdapter(adapter);
		Utilities.setListViewHeightBasedOnChildren(listView);
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
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, LoginScreen.class);
		startActivity(intent);
	}

}
