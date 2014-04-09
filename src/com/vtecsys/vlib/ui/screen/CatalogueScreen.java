package com.vtecsys.vlib.ui.screen;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.vtecsys.vlib.model.Volume;
import com.vtecsys.vlib.model.result.CatalogueResult;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.ui.dialog.ReservationDialog;
import com.vtecsys.vlib.ui.dialog.ReservationDialog.OnReservationClickListener;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.Utilities;

public class CatalogueScreen extends BaseScreen implements OnReservationClickListener {
	
	private ImageView bookCover;
	private ListView listView;
	private TextView isbn;
	private TextView callNumber;
	private TextView author;
	private TextView title;
	private TextView edition;
	private TextView publication;
	
	private String rid;
	private Book book;
	private Volume requestedVolume;
	
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
			requestCatalogue(true);
		} else {
			showConnectionErrorDialog();
		}
	}
	
	@Override
	protected void initializeViews() {
		super.initializeViews();
		
		bookCover = (ImageView) findViewById(R.id.bookCover); 
		listView = (ListView) findViewById(R.id.listView);
		
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
	
	private void requestCatalogue(boolean hideContent) {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_CATALOGUE);
		intent.putExtra(ApiData.PARAM_RID, rid);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		startService(intent);
		showProgress(hideContent);
	}
	
	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {
		hideProgress();
		if (ApiData.COMMAND_CATALOGUE.equalsIgnoreCase(apiResponse.getRequestName())) {
			if (apiStatus == ApiService.API_STATUS_SUCCESS) {
				if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
					CatalogueResult result = (CatalogueResult) apiResponse.getData();
					updateData(result);
				}
			}
		} else if (ApiData.COMMAND_RESERVATION.equalsIgnoreCase(apiResponse.getRequestName())) {
			if (apiStatus == ApiService.API_STATUS_SUCCESS) {
				if (apiResponse.getStatus() == ApiResponse.STATUS_OK) {
					showReservationSuccessDialog();
				} else {
					DialogUtils.showDialog(this, getString(R.string.error),
						apiResponse.getMessage());
				}
			}
		}
	}
	
	private void updateData(CatalogueResult result) {
		book = result.getBook();
		
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
	
	public void tryReserveVolume(Volume volume) {
		requestedVolume = volume;
		if (volume == null) {
			return;
		}
		
		if (!isLoggedIn) {	
			Intent intent = new Intent(this, LoginScreen.class);
			startActivityForResult(intent, REQUEST_LOGIN);
			return;
		}
		
		if (Utilities.isConnectionAvailable(this)) {
			showRequestReservationDialog();
		} else {
			showConnectionErrorDialog();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LOGIN) {
			if (resultCode == RESULT_OK) {
				tryReserveVolume(requestedVolume);
			} else {
				requestedVolume = null;
			}
		}
	}
	
	private void showRequestReservationDialog() {
		ReservationDialog dialog = new ReservationDialog();
		dialog.setRetainInstance(true);
		dialog.setMode(ReservationDialog.MODE_BEFORE_RESERVATION);
		dialog.setBook(book);
		dialog.setVolume(requestedVolume);
		dialog.show(getFragmentManager(), "reservation");
	}
	
	private void showReservationSuccessDialog() {
		ReservationDialog dialog = new ReservationDialog();
		dialog.setRetainInstance(true);
		dialog.setMode(ReservationDialog.MODE_AFTER_RESERVATION);
		dialog.setBook(book);
		dialog.setVolume(requestedVolume);
		dialog.show(getFragmentManager(), "reservation");
	}
	
	private void requestReservation() {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiData.COMMAND_RESERVATION);
		intent.putExtra(ApiData.PARAM_LANG, settings.getInt(Settings.LANGUAGE));
		intent.putExtra(ApiData.PARAM_ID, settings.getString(Settings.MEMBER_ID));
		intent.putExtra(ApiData.PARAM_PASSWD, settings.getString(Settings.PASSWORD));
		intent.putExtra(ApiData.PARAM_ITEMNO, requestedVolume.getItem());
		startService(intent);
		showProgress(false);
	}

	@Override
	public void onReservationYesClick(DialogFragment dialog, Volume volume) {
		requestReservation();
	}

	@Override
	public void onReservationNoClick(DialogFragment dialog) {}

	@Override
	public void onReservationOkClick(DialogFragment dialog) {
		if (Utilities.isConnectionAvailable(this)) {
			requestCatalogue(false);
		} else {
			showConnectionErrorDialog();
		}
	}

}
