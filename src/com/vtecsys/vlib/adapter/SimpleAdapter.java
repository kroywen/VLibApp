package com.vtecsys.vlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class SimpleAdapter extends BaseAdapter {
	
	private Context context;
	private String[] items;
	private Settings settings;
	private int resourceId;
	
	public SimpleAdapter(Context context, int resourceId, String[] items) {
		this.context = context;
		this.resourceId = resourceId;
		this.items = items;
		settings = new Settings(context);
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public String getItem(int position) {
		return items[position];
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
			convertView = inflater.inflate(resourceId, null);
			Utilities.setFontSize(convertView, Utilities.getFontSize(
				settings.getInt(Settings.FONT_SIZE)));
		}
		
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(items[position]);
		
		return convertView;
	}

}
