package com.vtecsys.vlib.ui.screen;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.adapter.BookmarksAdapter;
import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.DialogUtils;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class BookmarksScreen extends BaseScreen implements OnItemClickListener, OnClickListener {
	
	protected View removeAllBtn;
	protected TextView infoView;
	protected ListView listView;
	protected TextView emptyView;
	protected View touchPictureView;
	
	private BookmarksAdapter adapter;
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.bookmarks_screen, null);
		setContentView(root);
		initializeViews(root);
		updateViews();
		
		if (isLoggedIn) {
			updateViews();
		} else {
			Intent intent = new Intent(this, LoginScreen.class);
			startActivityForResult(intent, REQUEST_LOGIN);
		}
	}
	
	@Override
	protected void initializeViews(View root) {
		super.initializeViews(root);
		
		removeAllBtn = root.findViewById(R.id.removeAllBtn);
		removeAllBtn.setOnClickListener(this);
		
		infoView = (TextView) findViewById(R.id.infoView);
		
		touchPictureView = findViewById(R.id.touchPictureView);
		touchPictureView.setVisibility(View.GONE);
		
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		emptyView = (TextView) findViewById(R.id.emptyView);
	}
	
	private void updateViews() {	
		String memberId = settings.getString(Settings.MEMBER_ID);
		List<Book> books = dbManager.getBookmarks(memberId);
		
		int count = Utilities.isEmpty(books) ? 0 : books.size();
		String color = String.format("#%06X", 0xFFFFFF & 
			getResources().getColor(R.color.highlight));
		String info = String.format(locale.get(LocaleManager.BOOKMARKS_COUNT), color, count);
		infoView.setText(Html.fromHtml(info));
		
		removeAllBtn.setVisibility(Utilities.isEmpty(books) ? View.GONE : View.VISIBLE);
		
		if (!Utilities.isEmpty(books)) {
			adapter = new BookmarksAdapter(this, books);
			listView.setAdapter(adapter);
			listView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		} else {
			listView.setAdapter(null);
			listView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
			emptyView.setText(locale.get(LocaleManager.BOOKMARKS_EMPTY));
		}
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Book book = adapter.getItem(position);
		openCatalogueScreen(book);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.removeAllBtn:
			showConfirmRemoveAllBookmarksDialog();
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LOGIN) {
			if (resultCode == RESULT_OK) {
				updateViews();
			} else {
				finish();
			}
		}
	}
	
	public void showConfirmRemoveBookmarkDialog(final Book book) {
		if (book == null) {
			return;
		}
		DialogUtils.showConfirmDialog(this, null, locale.get(LocaleManager.CONFIRM_REMOVE_BOOKMARK), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String memberId = settings.getString(Settings.MEMBER_ID);
				dbManager.removeBookmark(memberId, book);
				updateViews();
			}
		}, null);
	}
	
	private void showConfirmRemoveAllBookmarksDialog() {
		DialogUtils.showConfirmDialog(this, null, locale.get(LocaleManager.CONFIRM_REMOVE_ALL_BOOKMARKS), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String memberId = settings.getString(Settings.MEMBER_ID);
				dbManager.removeAllBookmarks(memberId);
				updateViews();
			}
		}, null);
	}

}
