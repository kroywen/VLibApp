package com.vtecsys.vlib.ui.screen;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vtecsys.vlib.R;
import com.vtecsys.vlib.api.ApiData;
import com.vtecsys.vlib.api.ApiResponse;
import com.vtecsys.vlib.api.ApiResponseReceiver;
import com.vtecsys.vlib.api.ApiService;
import com.vtecsys.vlib.api.OnApiResponseListener;
import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.storage.database.DatabaseManager;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class BaseScreen extends Activity implements OnApiResponseListener {
	
	public static final int REQUEST_LOGIN = 0;
	
	protected ApiResponseReceiver responseReceiver;
	public static boolean isLoggedIn;
	protected Settings settings;
	protected DatabaseManager dbManager;
	protected LocaleManager locale;
	protected LayoutInflater inflater;
	protected View mainContent;
	protected View progress;
	private Book bookmark;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new Settings(this);
		dbManager = DatabaseManager.newInstance(this);
		locale = LocaleManager.getInstance();
		locale.setLanguage(this, settings.getInt(Settings.LANGUAGE));
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		
		setTitle(settings.getString(Settings.APP_TITLE));
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.build();
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
			.defaultDisplayImageOptions(options)
			.build();
		ImageLoader.getInstance().init(configuration);
	}
	
	protected void applyFontSize(View view) {
		Utilities.setFontSize(view, Utilities.getFontSize(
			settings.getInt(Settings.FONT_SIZE)));
	}
	
	protected void applyLocale(View view) {
		locale.apply(view);
	}

	protected void initializeViews(View root) {
		applyFontSize(root);
		applyLocale(root);
		
		progress = findViewById(R.id.progress);
		mainContent = findViewById(R.id.main_content);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter(ApiService.ACTION_API_RESULT);
		responseReceiver = new ApiResponseReceiver(this);
		LocalBroadcastManager.getInstance(this).registerReceiver(
			responseReceiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(responseReceiver);
	}
	
	protected void showProgress(boolean hideContent) {
		if (progress != null) {
			progress.setVisibility(View.VISIBLE);
			if (hideContent && mainContent != null) {
				mainContent.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	protected void hideProgress() {
		if (progress != null) {
			progress.setVisibility(View.INVISIBLE);
		}
		if (mainContent != null) {
			mainContent.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onApiResponse(int apiStatus, ApiResponse apiResponse) {}
	
	public void showConnectionErrorDialog() {
		DialogUtils.showDialog(this,
			locale.get(LocaleManager.ERROR), 
			locale.get(LocaleManager.NO_CONNECTION_ERROR),
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			},
			new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			}
		);
	}
	
	public void showBookmarkCatalogueDialog(final Book book) {
		if (book != null) {
			DialogUtils.showConfirmDialog(this, null, locale.get(LocaleManager.BOOKMARK_CATALOGUE), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					bookmarkBook(book);
				}
			}, null);
		}
	}
	
	private void bookmarkBook(Book book) {
		if (isLoggedIn) {
			String memberId = settings.getString(Settings.MEMBER_ID);
			List<Book> bookmarks = dbManager.getBookmarks(memberId);
			if (!Utilities.isEmpty(bookmarks)) {
				int count = bookmarks.size();
				int maxBookmarks = settings.getInt(Settings.MAX_BOOKMARKS, 10);
				if (count >= maxBookmarks) {
					DialogUtils.showDialog(BaseScreen.this, null, locale.get(LocaleManager.MAXIMUM_BOOKMARKS_REACHED));
				} else {
					dbManager.addBookmark(memberId, book);
				}
			} else {
				dbManager.addBookmark(memberId, book);
			}
		} else {
			bookmark = book;
			Intent intent = new Intent(BaseScreen.this, LoginScreen.class);
			startActivityForResult(intent, REQUEST_LOGIN);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
			bookmarkBook(bookmark);
			bookmark = null;
		}
	}
	
	public void openCatalogueScreen(Book book) {
		if (book != null) {
			Intent intent = new Intent(this, CatalogueScreen.class);
			intent.putExtra(ApiData.PARAM_RID, book.getRID());
			startActivity(intent);
		}
	}

}
