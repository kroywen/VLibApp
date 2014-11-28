package com.vtecsys.vlib.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
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
import com.vtecsys.vlib.ui.screen.BaseScreen;
import com.vtecsys.vlib.ui.screen.BookmarksScreen;
import com.vtecsys.vlib.util.LocaleManager;
import com.vtecsys.vlib.util.Utilities;

public class BookmarksAdapter extends BaseAdapter {
	
	protected Context context;
	protected List<Book> books;
	protected LocaleManager locale;
	protected Settings settings;
	
	public BookmarksAdapter(Context context, List<Book> books) {
		this.context = context;
		this.books = books;
		locale = LocaleManager.getInstance();
		settings = new Settings(context);
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
			convertView = inflater.inflate(R.layout.bookmark_list_item, null);
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
		
		View removeBtn = convertView.findViewById(R.id.removeBtn);
		removeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((BookmarksScreen) context).showConfirmRemoveBookmarkDialog(book);
			}
		});
		
		View detailsBtn = convertView.findViewById(R.id.detailsBtn);
		detailsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseScreen) context).openCatalogueScreen(book);
			}
		});
		
		return convertView;
	}

}
