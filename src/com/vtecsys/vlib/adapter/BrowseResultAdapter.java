package com.vtecsys.vlib.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Auth;
import com.vtecsys.vlib.storage.Settings;
import com.vtecsys.vlib.util.Utilities;

public class BrowseResultAdapter extends BaseAdapter {
	
	private Context context;
	private List<Auth> authes;
	private Settings settings;
	
	public BrowseResultAdapter(Context context, List<Auth> authes) {
		this.context = context;
		this.authes = authes;
		settings = new Settings(context);
	}

	@Override
	public int getCount() {
		return authes.size();
	}

	@Override
	public Auth getItem(int position) {
		return authes.get(position);
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
			convertView = inflater.inflate(R.layout.browse_result_list_item, null);
			float fontSize = Utilities.getFontSize(
				settings.getInt(Settings.FONT_SIZE));
			Utilities.setFontSize(convertView, fontSize);
		}
		
		Auth auth = getItem(position);
		
		TextView authEntry = (TextView) convertView.findViewById(R.id.text);
		authEntry.setText((position + 1) + ". " + auth.getAuthEntry());
		
		return convertView;
	}

}
