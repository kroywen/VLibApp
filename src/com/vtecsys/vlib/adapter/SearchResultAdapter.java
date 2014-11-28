package com.vtecsys.vlib.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Book;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.storage.database.DatabaseManager;
import com.vtecsys.vlib.ui.screen.BaseScreen;
import com.vtecsys.vlib.ui.screen.SearchResultScreen;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class SearchResultAdapter extends BaseAdapter {
	
	protected Context context;
	protected List<Book> books;
	protected LocaleManager locale;
	protected Settings settings;
	protected DatabaseManager dbManager;
	protected String memberId;
	
	public SearchResultAdapter(Context context, List<Book> books) {
		this.context = context;
		this.books = books;
		locale = LocaleManager.getInstance();
		settings = new Settings(context);
		dbManager = DatabaseManager.newInstance(context);
		memberId = settings.getString(Settings.MEMBER_ID);
	}

	@Override
	public int getCount() {
		return books.size();
	}

	@Override
	public Book getItem(int position) {
		return books.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.search_result_list_item, null);
			locale.apply(convertView);
			float fontSize = Utilities.getFontSize(
				settings.getInt(Settings.FONT_SIZE));
			Utilities.setFontSize(convertView, fontSize);
		}
		
		final Book book = getItem(position);
		
		ImageView bookCover = (ImageView) convertView.findViewById(R.id.bookCover);
		ImageLoader.getInstance().displayImage(book.getBookCover(), bookCover);
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		title.setText(book.getTitle());
		
		TextView author = (TextView) convertView.findViewById(R.id.author);
		author.setText(book.getAuthor());
		
		TextView publication = (TextView) convertView.findViewById(R.id.publication);
		publication.setText(book.getPublication());
		
		TextView callNumber = (TextView) convertView.findViewById(R.id.callnumber);
		callNumber.setText(book.getCallNumber());
		
		TextView number = (TextView) convertView.findViewById(R.id.number);
		String numberText = "<font color=\"" + 
			context.getResources().getColor(R.color.highlight) + "\">" + 
			String.valueOf(position + 1) + "</font>";
		number.setText(Html.fromHtml(numberText));
		
		final ImageView bookmarkBtn = (ImageView) convertView.findViewById(R.id.bookmarkBtn);
		bookmarkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String memberId = settings.getString(Settings.MEMBER_ID);
				if (!TextUtils.isEmpty(memberId)) {
					boolean isBookmark = dbManager.isBookmarked(memberId, book);
					if (!isBookmark) {
						((SearchResultScreen) context).showBookmarkCatalogueDialog(book, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								notifyDataSetChanged();
								updateBookmarkBtn(bookmarkBtn, book);
							}
						});
					}
				}
			}
		});
		int resId = R.drawable.icon_bookmark;
		String memeberId = settings.getString(Settings.MEMBER_ID);
		if (!TextUtils.isEmpty(memeberId)) {
			boolean isBookmark = dbManager.isBookmarked(memberId, book);
			resId = isBookmark ? R.drawable.icon_bookmark_disabled : resId;	
		}
		bookmarkBtn.setImageResource(resId);
		
		View detailsBtn = convertView.findViewById(R.id.detailsBtn);
		detailsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseScreen) context).openCatalogueScreen(book);
			}
		});
		
		return convertView;
	}
	
	private void updateBookmarkBtn(ImageView bookmarkBtn, Book book) {
		int resId = R.drawable.icon_bookmark;
		String memberId = settings.getString(Settings.MEMBER_ID);
		if (!TextUtils.isEmpty(memberId)) {
			boolean isBookmark = dbManager.isBookmarked(memberId, book);
			resId = isBookmark ? R.drawable.icon_bookmark_disabled : resId;	
		}	
		bookmarkBtn.setImageResource(resId);
	}

}
