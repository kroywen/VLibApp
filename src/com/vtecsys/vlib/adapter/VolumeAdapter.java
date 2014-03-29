package com.vtecsys.vlib.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.vtecsys.vlib.R;
import com.vtecsys.vlib.model.Volume;

public class VolumeAdapter extends BaseAdapter {
	
	private Context context;
	private List<Volume> volumes;
	
	public VolumeAdapter(Context context, List<Volume> volumes) {
		this.context = context;
		this.volumes = volumes;
	}

	@Override
	public int getCount() {
		return volumes.size();
	}

	@Override
	public Volume getItem(int position) {
		return volumes.get(position);
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
			convertView = inflater.inflate(R.layout.volume_list_item, null);
		}
		
		Volume volume = getItem(position);
		
		TextView vol = (TextView) convertView.findViewById(R.id.volume);
		vol.setText(volume.getVolume());
		
		TextView item = (TextView) convertView.findViewById(R.id.item);
		item.setText(volume.getItem());
		
		TextView status = (TextView) convertView.findViewById(R.id.status);
		status.setText(volume.getStatus());
		
		TextView location = (TextView) convertView.findViewById(R.id.location);
		location.setText(volume.getLocation());
		
		Button renewBtn = (Button) convertView.findViewById(R.id.renewBtn);
		renewBtn.setEnabled(volume.canReserve());
		
		return convertView;
	}

}
