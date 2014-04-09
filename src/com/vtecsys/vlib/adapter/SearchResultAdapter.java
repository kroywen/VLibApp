package com.vtecsys.vlib.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Book;

public class SearchResultAdapter extends BaseAdapter {
	
	private Context context;
	private List<Book> books;
	
	public SearchResultAdapter(Context context, List<Book> books) {
		this.context = context;
		this.books = books;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.search_result_list_item, null);
		}
		
		Book book = getItem(position);
		
		ImageView bookCover = (ImageView) convertView.findViewById(R.id.bookCover);
		ImageLoader.getInstance().displayImage(book.getBookCover(), bookCover);
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		title.setText(book.getTitle());
		
		TextView author = (TextView) convertView.findViewById(R.id.author);
		author.setText(book.getAuthor());
		
		TextView publisher = (TextView) convertView.findViewById(R.id.publisher);
		publisher.setText(book.getPublisher());
		
		TextView callNumber = (TextView) convertView.findViewById(R.id.callnumber);
		callNumber.setText(book.getCallNumber());
		
		TextView number = (TextView) convertView.findViewById(R.id.number);
		number.setText(String.valueOf(position + 1));
		
		return convertView;
	}

}
